package com.example.data

import com.example.data.local.UnitDao
import com.example.data.local.UnitEntity
import com.example.model.Faction
import com.example.model.UnitType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository for managing military units persistence via Room Database.
 * Seeds initial Mesopotamia army roster and abstracts DAO queries from ViewModel.
 */
class UnitRepository(private val unitDao: UnitDao) {

    val allUnits: Flow<List<UnitEntity>> = unitDao.getAllUnits()

    fun getUnitsForFaction(factionId: String): Flow<List<UnitEntity>> =
        unitDao.getUnitsByFaction(factionId)

    suspend fun getUnitById(id: String): UnitEntity? = withContext(Dispatchers.IO) {
        unitDao.getUnitByIdOnce(id)
    }

    suspend fun getUnitByFactionAndType(factionId: String, unitTypeId: String): UnitEntity? = withContext(Dispatchers.IO) {
        unitDao.getUnitByFactionAndType(factionId, unitTypeId)
    }

    suspend fun insertUnit(unit: UnitEntity) = withContext(Dispatchers.IO) {
        unitDao.insertUnit(unit)
    }

    suspend fun seedInitialUnitsIfEmpty() = withContext(Dispatchers.IO) {
        val count = unitDao.getUnitsCount()
        if (count == 0) {
            val initialUnits = mutableListOf<UnitEntity>()
            Faction.ALL_FACTIONS.forEach { faction ->
                UnitType.ALL_UNITS.forEach { unitType ->
                    // Faction tactical military specialties
                    val attackBonus = when {
                        faction.id == "lagash" && unitType.id == "phalanx" -> 8
                        faction.id == "kish" && unitType.id == "chariot" -> 10
                        faction.id == "umma" && unitType.id == "spearmen" -> 5
                        faction.id == "uruk" && unitType.id == "archers" -> 6
                        faction.id == "elam" && unitType.id == "phalanx" -> 5
                        else -> 0
                    }
                    val defenseBonus = when {
                        faction.id == "lagash" -> 6
                        faction.id == "uruk" -> 8 // Walls and armor tradition
                        faction.id == "elam" -> 5
                        else -> 0
                    }
                    val speedBonus = when {
                        faction.id == "kish" && unitType.id == "chariot" -> 1
                        faction.id == "ur" && unitType.id == "caravan" -> 1
                        else -> 0
                    }
                    val icon = when (unitType.id) {
                        "spearmen" -> "🗡️"
                        "phalanx" -> "🛡️"
                        "chariot" -> "🐎"
                        "archers" -> "🏹"
                        "priest" -> "📜"
                        "caravan" -> "🐪"
                        else -> "⚔️"
                    }

                    val customName = when {
                        faction.id == "lagash" && unitType.id == "phalanx" -> "Фаланга «Стели шулік» Лагаша"
                        faction.id == "kish" && unitType.id == "chariot" -> "Царська колісниця Лугаля Кіша"
                        faction.id == "uruk" && unitType.id == "archers" -> "Захисники високих мурів Урука"
                        faction.id == "ur" && unitType.id == "caravan" -> "Морський караван Дільмуна (Ур)"
                        faction.id == "nippur" && unitType.id == "priest" -> "Верховний жрець бога Енліля"
                        faction.id == "elam" && unitType.id == "phalanx" -> "Гірські щитоносці Загросу"
                        else -> "${faction.name}: ${unitType.name}"
                    }

                    initialUnits.add(
                        UnitEntity(
                            id = "${faction.id}_${unitType.id}",
                            unitTypeId = unitType.id,
                            name = customName,
                            role = unitType.role,
                            factionId = faction.id,
                            attack = unitType.strength + attackBonus,
                            defense = unitType.defense + defenseBonus,
                            movement = unitType.speed + speedBonus,
                            upkeepGrain = unitType.upkeepGrain,
                            costGrain = unitType.cost.grain,
                            costClay = unitType.cost.clay,
                            costBronze = unitType.cost.bronze,
                            costSilver = unitType.cost.silver,
                            description = unitType.description,
                            historyFact = unitType.historyFact,
                            iconEmoji = icon
                        )
                    )
                }
            }
            unitDao.insertUnits(initialUnits)
        }
    }
}

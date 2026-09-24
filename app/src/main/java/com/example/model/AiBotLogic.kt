package com.example.model

import kotlin.random.Random

data class BotTurnResult(
    val updatedCities: List<City>,
    val updatedRelations: Map<String, FactionRelation>,
    val chronicleEntries: List<String>,
    val triggeredPlayerSiege: DefensiveSiegeEngagement? = null
)

data class DefensiveSiegeEngagement(
    val attackingFactionId: String,
    val targetCityId: String,
    val attackerTroopDescription: String,
    val attackerStrength: Int,
    val attackerGarrison: Map<String, Int>
)

object AiBotLogic {

    fun processAiTurn(
        currentGameState: GameState
    ): BotTurnResult {
        var cities = currentGameState.cities
        var relations = currentGameState.relations.toMutableMap()
        val chronicle = mutableListOf<String>()
        var pendingPlayerSiege: DefensiveSiegeEngagement? = null

        val nonPlayerFactions = Faction.ALL_FACTIONS.filter { it.id != currentGameState.playerFactionId }

        for (faction in nonPlayerFactions) {
            val factionCities = cities.filter { it.factionId == faction.id }
            if (factionCities.isEmpty()) continue

            // 1. Bot City Building & Construction Logic
            val updatedFactionCities = factionCities.map { city ->
                var updatedCity = city

                // Progress building in progress
                if (updatedCity.buildingInProgress != null) {
                    val remaining = updatedCity.buildingTurnsRemaining - 1
                    if (remaining <= 0) {
                        val finishedBld = updatedCity.buildingInProgress!!
                        val bldObj = Building.getById(finishedBld)
                        chronicle.add("Місто ${city.name} (${faction.name}) завершило зведення «${bldObj.name}»!")
                        updatedCity = updatedCity.copy(
                            buildings = updatedCity.buildings + finishedBld,
                            buildingInProgress = null,
                            buildingTurnsRemaining = 0
                        )
                    } else {
                        updatedCity = updatedCity.copy(buildingTurnsRemaining = remaining)
                    }
                } else {
                    // Start new construction if slot available (e.g., 20% chance per turn)
                    if (Random.nextInt(100) < 30) {
                        val desirableBuilding = selectDesirableBuilding(faction.id, updatedCity)
                        if (desirableBuilding != null && !updatedCity.hasBuilding(desirableBuilding)) {
                            val bld = Building.getById(desirableBuilding)
                            updatedCity = updatedCity.copy(
                                buildingInProgress = desirableBuilding,
                                buildingTurnsRemaining = bld.turnsToBuild
                            )
                        }
                    }
                }

                // 2. Bot Military Recruitment Logic
                val currentTotalGarrison = updatedCity.garrison.values.sum()
                if (currentTotalGarrison < 6 && Random.nextInt(100) < 40) {
                    val unitToRecruit = selectDesirableUnit(faction.id, updatedCity)
                    val newGarrison = updatedCity.garrison.toMutableMap()
                    newGarrison[unitToRecruit] = (newGarrison[unitToRecruit] ?: 0) + 1
                    updatedCity = updatedCity.copy(garrison = newGarrison)
                }

                updatedCity
            }

            // Update cities list with newly evolved faction cities
            cities = cities.map { c -> updatedFactionCities.find { it.id == c.id } ?: c }

            // 3. Bot Strategic Military & Raid Decision
            val totalBotPower = updatedFactionCities.sumOf { it.totalMilitaryPower }
            val relToPlayer = relations[faction.id] ?: FactionRelation(faction.id)

            // Check if Bot launches military raid or siege on player city
            val playerCities = cities.filter { it.factionId == currentGameState.playerFactionId }
            if (playerCities.isNotEmpty() && (relToPlayer.status == DiplomaticStatus.WAR || relToPlayer.status == DiplomaticStatus.HOSTILE)) {
                // Hostile/war bots with army power > 50 have 25% chance to strike player
                if (totalBotPower >= 45 && Random.nextInt(100) < 25 && pendingPlayerSiege == null) {
                    val targetPlayerCity = playerCities.minByOrNull { it.defenseRating }!!
                    val attackingCity = updatedFactionCities.maxByOrNull { it.totalMilitaryPower }!!

                    val attackingArmyStrength = (attackingCity.totalMilitaryPower * 0.75).toInt().coerceAtLeast(30)
                    val attackingGarrison = attackingCity.garrison.filter { it.value > 0 }

                    pendingPlayerSiege = DefensiveSiegeEngagement(
                        attackingFactionId = faction.id,
                        targetCityId = targetPlayerCity.id,
                        attackerTroopDescription = "${faction.ruler} веде полки ${faction.name} (сила: $attackingArmyStrength) на штурм ${targetPlayerCity.name}!",
                        attackerStrength = attackingArmyStrength,
                        attackerGarrison = attackingGarrison
                    )
                }
            }

            // 4. Bot vs Bot Rivalry Interactions (Simulated geopolitics)
            if (Random.nextInt(100) < 15) {
                val otherBotFactions = nonPlayerFactions.filter { it.id != faction.id }
                if (otherBotFactions.isNotEmpty()) {
                    val rivalFaction = otherBotFactions.random()
                    val rivalCities = cities.filter { it.factionId == rivalFaction.id }
                    if (rivalCities.isNotEmpty()) {
                        chronicle.add("Війна сусідів: Війська ${faction.name} провели сутичку біля каналів з загонами ${rivalFaction.name}!")
                    }
                }
            }
        }

        // 5. Desert Nomad Raids (Martu Nomads)
        if (Random.nextInt(100) < 18 && pendingPlayerSiege == null) {
            val playerBorderCity = currentGameState.playerCities.minByOrNull { it.defenseRating }
            if (playerBorderCity != null && playerBorderCity.defenseRating < 55) {
                pendingPlayerSiege = DefensiveSiegeEngagement(
                    attackingFactionId = "martu",
                    targetCityId = playerBorderCity.id,
                    attackerTroopDescription = "Швидкі кочівники Марту на онаграх вийшли з Сирійської пустелі для набігу на житниці ${playerBorderCity.name}!",
                    attackerStrength = 35 + Random.nextInt(20),
                    attackerGarrison = mapOf("chariot" to 2, "archers" to 2)
                )
            }
        }

        return BotTurnResult(
            updatedCities = cities,
            updatedRelations = relations,
            chronicleEntries = chronicle,
            triggeredPlayerSiege = pendingPlayerSiege
        )
    }

    private fun selectDesirableBuilding(factionId: String, city: City): String? {
        val candidates = when (factionId) {
            "lagash" -> listOf("bronze_foundry", "walls", "canals", "granary")
            "ur" -> listOf("harbor", "granary", "ziggurat", "canals")
            "kish" -> listOf("edubba", "walls", "granary", "canals")
            "nippur" -> listOf("ziggurat", "temple_inanna", "edubba", "canals")
            "umma" -> listOf("canals", "walls", "bronze_foundry", "granary")
            "elam" -> listOf("bronze_foundry", "walls", "granary")
            else -> listOf("canals", "walls", "granary")
        }
        return candidates.firstOrNull { !city.hasBuilding(it) }
    }

    private fun selectDesirableUnit(factionId: String, city: City): String {
        return when (factionId) {
            "lagash" -> if (Random.nextBoolean()) "phalanx" else "chariot"
            "ur" -> if (Random.nextBoolean()) "spearmen" else "archers"
            "kish" -> if (Random.nextBoolean()) "chariot" else "spearmen"
            "nippur" -> if (Random.nextBoolean()) "priest" else "spearmen"
            "umma" -> if (Random.nextBoolean()) "archers" else "spearmen"
            "elam" -> if (Random.nextBoolean()) "archers" else "phalanx"
            "martu" -> "chariot"
            else -> "spearmen"
        }
    }
}

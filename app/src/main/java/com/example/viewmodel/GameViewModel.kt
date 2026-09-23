package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.MesopotamianLyrePlayer
import com.example.data.GameRepository
import com.example.model.Building
import com.example.model.City
import com.example.model.DiplomaticStatus
import com.example.model.Faction
import com.example.model.FactionRelation
import com.example.model.GameEvent
import com.example.model.GameState
import com.example.model.PlayerResources
import com.example.model.ResourceCost
import com.example.model.Technology
import com.example.model.UnitType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GameRepository(application)
    val lyrePlayer = MesopotamianLyrePlayer()

    private val _gameState = MutableStateFlow(GameState.createInitial("uruk"))
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _selectedCityId = MutableStateFlow<String?>("uruk")
    val selectedCityId: StateFlow<String?> = _selectedCityId.asStateFlow()

    private val _isMusicMuted = MutableStateFlow(false)
    val isMusicMuted: StateFlow<Boolean> = _isMusicMuted.asStateFlow()

    private val _hasSavedGame = MutableStateFlow(repository.hasSavedGame())
    val hasSavedGame: StateFlow<Boolean> = _hasSavedGame.asStateFlow()

    private val _savedGameSummary = MutableStateFlow(repository.getSavedGameSummary())
    val savedGameSummary: StateFlow<com.example.data.SavedGameSummary?> = _savedGameSummary.asStateFlow()

    private val _battleResultDialog = MutableStateFlow<BattleResult?>(null)
    val battleResultDialog: StateFlow<BattleResult?> = _battleResultDialog.asStateFlow()

    init {
        lyrePlayer.start()
    }

    override fun onCleared() {
        super.onCleared()
        lyrePlayer.stop()
    }

    fun toggleMusic() {
        val newMuted = !_isMusicMuted.value
        _isMusicMuted.value = newMuted
        lyrePlayer.isMuted = newMuted
    }

    fun startNewCampaign(factionId: String) {
        val initial = GameState.createInitial(factionId)
        _gameState.value = initial
        _selectedCityId.value = initial.playerCities.firstOrNull()?.id ?: factionId
        repository.saveGame(initial)
        _hasSavedGame.value = true
        _savedGameSummary.value = repository.getSavedGameSummary()
    }

    fun loadSavedCampaign(): Boolean {
        val saved = repository.loadGame()
        return if (saved != null) {
            _gameState.value = saved
            _selectedCityId.value = saved.playerCities.firstOrNull()?.id ?: saved.cities.first().id
            true
        } else {
            false
        }
    }

    fun selectCity(cityId: String) {
        _selectedCityId.value = cityId
    }

    fun startBuildingConstruction(cityId: String, buildingId: String): Boolean {
        val current = _gameState.value
        val city = current.cities.find { it.id == cityId } ?: return false
        if (city.factionId != current.playerFactionId) return false
        if (city.hasBuilding(buildingId)) return false
        if (city.buildingInProgress != null) return false

        val building = Building.getById(buildingId)
        if (!current.resources.canAfford(building.cost)) return false

        val updatedResources = current.resources.subtract(building.cost)
        val updatedCities = current.cities.map {
            if (it.id == cityId) {
                it.copy(
                    buildingInProgress = buildingId,
                    buildingTurnsRemaining = building.turnsToBuild
                )
            } else it
        }

        val log = current.chronicleLog + "У місті ${city.name} закладено фундамент споруди «${building.name}»."
        val nextState = current.copy(
            resources = updatedResources,
            cities = updatedCities,
            chronicleLog = log
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun recruitUnit(cityId: String, unitId: String): Boolean {
        val current = _gameState.value
        val city = current.cities.find { it.id == cityId } ?: return false
        if (city.factionId != current.playerFactionId) return false

        val unit = UnitType.getById(unitId)
        if (!current.resources.canAfford(unit.cost)) return false

        // Check if tech unlocked
        if (unit.id == "chariot" && !current.researchedTechIds.contains("wheel_cart")) return false
        if (unit.id == "phalanx" && !current.researchedTechIds.contains("phalanx_formation")) return false

        val updatedResources = current.resources.subtract(unit.cost)
        val currentGarrison = city.garrison.toMutableMap()
        currentGarrison[unitId] = (currentGarrison[unitId] ?: 0) + 1

        val updatedCities = current.cities.map {
            if (it.id == cityId) it.copy(garrison = currentGarrison) else it
        }

        val log = current.chronicleLog + "До гарнізону міста ${city.name} приєднався загін «${unit.name}»."
        val nextState = current.copy(
            resources = updatedResources,
            cities = updatedCities,
            chronicleLog = log
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun startResearch(techId: String): Boolean {
        val current = _gameState.value
        if (current.researchedTechIds.contains(techId)) return false
        val tech = Technology.getById(techId)
        // Check prerequisites
        if (!current.researchedTechIds.containsAll(tech.prerequisites)) return false

        val nextState = current.copy(
            currentTechId = techId,
            currentTechTurnsRemaining = tech.turnsRequired,
            chronicleLog = current.chronicleLog + "Жерці та писарі розпочали дослідження технології «${tech.name}»."
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun sendDiplomaticGift(targetFactionId: String): Boolean {
        val current = _gameState.value
        val cost = ResourceCost(silver = 40)
        if (!current.resources.canAfford(cost)) return false

        val rel = current.relations[targetFactionId] ?: FactionRelation(targetFactionId)
        val newScore = (rel.relationshipScore + 30).coerceAtMost(100)
        val updatedRelations = current.relations.toMutableMap()
        updatedRelations[targetFactionId] = rel.copy(relationshipScore = newScore)

        val targetFaction = Faction.getById(targetFactionId)
        val nextState = current.copy(
            resources = current.resources.subtract(cost),
            relations = updatedRelations,
            chronicleLog = current.chronicleLog + "Царські дари відправлено правителю міста ${targetFaction.name}. Стосунки покращилися!"
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun proposePeace(targetFactionId: String): Boolean {
        val current = _gameState.value
        val rel = current.relations[targetFactionId] ?: return false
        val cost = ResourceCost(silver = 30, grain = 30)
        if (!current.resources.canAfford(cost)) return false

        val updatedRelations = current.relations.toMutableMap()
        updatedRelations[targetFactionId] = rel.copy(
            status = DiplomaticStatus.PEACE,
            relationshipScore = (rel.relationshipScore + 30).coerceAtMost(100)
        )

        val targetFaction = Faction.getById(targetFactionId)
        val nextState = current.copy(
            resources = current.resources.subtract(cost),
            relations = updatedRelations,
            chronicleLog = current.chronicleLog + "Укладено священний мир із містом ${targetFaction.name}! Війну припинено."
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun proposeTrade(targetFactionId: String): Boolean {
        val current = _gameState.value
        val rel = current.relations[targetFactionId] ?: return false
        if (rel.status == DiplomaticStatus.WAR) return false

        val updatedRelations = current.relations.toMutableMap()
        updatedRelations[targetFactionId] = rel.copy(
            status = DiplomaticStatus.TRADE_PACT,
            relationshipScore = (rel.relationshipScore + 15).coerceAtMost(100)
        )

        val targetFaction = Faction.getById(targetFactionId)
        val nextState = current.copy(
            relations = updatedRelations,
            chronicleLog = current.chronicleLog + "Укладено вигідний торговий договір із містом ${targetFaction.name}."
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
        return true
    }

    fun demandTribute(targetFactionId: String): Boolean {
        val current = _gameState.value
        val rel = current.relations[targetFactionId] ?: return false
        val ourPower = current.totalMilitaryPower
        val targetFaction = Faction.getById(targetFactionId)

        val updatedRelations = current.relations.toMutableMap()
        if (ourPower > 80) {
            // Success
            val tributeSilver = 60
            val tributeGrain = 50
            updatedRelations[targetFactionId] = rel.copy(
                tributePaidToUs = rel.tributePaidToUs + tributeSilver,
                relationshipScore = (rel.relationshipScore - 15).coerceAtLeast(-100)
            )
            val nextState = current.copy(
                resources = current.resources.add(PlayerResources(silver = tributeSilver, grain = tributeGrain)),
                relations = updatedRelations,
                chronicleLog = current.chronicleLog + "Місто ${targetFaction.name} підкорилося нашій силі і сплатило данину!"
            )
            _gameState.value = nextState
            repository.saveGame(nextState)
            return true
        } else {
            // Refusal & anger
            updatedRelations[targetFactionId] = rel.copy(
                status = DiplomaticStatus.HOSTILE,
                relationshipScore = (rel.relationshipScore - 30).coerceAtLeast(-100)
            )
            val nextState = current.copy(
                relations = updatedRelations,
                chronicleLog = current.chronicleLog + "Правитель міста ${targetFaction.name} гордо відкинув нашу вимогу данини!"
            )
            _gameState.value = nextState
            repository.saveGame(nextState)
            return false
        }
    }

    fun declareWar(targetFactionId: String) {
        val current = _gameState.value
        val rel = current.relations[targetFactionId] ?: FactionRelation(targetFactionId)
        val updatedRelations = current.relations.toMutableMap()
        updatedRelations[targetFactionId] = rel.copy(
            status = DiplomaticStatus.WAR,
            relationshipScore = -80
        )
        val targetFaction = Faction.getById(targetFactionId)
        val nextState = current.copy(
            relations = updatedRelations,
            chronicleLog = current.chronicleLog + "Оголошено священну війну місту ${targetFaction.name}! Наші полки готуються до походів."
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
    }

    fun launchMilitaryCampaign(targetCityId: String) {
        val current = _gameState.value
        val targetCity = current.cities.find { it.id == targetCityId } ?: return
        if (targetCity.factionId == current.playerFactionId) return

        // Attacking army calculated from player's nearest/strongest city
        val attackerCity = current.playerCities.maxByOrNull { it.totalMilitaryPower } ?: return
        val attackerPower = attackerCity.totalMilitaryPower
        val defenderPower = targetCity.defenseRating

        // Battle calculation with dice variance
        val roll = Random.nextInt(85, 120)
        val finalAttackerScore = (attackerPower * (roll / 100.0)).toInt()
        val finalDefenderScore = defenderPower

        val isVictorious = finalAttackerScore >= finalDefenderScore

        val updatedCities = current.cities.map { city ->
            if (city.id == targetCityId && isVictorious) {
                // City conquered!
                city.copy(
                    factionId = current.playerFactionId,
                    loyalty = 60,
                    garrison = mapOf("spearmen" to 2)
                )
            } else if (city.id == attackerCity.id) {
                // Casualties
                val updatedGarrison = city.garrison.mapValues { (_, count) ->
                    (count - 1).coerceAtLeast(1)
                }
                city.copy(garrison = updatedGarrison)
            } else city
        }

        val lootSilver = if (isVictorious) 120 else 0
        val lootGrain = if (isVictorious) 80 else 0
        val updatedResources = if (isVictorious) {
            current.resources.add(PlayerResources(silver = lootSilver, grain = lootGrain, loyalty = 10))
        } else {
            current.resources.copy(loyalty = (current.resources.loyalty - 10).coerceAtLeast(10))
        }

        val battleTitle = if (isVictorious) "Тріумф під мурами ${targetCity.name}!" else "Відступ від стін ${targetCity.name}"
        val battleDetails = if (isVictorious) {
            "Ваші шумерські фаланги та лучники прорвали ворожі укріплення! Місто ${targetCity.name} визнало владу нашого правителя. Захоплено трофеї: +$lootSilver срібла та +$lootGrain зерна."
        } else {
            "Ворожі мури виявилися занадто міцними. Наші воїни завдали втрат супернику, але змушені були відступити до ${attackerCity.name} для перегрупування."
        }

        _battleResultDialog.value = BattleResult(
            title = battleTitle,
            isVictory = isVictorious,
            details = battleDetails,
            conqueredCityName = if (isVictorious) targetCity.name else null
        )

        // Check if victory condition met (controlling at least 5 cities)
        val controlledCities = updatedCities.count { it.factionId == current.playerFactionId }
        val isCampaignWon = controlledCities >= 5

        val nextState = current.copy(
            cities = updatedCities,
            resources = updatedResources,
            isVictory = isCampaignWon,
            chronicleLog = current.chronicleLog + "$battleTitle. $battleDetails"
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
    }

    fun dismissBattleResult() {
        _battleResultDialog.value = null
    }

    fun endTurn() {
        val current = _gameState.value

        // 1. Calculate production from all player cities & buildings
        var deltaGrain = 30 // Base fields
        var deltaClay = 25  // River silt
        var deltaBronze = 10
        var deltaSilver = 35 // Taxes
        var deltaPiety = 5
        var additionalStorage = 0

        current.playerCities.forEach { city ->
            city.buildings.forEach { bldId ->
                val b = Building.getById(bldId)
                deltaGrain += b.incomeGrain
                deltaClay += b.incomeClay
                deltaBronze += b.incomeBronze
                deltaSilver += b.incomeSilver
                deltaPiety += b.pietyBonus
                additionalStorage += b.maxGrainStorageBonus
            }
            // City Unique Starter Traits
            deltaGrain += city.bonusGrain
            deltaClay += city.bonusClay
            deltaBronze += city.bonusBronze
            deltaSilver += city.bonusSilver
        }

        // Faction bonus calculations
        if (current.playerFactionId == "ur") {
            deltaSilver = (deltaSilver * 1.25).toInt()
        } else if (current.playerFactionId == "lagash") {
            deltaBronze = (deltaBronze * 1.20).toInt()
        }

        // Tech bonuses
        if (current.researchedTechIds.contains("seed_plow")) {
            deltaGrain = (deltaGrain * 1.35).toInt()
        }
        if (current.researchedTechIds.contains("pottery_wheel")) {
            deltaClay = (deltaClay * 1.20).toInt()
        }
        if (current.researchedTechIds.contains("cuneiform")) {
            deltaSilver = (deltaSilver * 1.15).toInt()
        }

        // Diplomatic Trade Pacts (+20 silver per treaty)
        current.relations.values.filter { it.status == DiplomaticStatus.TRADE_PACT }.forEach {
            deltaSilver += 20
        }

        // Upkeep consumption: armies consume grain
        val totalTroops = current.playerCities.sumOf { it.garrison.values.sum() }
        val armyFoodUpkeep = totalTroops * 5
        deltaGrain -= armyFoodUpkeep

        // 2. Advance building construction
        val updatedCities = current.cities.map { city ->
            if (city.buildingInProgress != null) {
                val remaining = city.buildingTurnsRemaining - 1
                if (remaining <= 0) {
                    // Finished!
                    val finishedBld = city.buildingInProgress
                    city.copy(
                        buildings = city.buildings + finishedBld,
                        buildingInProgress = null,
                        buildingTurnsRemaining = 0
                    )
                } else {
                    city.copy(buildingTurnsRemaining = remaining)
                }
            } else city
        }

        // 3. Advance Tech research
        var updatedResearchedTechs = current.researchedTechIds
        var updatedCurrentTech = current.currentTechId
        var updatedTechTurns = current.currentTechTurnsRemaining
        if (updatedCurrentTech != null) {
            updatedTechTurns -= 1
            if (updatedTechTurns <= 0) {
                updatedResearchedTechs = updatedResearchedTechs + updatedCurrentTech
                val completedTech = Technology.getById(updatedCurrentTech)
                updatedCurrentTech = null
                updatedTechTurns = 0
            }
        }

        // 4. Resources
        val maxCap = 400 + additionalStorage
        val newGrain = (current.resources.grain + deltaGrain).coerceIn(0, maxCap)
        val newClay = (current.resources.clay + deltaClay).coerceAtLeast(0)
        val newBronze = (current.resources.bronze + deltaBronze).coerceAtLeast(0)
        val newSilver = (current.resources.silver + deltaSilver).coerceAtLeast(0)
        val newLoyalty = if (newGrain <= 10) (current.resources.loyalty - 15).coerceAtLeast(10) else current.resources.loyalty

        val updatedResources = current.resources.copy(
            grain = newGrain,
            clay = newClay,
            bronze = newBronze,
            silver = newSilver,
            loyalty = newLoyalty,
            piety = (current.resources.piety + deltaPiety).coerceIn(0, 100),
            maxGrainStorage = maxCap
        )

        // 5. Check for historical events (every 2-3 turns)
        val nextEvent = if (current.turn % 2 == 0 && Random.nextBoolean()) {
            GameEvent.ALL_EVENTS.random()
        } else null

        val nextTurn = current.turn + 1
        val nextYearBCE = current.yearBCE - 5

        // Check if 50-turn campaign limit is reached or player defeated
        val playerCitiesLeft = updatedCities.count { it.factionId == current.playerFactionId }
        val isTimeExpired = nextTurn > current.maxTurns
        val isTotalDefeat = playerCitiesLeft == 0
        val isCampaignVictorious = playerCitiesLeft >= 4

        val isGameOver = isTimeExpired || isTotalDefeat || playerCitiesLeft == updatedCities.size
        val gameOverReason = when {
            isTotalDefeat -> "Поразка! Ворожі сили захопили всі ваші володіння у Межиріччі."
            playerCitiesLeft == updatedCities.size -> "Абсолютна перемога! Усі міста Месопотамії від Перської затоки до гір Загросу об'єднані під вашим скіпетром!"
            isTimeExpired && isCampaignVictorious -> "Тріумф 50 ходів! Ви об'єднали $playerCitiesLeft з ${updatedCities.size} міст Шумеру та створили наймогутнішу державу Бронзової доби!"
            isTimeExpired && playerCitiesLeft in 2..3 -> "Регіональна гегемонія! За 50 ходів правління ваше царство зберегло міцні позиції серед великих міст Межиріччя."
            isTimeExpired -> "50 ходів вичерпано. Ваше місто вистояло перед викликами часу, проте об'єднати Месопотамію не вдалося."
            else -> null
        }

        val logEntry = "$nextYearBCE р. до н.е. (Хід $nextTurn/${current.maxTurns}): Завершено сезонні роботи. Урожай: +$deltaGrain зерна, витрати війська: -$armyFoodUpkeep зерна."

        val nextState = current.copy(
            turn = nextTurn,
            yearBCE = nextYearBCE,
            resources = updatedResources,
            cities = updatedCities,
            researchedTechIds = updatedResearchedTechs,
            currentTechId = updatedCurrentTech,
            currentTechTurnsRemaining = updatedTechTurns,
            activeEvent = if (!isGameOver) nextEvent else null,
            isVictory = isCampaignVictorious,
            isDefeat = isTotalDefeat,
            isGameOver = isGameOver,
            gameOverReason = gameOverReason,
            chronicleLog = current.chronicleLog + logEntry
        )

        _gameState.value = nextState
        repository.saveGame(nextState)
    }

    fun restartCampaign() {
        val factionId = _gameState.value.playerFactionId
        startNewCampaign(factionId)
    }

    fun resolveEventOption(optionIndex: Int) {
        val current = _gameState.value
        val event = current.activeEvent ?: return
        val option = event.options.getOrNull(optionIndex) ?: return

        val updatedResources = current.resources.copy(
            grain = (current.resources.grain + option.grainChange).coerceIn(0, current.resources.maxGrainStorage),
            clay = (current.resources.clay + option.clayChange).coerceAtLeast(0),
            bronze = (current.resources.bronze + option.bronzeChange).coerceAtLeast(0),
            silver = (current.resources.silver + option.silverChange).coerceAtLeast(0),
            loyalty = (current.resources.loyalty + option.loyaltyChange).coerceIn(0, 100),
            piety = (current.resources.piety + option.pietyChange).coerceIn(0, 100)
        )

        val nextState = current.copy(
            resources = updatedResources,
            activeEvent = null,
            chronicleLog = current.chronicleLog + "${event.title}: ${option.outcomeMessage}"
        )
        _gameState.value = nextState
        repository.saveGame(nextState)
    }

    fun dismissEvent() {
        val current = _gameState.value
        _gameState.value = current.copy(activeEvent = null)
    }
}

data class BattleResult(
    val title: String,
    val isVictory: Boolean,
    val details: String,
    val conqueredCityName: String? = null
)

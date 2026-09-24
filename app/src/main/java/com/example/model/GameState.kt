package com.example.model

data class GameState(
    val turn: Int = 1,
    val maxTurns: Int = 50,
    val yearBCE: Int = 2600,
    val playerFactionId: String = "uruk",
    val resources: PlayerResources = PlayerResources(),
    val cities: List<City> = City.INITIAL_CITIES,
    val relations: Map<String, FactionRelation> = defaultRelations(),
    val tradeRoutes: List<TradeRoute> = defaultTradeRoutes(),
    val researchedTechIds: Set<String> = setOf("cuneiform"),
    val currentTechId: String? = null,
    val currentTechTurnsRemaining: Int = 0,
    val activeEvent: GameEvent? = null,
    val chronicleLog: List<String> = listOf("2600 р. до н.е.: Правитель зійшов на престол міста. Розпочалася епоха перших держав Межиріччя!"),
    val isVictory: Boolean = false,
    val isDefeat: Boolean = false,
    val isGameOver: Boolean = false,
    val gameOverReason: String? = null,
    val lastTurnLogs: List<TurnLogItem> = emptyList(),
    val showTurnLogOverlay: Boolean = false,
    val wonders: List<MegaProject> = MegaProject.ALL_WONDERS,
    val showFactionsOverview: Boolean = false,
    val showMegaProjects: Boolean = false,
    val lastHarvestDeltas: List<Int>? = null,
    val botCampaignSourceCityId: String? = null,
    val botCampaignTargetCityId: String? = null,
    val activeQuizQuestion: HistoryQuizQuestion? = null,
    val answeredQuizzesCount: Int = 0,
    val activeLaws: Set<String> = emptySet(),
    val activeBattle: ActiveTacticalBattle? = null,
    val showLawsDialog: Boolean = false
) {
    val playerFaction: Faction
        get() = Faction.getById(playerFactionId)

    val playerCities: List<City>
        get() = cities.filter { it.factionId == playerFactionId }

    val turnsRemaining: Int
        get() = (maxTurns - turn).coerceAtLeast(0)

    val isTimeExpired: Boolean
        get() = turn >= maxTurns

    val seasonName: String
        get() = when (turn % 4) {
            1 -> "Повінь (Нісанну / Весна)"
            2 -> "Жнива ячменю (Ташріту / Літо)"
            3 -> "Оранка та посів (Арахсамну / Осінь)"
            else -> "Свято Акіту (Зима)"
        }

    val totalControlledPopulation: Int
        get() = playerCities.sumOf { it.population }

    val totalMilitaryPower: Int
        get() {
            var pwr = playerCities.sumOf { it.totalMilitaryPower }
            if ("lex_talionis_eye_for_eye" in activeLaws) pwr = (pwr * 1.10).toInt()
            if ("royal_bronze_monopoly" in activeLaws) pwr = (pwr * 1.20).toInt()
            return pwr
        }

    // Net income calculations per turn
    val netIncomeGrain: Int
        get() {
            var inc = 30
            playerCities.forEach { city ->
                city.buildings.forEach { bldId ->
                    inc += Building.getById(bldId).incomeGrain
                }
                inc += city.bonusGrain
            }
            if (researchedTechIds.contains("seed_plow")) inc = (inc * 1.35).toInt()
            if ("ilku_corvee_canals" in activeLaws) inc += 40
            if ("temple_sacred_tithe" in activeLaws) inc -= 25
            val totalTroops = playerCities.sumOf { it.garrison.values.sum() }
            val foodUpkeep = totalTroops * 4
            return inc - foodUpkeep
        }

    val netIncomeClay: Int
        get() {
            var inc = 25
            playerCities.forEach { city ->
                city.buildings.forEach { bldId ->
                    inc += Building.getById(bldId).incomeClay
                }
                inc += city.bonusClay
            }
            if (researchedTechIds.contains("pottery_wheel")) inc = (inc * 1.20).toInt()
            if ("ilku_corvee_canals" in activeLaws) inc += 20
            if ("standard_shekel_mina" in activeLaws) inc -= 15
            return inc
        }

    val netIncomeBronze: Int
        get() {
            var inc = 10
            playerCities.forEach { city ->
                city.buildings.forEach { bldId ->
                    inc += Building.getById(bldId).incomeBronze
                }
                inc += city.bonusBronze
            }
            if (playerFactionId == "lagash") inc = (inc * 1.20).toInt()
            if ("royal_bronze_monopoly" in activeLaws) inc += 25
            return inc
        }

    val netIncomeSilver: Int
        get() {
            var inc = 35
            playerCities.forEach { city ->
                city.buildings.forEach { bldId ->
                    inc += Building.getById(bldId).incomeSilver
                }
                inc += city.bonusSilver
            }
            if (playerFactionId == "ur") inc = (inc * 1.25).toInt()
            if (researchedTechIds.contains("cuneiform")) inc = (inc * 1.15).toInt()
            if ("misharum_debt_relief" in activeLaws) inc -= 15
            if ("standard_shekel_mina" in activeLaws) inc += 35
            if ("royal_bronze_monopoly" in activeLaws) inc -= 25
            if ("ilku_corvee_canals" in activeLaws) inc -= 10
            // Trade pact bonus
            relations.values.filter { it.status == DiplomaticStatus.TRADE_PACT }.forEach {
                inc += 20
            }
            return inc
        }

    companion object {
        fun defaultRelations(): Map<String, FactionRelation> {
            return mapOf(
                "ur" to FactionRelation("ur", DiplomaticStatus.NEUTRAL, relationshipScore = 15),
                "uruk" to FactionRelation("uruk", DiplomaticStatus.PEACE, relationshipScore = 20),
                "lagash" to FactionRelation("lagash", DiplomaticStatus.NEUTRAL, relationshipScore = 0),
                "kish" to FactionRelation("kish", DiplomaticStatus.NEUTRAL, relationshipScore = 10),
                "nippur" to FactionRelation("nippur", DiplomaticStatus.PEACE, relationshipScore = 40),
                "umma" to FactionRelation("umma", DiplomaticStatus.HOSTILE, relationshipScore = -30),
                "martu" to FactionRelation("martu", DiplomaticStatus.HOSTILE, relationshipScore = -40),
                "elam" to FactionRelation("elam", DiplomaticStatus.NEUTRAL, relationshipScore = 5)
            )
        }

        fun defaultTradeRoutes(): List<TradeRoute> {
            return listOf(
                TradeRoute(
                    id = "route_ur_dilmun",
                    originCityId = "ur",
                    destinationCityId = "uruk",
                    exportedResource = "Зерно ячменю",
                    importedResource = "Срібло та мідь",
                    turnsActive = 3,
                    profitPerTurn = 25
                )
            )
        }

        fun createInitial(factionId: String): GameState {
            val faction = Faction.getById(factionId)
            // Adjust starting cities so player owns their faction city
            val cities = City.INITIAL_CITIES.map { city ->
                if (city.id == factionId) {
                    city.copy(factionId = factionId)
                } else {
                    city
                }
            }

            val bonusResources = when (factionId) {
                "ur" -> PlayerResources(grain = 220, clay = 140, bronze = 40, silver = 320, loyalty = 90)
                "lagash" -> PlayerResources(grain = 180, clay = 140, bronze = 70, silver = 190, loyalty = 88)
                "uruk" -> PlayerResources(grain = 200, clay = 180, bronze = 45, silver = 230, loyalty = 92)
                "kish" -> PlayerResources(grain = 190, clay = 150, bronze = 50, silver = 260, loyalty = 85)
                "nippur" -> PlayerResources(grain = 170, clay = 160, bronze = 35, silver = 210, loyalty = 98, piety = 80)
                else -> PlayerResources()
            }

            return GameState(
                turn = 1,
                yearBCE = 2600,
                playerFactionId = factionId,
                resources = bonusResources,
                cities = cities,
                chronicleLog = listOf("2600 р. до н.е.: Правитель ${faction.ruler} очолив місто ${faction.name}! Народ молиться ${faction.patronDeity}.")
            )
        }
    }
}

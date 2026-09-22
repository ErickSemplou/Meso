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
    val pendingQuiz: QuizQuestion? = null,
    val completedQuizIds: Set<String> = emptySet(),
    val correctQuizCount: Int = 0,
    val chronicleLog: List<String> = listOf("2600 р. до н.е.: Правитель зійшов на престол міста. Розпочалася епоха перших держав Межиріччя!"),
    val isVictory: Boolean = false,
    val isDefeat: Boolean = false,
    val isGameOver: Boolean = false,
    val gameOverReason: String? = null
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
        get() = playerCities.sumOf { it.totalMilitaryPower }

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

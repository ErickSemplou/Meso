package com.example.engine

import com.example.model.Building
import com.example.model.City
import com.example.model.Faction
import com.example.model.GameState
import com.example.model.LogCategory
import com.example.model.MegaProject
import com.example.model.Technology
import com.example.model.TurnLogItem
import kotlin.random.Random

data class BotTurnResult(
    val updatedCities: List<City>,
    val updatedWonders: List<MegaProject>,
    val logs: List<TurnLogItem>,
    val campaignSourceCityId: String? = null,
    val campaignTargetCityId: String? = null
)

object TurnLogEngine {

    private val HISTORICAL_FACTS = listOf(
        "З зрошувальними каналами Месопотамія стала колискою землеробства. Завдяки шлюзам на Тигрі й Євфраті шумери збирали по два врожаї ячменю на рік.",
        "Перші у світі школи називалися 'Едубба' ('Будинок табличок'). Учні з дитинства вчилися ліпити глиняні таблички та виводити на них клинописні значки.",
        "'Стела шумерських шулік' із Лагаша — найдавніший у світі зображувальний документ, що описує фалангу воїнів із щитами та списами.",
        "Торгівля у Бронзову добу була бартерною, але еквівалентом вартості слугувало срібло (у шекелях) та міри ячменю ('гур').",
        "Зикурати — це багатоярусні культові споруди. На верхньому майданчику знаходилося 'святилище бога', куди мали доступ лише вищі жерці.",
        "Месопотамські астрономи першими розділили коло на 360 градусів, а годину — на 60 хвилин, використовуючи шістдесяткову систему числення.",
        "Правителі міст-держав мали титул 'Ен' (верховний жрець), 'Енсі' (правитель-будівничий) або 'Лугаль' (великий цар-воєначальник).",
        "Покриття мурів Урука обпаленою цеглою робило їх настільки міцними, що про них складали легенди в 'Епосі про Гільгамеша'.",
        "Мідь та олово для виплавки бронзи доставлялися караванами з гір Загросу та Анатолії, адже на рівнинах Межиріччя не було металевих руд.",
        "Вози та бойові колісниці Шумеру мали суцільні дерев'яні колеса, скріплені мідними цвяхами, і тягнулися вонаграми — степовими віслюками.",
        "Канал Гу'едінна між Лагашем та Уммою вважається першим відомим прикордонним конфліктом у світовій історії.",
        "Шумерський плуг із сівалкою дозволяв землеробам одночасно орати землю та висівати зерна ячменю в борозни.",
        "Ур-Намму склав найдавніший відомий збірник законів за 300 років до Кодексу Хаммурапі.",
        "Глиняні печатки із циліндричним різьбленням використовувалися як особистий підпис правителів та купців."
    )

    fun processTurn(
        state: GameState,
        deltaGrain: Int,
        deltaClay: Int,
        deltaBronze: Int,
        deltaSilver: Int,
        armyFoodUpkeep: Int,
        completedBuildingCities: List<Pair<City, String>>,
        completedTechId: String?
    ): BotTurnResult {
        val logs = mutableListOf<TurnLogItem>()
        val nextTurn = state.turn + 1
        val nextYearBCE = state.yearBCE - 5
        var campaignSourceId: String? = null
        var campaignTargetId: String? = null

        // 1. Harvest & Economic Log
        logs.add(
            TurnLogItem(
                turn = nextTurn,
                yearBCE = nextYearBCE,
                category = LogCategory.ECONOMIC,
                title = "Сезонний врожай та прибутки",
                description = "Поля Месопотамії дають плоди: +$deltaGrain зерна, +$deltaClay глини, +$deltaBronze бронзи, +$deltaSilver срібла. Військовий гарнізон спожив -$armyFoodUpkeep зерна.",
                highlightColorHex = 0xFF2E7D32
            )
        )

        if (state.resources.grain < 30) {
            logs.add(
                TurnLogItem(
                    turn = nextTurn,
                    yearBCE = nextYearBCE,
                    category = LogCategory.ECONOMIC,
                    title = "⚠️ Недостача зерна!",
                    description = "Запаси ячменю критично низькі. Зведіть зрошувальний канал або млин, щоб уникнути бунту.",
                    highlightColorHex = 0xFFC62828
                )
            )
        }

        // 2. Player Progress Logs
        completedBuildingCities.forEach { (city, buildingId) ->
            val bld = Building.getById(buildingId)
            logs.add(
                TurnLogItem(
                    turn = nextTurn,
                    yearBCE = nextYearBCE,
                    category = LogCategory.DEVELOPMENT,
                    title = "🏛️ Збудовано: ${bld.name}",
                    description = "У місті ${city.name} завершено спорудження ${bld.name}. ${bld.description}",
                    highlightColorHex = 0xFF8D6E63
                )
            )
        }

        if (completedTechId != null) {
            val tech = Technology.getById(completedTechId)
            logs.add(
                TurnLogItem(
                    turn = nextTurn,
                    yearBCE = nextYearBCE,
                    category = LogCategory.DEVELOPMENT,
                    title = "📜 Дослідження: ${tech.name}",
                    description = "Писарі та майстри опанували нові знання: ${tech.description}",
                    highlightColorHex = 0xFF1565C0
                )
            )
        }

        // 3. AI Bot Cities Development Logic
        var mutableCities = state.cities.map { city -> city.copy() }.toMutableList()

        mutableCities = mutableCities.map { city ->
            if (city.factionId == state.playerFactionId) {
                city
            } else {
                val faction = Faction.getById(city.factionId)
                val totalTroops = city.garrison.values.sum()
                val availableBuildings = listOf("canals", "edubba", "granary", "bronze_foundry", "walls", "ziggurat")
                val unbuilt = availableBuildings.filter { bId -> !city.hasBuilding(bId) }

                when {
                    // Option A: Build a missing economic/defensive building
                    unbuilt.isNotEmpty() && Random.nextFloat() < 0.35f -> {
                        val bToBuild = unbuilt.random()
                        val bld = Building.getById(bToBuild)
                        logs.add(
                            TurnLogItem(
                                turn = nextTurn,
                                yearBCE = nextYearBCE,
                                category = LogCategory.BOT_ACTION,
                                title = "🏛️ ${faction.name}: Розбудова міста",
                                description = "У місті ${city.name} зведено '${bld.name}'. ${bld.description}",
                                factionId = faction.id,
                                highlightColorHex = 0xFF8D6E63
                            )
                        )
                        city.copy(buildings = city.buildings + bToBuild)
                    }

                    // Option B: Recruit new troops
                    totalTroops < 60 && Random.nextFloat() < 0.55f -> {
                        val unitToRecruit = when (city.factionId) {
                            "lagash" -> "phalanx"
                            "kish" -> "chariot"
                            "umma" -> "spearmen"
                            else -> listOf("spearmen", "archers").random()
                        }
                        val currentCount = city.garrison[unitToRecruit] ?: 0
                        val newGarrison = city.garrison.toMutableMap()
                        newGarrison[unitToRecruit] = currentCount + 12

                        logs.add(
                            TurnLogItem(
                                turn = nextTurn,
                                yearBCE = nextYearBCE,
                                category = LogCategory.BOT_ACTION,
                                title = "⚔️ ${faction.name}: Військовий вишкіл",
                                description = "Правитель ${faction.ruler} з міста ${city.name} поповнив гарнізон загоном $unitToRecruit.",
                                factionId = faction.id,
                                highlightColorHex = 0xFFE65100
                            )
                        )
                        city.copy(garrison = newGarrison)
                    }

                    // Option C: Researching science
                    Random.nextFloat() < 0.25f -> {
                        val techs = listOf("Бойову фалангу", "Плуг-сіялку", "Гончарне коло", "Бронзове лиття")
                        logs.add(
                            TurnLogItem(
                                turn = nextTurn,
                                yearBCE = nextYearBCE,
                                category = LogCategory.BOT_ACTION,
                                title = "📜 ${faction.name}: Дослідження науки",
                                description = "Мудреці та писарі міста ${city.name} вивчили '${techs.random()}'.",
                                factionId = faction.id,
                                highlightColorHex = 0xFF1565C0
                            )
                        )
                        city
                    }

                    else -> {
                        city
                    }
                }
            }
        }.toMutableList()

        // 4. Bot Military Expansion Campaign (NPC vs NPC or NPC vs Neutral)
        if (nextTurn % 3 == 0) {
            val nonPlayerCities = mutableCities.filter { it.factionId != state.playerFactionId }
            if (nonPlayerCities.size >= 2) {
                val attackerCity = nonPlayerCities.maxByOrNull { it.totalMilitaryPower }
                val targetCity = nonPlayerCities.filter { it.factionId != attackerCity?.factionId }.randomOrNull()

                if (attackerCity != null && targetCity != null && attackerCity.totalMilitaryPower > 35) {
                    val attackerFaction = Faction.getById(attackerCity.factionId)
                    val targetFaction = Faction.getById(targetCity.factionId)
                    val attackPower = attackerCity.totalMilitaryPower
                    val defensePower = targetCity.totalMilitaryPower + (if (targetCity.hasBuilding("walls")) 30 else 0)

                    campaignSourceId = attackerCity.id
                    campaignTargetId = targetCity.id

                    if (attackPower > defensePower) {
                        // Attacker captures target city!
                        val cityIndex = mutableCities.indexOfFirst { it.id == targetCity.id }
                        if (cityIndex != -1) {
                            mutableCities[cityIndex] = targetCity.copy(
                                factionId = attackerCity.factionId,
                                garrison = mapOf("spearmen" to 15, "archers" to 10)
                            )
                        }

                        logs.add(
                            TurnLogItem(
                                turn = nextTurn,
                                yearBCE = nextYearBCE,
                                category = LogCategory.MILITARY,
                                title = "⚔️ ВІЙСЬКОВИЙ ПОХІД: Захоплення міста!",
                                description = "Війська міста ${attackerCity.name} (${attackerFaction.name}) у ході битви підкорили місто ${targetCity.name} (${targetFaction.name})! Кордони Межиріччя перекраяно.",
                                factionId = attackerFaction.id,
                                highlightColorHex = 0xFFB71C1C
                            )
                        )
                    } else {
                        logs.add(
                            TurnLogItem(
                                turn = nextTurn,
                                yearBCE = nextYearBCE,
                                category = LogCategory.MILITARY,
                                title = "⚔️ ОБЛОГА МІСТА: Мури вистояли!",
                                description = "Війська ${attackerCity.name} штурмували ${targetCity.name}, проте міцні мури та стрільці захисників відбили напад.",
                                factionId = targetFaction.id,
                                highlightColorHex = 0xFF3E2723
                            )
                        )
                    }
                }
            }
        }

        // 5. Bot Wonders Progress
        val updatedWonders = state.wonders.map { wonder ->
            if (!wonder.isCompleted) {
                val builderFaction = wonder.builderFactionId
                val controlsCity = mutableCities.any { it.id == wonder.cityId && it.factionId == builderFaction }

                if (controlsCity && Random.nextFloat() < 0.60f) {
                    val addGrain = 30
                    val addClay = 40
                    val addSilver = 20

                    val newGrainSpent = wonder.grainSpent + addGrain
                    val newClaySpent = wonder.claySpent + addClay
                    val newSilverSpent = wonder.silverSpent + addSilver

                    val stageCompleted = newGrainSpent >= wonder.grainNeeded &&
                            newClaySpent >= wonder.clayNeeded &&
                            newSilverSpent >= wonder.silverNeeded

                    if (stageCompleted) {
                        val newStage = wonder.stage + 1
                        val isFullyDone = newStage > wonder.maxStages

                        if (isFullyDone) {
                            logs.add(
                                TurnLogItem(
                                    turn = nextTurn,
                                    yearBCE = nextYearBCE,
                                    category = LogCategory.DEVELOPMENT,
                                    title = "✨ ДИВО ЗБУДОВАНО: ${wonder.name}!",
                                    description = "Держава ${Faction.getById(builderFaction).name} завершила зведення ${wonder.name}! ${wonder.completedEffectDescription}",
                                    factionId = builderFaction,
                                    highlightColorHex = 0xFFD4AF37
                                )
                            )
                            wonder.copy(
                                isCompleted = true,
                                grainSpent = wonder.grainNeeded,
                                claySpent = wonder.clayNeeded,
                                silverSpent = wonder.silverNeeded
                            )
                        } else {
                            logs.add(
                                TurnLogItem(
                                    turn = nextTurn,
                                    yearBCE = nextYearBCE,
                                    category = LogCategory.DEVELOPMENT,
                                    title = "🏛️ Завершено Етап ${wonder.stage} для ${wonder.name}",
                                    description = "Будівельники завершили ярус ${wonder.stage} дива у місті ${wonder.cityId.uppercase()}.",
                                    factionId = builderFaction,
                                    highlightColorHex = 0xFF8D6E63
                                )
                            )
                            wonder.copy(
                                stage = newStage,
                                grainSpent = 0,
                                claySpent = 0,
                                silverSpent = 0
                            )
                        }
                    } else {
                        wonder.copy(
                            grainSpent = newGrainSpent,
                            claySpent = newClaySpent,
                            silverSpent = newSilverSpent
                        )
                    }
                } else {
                    wonder
                }
            } else {
                wonder
            }
        }

        // 6. Educational History Factoid
        val factIndex = (nextTurn - 1) % HISTORICAL_FACTS.size
        logs.add(
            TurnLogItem(
                turn = nextTurn,
                yearBCE = nextYearBCE,
                category = LogCategory.HISTORY_FACT,
                title = "💡 Епоха Месопотамії: Історичний факт",
                description = HISTORICAL_FACTS[factIndex],
                highlightColorHex = 0xFFD4AF37
            )
        )

        return BotTurnResult(
            updatedCities = mutableCities,
            updatedWonders = updatedWonders,
            logs = logs,
            campaignSourceCityId = campaignSourceId,
            campaignTargetCityId = campaignTargetId
        )
    }
}

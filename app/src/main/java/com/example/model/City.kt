package com.example.model

data class City(
    val id: String,
    val name: String,
    val title: String,
    val factionId: String,
    val mapX: Float, // Normalized 0.0 .. 1.0 on Mesopotamian map
    val mapY: Float,
    val river: String,
    val population: Int,
    val buildings: List<String> = emptyList(),
    val buildingInProgress: String? = null,
    val buildingTurnsRemaining: Int = 0,
    val garrison: Map<String, Int> = mapOf("spearmen" to 2), // unitId -> count
    val loyalty: Int = 85,
    val tradePartners: List<String> = emptyList(), // city IDs
    val historyFact: String
) {
    val defenseRating: Int
        get() {
            var rating = 20
            if ("walls" in buildings) rating += 40
            if ("ziggurat" in buildings) rating += 20
            val garrisonStr = garrison.entries.sumOf { (unitId, count) ->
                UnitType.getById(unitId).defense * count
            }
            return rating + garrisonStr
        }

    val totalMilitaryPower: Int
        get() = garrison.entries.sumOf { (unitId, count) ->
            UnitType.getById(unitId).strength * count
        }

    fun hasBuilding(buildingId: String): Boolean = buildings.contains(buildingId)

    companion object {
        val INITIAL_CITIES = listOf(
            City(
                id = "uruk",
                name = "Урук",
                title = "Місто легендарного Гільгамеша",
                factionId = "uruk",
                mapX = 0.46f,
                mapY = 0.62f,
                river = "Євфрат",
                population = 32000,
                buildings = listOf("canals", "walls", "edubba"),
                garrison = mapOf("spearmen" to 3, "archers" to 2),
                loyalty = 90,
                historyFact = "Урук був найбільшим мегаполісом свого часу! Саме тут знайдено найдавніші таблички з клинописом віком понад 5000 років."
            ),
            City(
                id = "ur",
                name = "Ур",
                title = "Столиця морських караванів",
                factionId = "ur",
                mapX = 0.58f,
                mapY = 0.74f,
                river = "Євфрат (гирло)",
                population = 28000,
                buildings = listOf("canals", "harbor", "granary", "ziggurat"),
                garrison = mapOf("spearmen" to 2, "phalanx" to 1, "caravan" to 1),
                loyalty = 92,
                historyFact = "Великий зикурат в Урі зберігся до наших днів! Він мав три яруси і був присвячений богу місяця Нанні."
            ),
            City(
                id = "lagash",
                name = "Лагаш",
                title = "Твердиня царів-воїнів",
                factionId = "lagash",
                mapX = 0.64f,
                mapY = 0.58f,
                river = "Тигр (канали)",
                population = 25000,
                buildings = listOf("canals", "bronze_foundry", "walls"),
                garrison = mapOf("phalanx" to 2, "spearmen" to 2, "chariot" to 1),
                loyalty = 88,
                historyFact = "Цар Лагаша Урукагіна провів перші в історії соціальні реформи, захистивши бідних і вдів від утисків багатіїв."
            ),
            City(
                id = "kish",
                name = "Кіш",
                title = "Північний царський престол",
                factionId = "kish",
                mapX = 0.35f,
                mapY = 0.38f,
                river = "Євфрат",
                population = 22000,
                buildings = listOf("canals", "edubba", "granary"),
                garrison = mapOf("spearmen" to 2, "chariot" to 2),
                loyalty = 84,
                historyFact = "Титул «Лугаль Кіша» означав володаря всього Південного Дворіччя. Царі мріяли про цей престижний статус."
            ),
            City(
                id = "nippur",
                name = "Ніппур",
                title = "Святилище бога Енліля",
                factionId = "nippur",
                mapX = 0.42f,
                mapY = 0.48f,
                river = "Євфрат",
                population = 20000,
                buildings = listOf("canals", "ziggurat", "temple_inanna"),
                garrison = mapOf("spearmen" to 2, "priest" to 2),
                loyalty = 95,
                historyFact = "Ніппур ніколи не вів загарбницьких воєн. Він був священним містом миру, як пізніше Олімпія чи Ватикан."
            ),
            City(
                id = "umma",
                name = "Умма",
                title = "Місто на каналі Ітурунгал",
                factionId = "umma",
                mapX = 0.54f,
                mapY = 0.52f,
                river = "Канал Ітурунгал",
                population = 19000,
                buildings = listOf("canals", "walls"),
                garrison = mapOf("archers" to 2, "spearmen" to 2),
                loyalty = 80,
                historyFact = "Століттями Умма та Лагаш вели запеклу боротьбу за родючу долину Ґуедінна, поки не уклали першу мирну угоду."
            ),
            City(
                id = "eridu",
                name = "Еріду",
                title = "Найдавніше місто бога мудрості Енкі",
                factionId = "ur",
                mapX = 0.54f,
                mapY = 0.82f,
                river = "Болота Євфрату",
                population = 14000,
                buildings = listOf("canals", "harbor"),
                garrison = mapOf("spearmen" to 2),
                loyalty = 85,
                historyFact = "За шумерськими міфами, Еріду було найпершим містом, яке виникло на Землі ще до великого Потопу."
            ),
            City(
                id = "susa",
                name = "Сузи (Елам)",
                title = "Гірська столиця Еламу",
                factionId = "elam",
                mapX = 0.88f,
                mapY = 0.45f,
                river = "Річка Карун (Загрос)",
                population = 21000,
                buildings = listOf("walls", "bronze_foundry"),
                garrison = mapOf("archers" to 3, "phalanx" to 1),
                loyalty = 80,
                historyFact = "Еламіти жили в горах Загросу на схід від Межиріччя. Вони володіли багатими покладами міді, олова та будівельного каміння."
            )
        )
    }
}

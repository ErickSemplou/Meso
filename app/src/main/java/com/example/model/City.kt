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
    val uniqueTrait: String,
    val traitDescription: String,
    val bonusGrain: Int = 0,
    val bonusClay: Int = 0,
    val bonusBronze: Int = 0,
    val bonusSilver: Int = 0,
    val bonusDefense: Int = 0,
    val historyFact: String
) {
    val defenseRating: Int
        get() {
            var rating = 20 + bonusDefense
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
                mapX = 0.28f,
                mapY = 0.58f,
                river = "Євфрат",
                population = 32000,
                buildings = listOf("canals", "walls", "edubba"),
                garrison = mapOf("spearmen" to 3, "archers" to 2),
                loyalty = 90,
                uniqueTrait = "Величні мури Гільгамеша",
                traitDescription = "+30 до оборони мурів, +15 глини та +10 срібла за хід.",
                bonusDefense = 30,
                bonusClay = 15,
                bonusSilver = 10,
                historyFact = "Урук був найбільшим мегаполісом свого часу! Саме тут знайдено найдавніші пам'ятки клинопису віком понад 5000 років."
            ),
            City(
                id = "ur",
                name = "Ур",
                title = "Столиця морських караванів",
                factionId = "ur",
                mapX = 0.52f,
                mapY = 0.72f,
                river = "Євфрат (гирло)",
                population = 28000,
                buildings = listOf("canals", "harbor", "granary", "ziggurat"),
                garrison = mapOf("spearmen" to 2, "phalanx" to 1, "caravan" to 1),
                loyalty = 92,
                uniqueTrait = "Морські ворота Дільмуна",
                traitDescription = "+30 срібла за хід та початковий торговий флот.",
                bonusSilver = 30,
                bonusGrain = 10,
                historyFact = "Великий зикурат в Урі зберігся до наших днів! Він мав три яруси і був присвячений богу місяця Нанні."
            ),
            City(
                id = "lagash",
                name = "Лагаш",
                title = "Твердиня царів-воїнів",
                factionId = "lagash",
                mapX = 0.68f,
                mapY = 0.52f,
                river = "Тигр (канали)",
                population = 25000,
                buildings = listOf("canals", "bronze_foundry", "walls"),
                garrison = mapOf("phalanx" to 2, "spearmen" to 2, "chariot" to 1),
                loyalty = 88,
                uniqueTrait = "Бронзова твердиня воїнів",
                traitDescription = "+20 бронзи за хід та непереможна шумерська фаланга.",
                bonusBronze = 20,
                bonusDefense = 15,
                historyFact = "Цар Лагаша Урукагіна провів перші в історії соціальні реформи, захистивши бідних і вдів від утисків багатіїв."
            ),
            City(
                id = "kish",
                name = "Кіш",
                title = "Північний царський престол",
                factionId = "kish",
                mapX = 0.22f,
                mapY = 0.24f,
                river = "Євфрат",
                population = 22000,
                buildings = listOf("canals", "edubba", "granary"),
                garrison = mapOf("spearmen" to 2, "chariot" to 2),
                loyalty = 84,
                uniqueTrait = "Царський престол Півночі",
                traitDescription = "+35 зерна та +15 срібла за хід з родючих житниць.",
                bonusGrain = 35,
                bonusSilver = 15,
                historyFact = "Титул «Лугаль Кіша» означав володаря всього Південного Дворіччя. Царі мріяли про цей престижний статус."
            ),
            City(
                id = "nippur",
                name = "Ніппур",
                title = "Святилище бога Енліля",
                factionId = "nippur",
                mapX = 0.42f,
                mapY = 0.32f,
                river = "Євфрат",
                population = 20000,
                buildings = listOf("canals", "ziggurat", "temple_inanna"),
                garrison = mapOf("spearmen" to 2, "priest" to 2),
                loyalty = 95,
                uniqueTrait = "Священне лоно Енліля",
                traitDescription = "+15 срібла, +15 глини, +20 оборони та захист від бунтів.",
                bonusSilver = 15,
                bonusClay = 15,
                bonusDefense = 20,
                historyFact = "Ніппур ніколи не вів загарбницьких воєн. Він був священним містом миру, як пізніше Олімпія чи Ватикан."
            ),
            City(
                id = "umma",
                name = "Умма",
                title = "Місто на каналі Ітурунгал",
                factionId = "umma",
                mapX = 0.60f,
                mapY = 0.34f,
                river = "Канал Ітурунгал",
                population = 19000,
                buildings = listOf("canals", "walls"),
                garrison = mapOf("archers" to 2, "spearmen" to 2),
                loyalty = 80,
                uniqueTrait = "Магістраль Ітурунгал",
                traitDescription = "+25 глини та +20 зерна щоходу від розвиненої системи шлюзів.",
                bonusClay = 25,
                bonusGrain = 20,
                historyFact = "Століттями Умма та Лагаш вели запеклу боротьбу за родючу долину Ґуедінна, поки не уклали першу мирну угоду."
            ),
            City(
                id = "eridu",
                name = "Еріду",
                title = "Найдавніше місто бога мудрості Енкі",
                factionId = "ur",
                mapX = 0.32f,
                mapY = 0.82f,
                river = "Болота Євфрату",
                population = 14000,
                buildings = listOf("canals", "harbor"),
                garrison = mapOf("spearmen" to 2),
                loyalty = 85,
                uniqueTrait = "Перше прадавнє місто",
                traitDescription = "+25 зерна та +10 срібла від багатих очеретяних плавнів.",
                bonusGrain = 25,
                bonusSilver = 10,
                historyFact = "За шумерськими міфами, Еріду було найпершим містом, яке виникло на Землі ще до великого Потопу."
            ),
            City(
                id = "susa",
                name = "Сузи (Елам)",
                title = "Гірська столиця Еламу",
                factionId = "elam",
                mapX = 0.86f,
                mapY = 0.32f,
                river = "Річка Карун (Загрос)",
                population = 21000,
                buildings = listOf("walls", "bronze_foundry"),
                garrison = mapOf("archers" to 3, "phalanx" to 1),
                loyalty = 80,
                uniqueTrait = "Гірські копальні Загросу",
                traitDescription = "+25 бронзи за хід та +25 гірської оборони.",
                bonusBronze = 25,
                bonusDefense = 25,
                historyFact = "Еламіти жили в горах Загросу на схід від Межиріччя. Вони володіли багатими покладами міді, олова та будівельного каміння."
            )
        )
    }
}

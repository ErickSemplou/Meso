package com.example.model

data class UnitType(
    val id: String,
    val name: String,
    val role: String,
    val description: String,
    val historyFact: String, // 6th-grade note
    val strength: Int,
    val defense: Int,
    val speed: Int,
    val upkeepGrain: Int,
    val cost: ResourceCost
) {
    companion object {
        val SPEARMEN = UnitType(
            id = "spearmen",
            name = "Списоносці ополчення",
            role = "Піхота оборони",
            description = "Містяни та селяни, озброєні довгими очеретяними списами з мідними вістрями та повстяними шапками.",
            historyFact = "В ополчення шумерських міст призивали звичайних хліборобів, коли місту загрожувала війна.",
            strength = 15,
            defense = 20,
            speed = 2,
            upkeepGrain = 5,
            cost = ResourceCost(grain = 20, bronze = 5, silver = 25)
        )

        val HEAVY_PHALANX = UnitType(
            id = "phalanx",
            name = "Царська фаланга",
            role = "Важка піхота прориву",
            description = "Елітні воїни правителя у бронзових шоломах. Ступають щільним строєм під захистом важких прямокутних щитів.",
            historyFact = "Перша в історії людства військова фаланга зображена на «Стелі шулік» правителя Еаннатума (близько 2450 р. до н.е.)!",
            strength = 35,
            defense = 40,
            speed = 1,
            upkeepGrain = 12,
            cost = ResourceCost(grain = 40, bronze = 25, silver = 65)
        )

        val ONAGER_CHARIOT = UnitType(
            id = "chariot",
            name = "Колісниці на онаграх",
            role = "Ударна кавалерія",
            description = "Важкі дерев'яні візки на чотирьох суцільних колесах, у які запряжені дикі азійські віслюки (онагри).",
            historyFact = "У часи Шумеру коней ще не приручили для упряжі! Замість них запрягали швидких і норовистих онагрів — диких віслюків.",
            strength = 45,
            defense = 20,
            speed = 3,
            upkeepGrain = 15,
            cost = ResourceCost(grain = 50, clay = 20, bronze = 30, silver = 85)
        )

        val ARCHERS = UnitType(
            id = "archers",
            name = "Шумерські лучники",
            role = "Дальній бій",
            description = "Влучні стрільці з луками з міцного очерету та дерева. Обсипають ворога градом стріл зі стін міста.",
            historyFact = "Стріли робили з тростини, а вістря — з кремнію чи міді. Вони були смертельною зброєю на відстані до 100 метрів.",
            strength = 22,
            defense = 10,
            speed = 2,
            upkeepGrain = 6,
            cost = ResourceCost(grain = 25, bronze = 10, silver = 35)
        )

        val PRIEST_DIPLOMAT = UnitType(
            id = "priest",
            name = "Жрець-посол (Ен)",
            role = "Дипломатія та мир",
            description = "Посланник із царськими дарами та печатками. Здійснює переговори, приборкує гнів богів і підносить лояльність племен.",
            historyFact = "Жерці в Шумері були не лише священниками, а й дипломатами, скарбниками та головними архітекторами держави.",
            strength = 5,
            defense = 10,
            speed = 3,
            upkeepGrain = 4,
            cost = ResourceCost(grain = 15, clay = 20, silver = 45)
        )

        val CARAVAN = UnitType(
            id = "caravan",
            name = "Торговий караван (Тамкар)",
            role = "Торгівля та ресурси",
            description = "Осли та в'ючні тварини, завантажені зерном, тканинами та олією. Відправляються до сусідніх міст за сріблом та оловом.",
            historyFact = "Торговців у Межиріччі називали «тамкарами». Вони укладали перші в світі торгові контракти, записані на глиняних табличках у глиняних конвертах!",
            strength = 3,
            defense = 5,
            speed = 3,
            upkeepGrain = 5,
            cost = ResourceCost(grain = 30, clay = 15, silver = 40)
        )

        val ALL_UNITS = listOf(SPEARMEN, HEAVY_PHALANX, ONAGER_CHARIOT, ARCHERS, PRIEST_DIPLOMAT, CARAVAN)

        fun getById(id: String): UnitType = ALL_UNITS.find { it.id == id } ?: SPEARMEN
    }
}

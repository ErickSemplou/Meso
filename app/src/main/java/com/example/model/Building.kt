package com.example.model

data class Building(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val historyFact: String, // 6th-grade historical educational note
    val cost: ResourceCost,
    val turnsToBuild: Int,
    val incomeGrain: Int = 0,
    val incomeClay: Int = 0,
    val incomeBronze: Int = 0,
    val incomeSilver: Int = 0,
    val loyaltyBonus: Int = 0,
    val pietyBonus: Int = 0,
    val defenseBonus: Int = 0,
    val maxGrainStorageBonus: Int = 0
) {
    companion object {
        val ALL_BUILDINGS = listOf(
            Building(
                id = "canals",
                name = "Іригаційні канали",
                subtitle = "Дамби та зрошувальні рови",
                description = "Канали відводять води Тигру та Євфрату на поля, забезпечуючи щедрі врожаї ячменю та пшениці.",
                historyFact = "Землеробство у Межиріччі було можливе лише завдяки штучному зрошенню. Шумери першими навчилися будувати складні системи дамб.",
                cost = ResourceCost(grain = 20, clay = 40, silver = 30),
                turnsToBuild = 1,
                incomeGrain = 35,
                incomeClay = 10
            ),
            Building(
                id = "granary",
                name = "Царське зерносховище",
                subtitle = "Захист від посухи та голоду",
                description = "Глиняні бункери для зберігання мішків із зерном. Захищає врожай від мишей та повеней.",
                historyFact = "Зерно було головною валютою Шумеру! Ним платили податки і видавали пайки будівничим та воїнам.",
                cost = ResourceCost(grain = 10, clay = 50, silver = 40),
                turnsToBuild = 2,
                maxGrainStorageBonus = 200,
                loyaltyBonus = 5
            ),
            Building(
                id = "walls",
                name = "Мури з цегли-сирцю",
                subtitle = "Оборонні міські укріплення",
                description = "Високі товсті мури з невипаленої глиняної цегли, крізь які не проїде жодна ворожа колісниця.",
                historyFact = "У Месопотамії не було будівельного каменю та лісу, тому мури зводили з суміші мулу, глини та соломи, висушеної на сонці.",
                cost = ResourceCost(grain = 15, clay = 70, bronze = 10, silver = 50),
                turnsToBuild = 2,
                defenseBonus = 40,
                loyaltyBonus = 10
            ),
            Building(
                id = "ziggurat",
                name = "Східчастий зикурат",
                subtitle = "Драбина до небесних богів",
                description = "Велична ступінчаста піраміда з глиняної цегли. На її вершині стоїть святилище головного бога міста.",
                historyFact = "Зикурати слугували не лише храмами, а й обсерваторіями: жерці з їхньої вершини спостерігали за зорями і створили перший точний календар!",
                cost = ResourceCost(grain = 40, clay = 100, bronze = 15, silver = 90),
                turnsToBuild = 3,
                pietyBonus = 25,
                loyaltyBonus = 15,
                incomeSilver = 20
            ),
            Building(
                id = "edubba",
                name = "Школа писарів (Едуба)",
                subtitle = "«Будинок глиняних табличок»",
                description = "Місце, де майбутні чиновники й мудреці вчаться писати клинописом загостреною тростинною паличкою.",
                historyFact = "Слово «Едуба» шумерською означає «дім табличок». Учнів суворо карали різками за помилки, а випускники ставали найшанованішими людьми держави.",
                cost = ResourceCost(grain = 25, clay = 50, silver = 60),
                turnsToBuild = 2,
                incomeSilver = 25,
                pietyBonus = 10
            ),
            Building(
                id = "bronze_foundry",
                name = "Бронзоливарна майстерня",
                subtitle = "Кування зброї та серпів",
                description = "Горна для плавлення міді та олова. Виготовляє наконечники списів, сокири, шоломи та міцні серпи.",
                historyFact = "Бронзова доба настала, коли майстри змішали мідь з оловом. Бронзова зброя була набагато твердішою за мідну.",
                cost = ResourceCost(grain = 30, clay = 60, bronze = 10, silver = 70),
                turnsToBuild = 2,
                incomeBronze = 20,
                incomeSilver = 15
            ),
            Building(
                id = "harbor",
                name = "Причал на Євфраті",
                subtitle = "Торгова гавань для човнів",
                description = "Пристань для очеретяних суден (магурів). Приймає товари з далеких країв Перської затоки.",
                historyFact = "Шумерські човни плели з очерету й обмазували смолою (бітумом), щоб вони не пропускали воду. На них допливали аж до Індії!",
                cost = ResourceCost(grain = 20, clay = 40, silver = 50),
                turnsToBuild = 1,
                incomeSilver = 30,
                incomeClay = 10
            ),
            Building(
                id = "temple_inanna",
                name = "Храм богині Інанни",
                subtitle = "Покровителька любові й родючості",
                description = "Храмовий комплекс, прикрашений мозаїкою з глиняних конусів, де моляться за приплід худоби та врожай.",
                historyFact = "Інанна (у вавилонян — Іштар) була найпопулярнішою богинею Шумеру. Її символом була восьмипроменева зірка.",
                cost = ResourceCost(grain = 30, clay = 60, silver = 50),
                turnsToBuild = 2,
                loyaltyBonus = 15,
                pietyBonus = 15
            )
        )

        fun getById(id: String): Building = ALL_BUILDINGS.find { it.id == id } ?: ALL_BUILDINGS.first()
    }
}

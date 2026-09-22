package com.example.model

enum class TechBranch(val displayName: String) {
    CIVIL("Цивільні відкриття"),
    MILITARY("Військова справа")
}

data class Technology(
    val id: String,
    val name: String,
    val branch: TechBranch,
    val description: String,
    val historyFact: String, // 6th-grade syllabus
    val costSciencePoints: Int,
    val turnsRequired: Int,
    val prerequisites: List<String> = emptyList(),
    val bonusSummary: String
) {
    companion object {
        val ALL_TECHS = listOf(
            // --- CIVIL BRANCH ---
            Technology(
                id = "cuneiform",
                name = "Клинопис на глині",
                branch = TechBranch.CIVIL,
                description = "Письмо за допомогою загостреної очеретяної палички на сирій глині. Знаки нагадують клини.",
                historyFact = "Шумери першими на Землі винайшли писемність близько 3300 р. до н.е.! Спочатку це були малюнки-піктограми, які згодом спростилися до клинів.",
                costSciencePoints = 50,
                turnsRequired = 2,
                bonusSummary = "+15% до прибутку срібла від обліку податків"
            ),
            Technology(
                id = "pottery_wheel",
                name = "Гончарне коло",
                branch = TechBranch.CIVIL,
                description = "Обертове колесо для швидкого ліплення ідеально круглого глиняного посуду.",
                historyFact = "Гончарне коло стало прототипом транспортного колеса! Воно дало змогу створювати посуд для зберігання зерна, олії та пива.",
                costSciencePoints = 60,
                turnsRequired = 2,
                prerequisites = listOf("cuneiform"),
                bonusSummary = "+20% до видобутку та обробки глини"
            ),
            Technology(
                id = "seed_plow",
                name = "Плуг-сіялка",
                branch = TechBranch.CIVIL,
                description = "Дерев'яний плуг зі спеціальною лійкою, яка кидає зерна прямо в борозну, захищаючи їх від птахів.",
                historyFact = "Шумерський плуг був справжнім дивом агрономії: він одночасно орав землю і рівномірно засівав насіння, що подвоїло врожаї!",
                costSciencePoints = 80,
                turnsRequired = 3,
                prerequisites = listOf("pottery_wheel"),
                bonusSummary = "+35% до врожайності зерна на зрошуваних полях"
            ),
            Technology(
                id = "sexagesimal",
                name = "Шістдесяткова система",
                branch = TechBranch.CIVIL,
                description = "Математична система з основою 60: ділення кола на 360 градусів, години на 60 хвилин, а хвилини на 60 секунд.",
                historyFact = "Ми й досі користуємося шумерським винаходом щодня, коли дивимося на годинник (60 хвилин) чи вимірюємо кути (360 градусів)!",
                costSciencePoints = 100,
                turnsRequired = 3,
                prerequisites = listOf("seed_plow"),
                bonusSummary = "+30% до швидкості вивчення всіх подальших наук"
            ),
            Technology(
                id = "laws_urukagina",
                name = "Перші писані закони",
                branch = TechBranch.CIVIL,
                description = "Чіткі правила життя громади, висічені на кам'яних стовпах і записані на табличках.",
                historyFact = "Цар Лагаша Урукагіна та цар Вавилона Хаммурапі створили перші зводи законів. Правило «око за око» мало зупинити сваволю беззаконня.",
                costSciencePoints = 120,
                turnsRequired = 4,
                prerequisites = listOf("sexagesimal"),
                bonusSummary = "+20 до лояльності населення та припинення бунтів"
            ),

            // --- MILITARY BRANCH ---
            Technology(
                id = "bronze_weapons",
                name = "Бронзове лиття",
                branch = TechBranch.MILITARY,
                description = "Виплавка міцного сплаву міді та олова. Списи і сокири стають гострішими й не гнуться у сутичках.",
                historyFact = "Оскільки в Шумері не було рудників, мідь везли морем із острова Дільмун (Бахрейн), а олово — караванами з гір Загросу.",
                costSciencePoints = 50,
                turnsRequired = 2,
                bonusSummary = "+20% до сили атаки ополчення та піхоти"
            ),
            Technology(
                id = "wheel_cart",
                name = "Колесо та колісниці",
                branch = TechBranch.MILITARY,
                description = "Суцільні дерев'яні дискові колеса, скріплені мідними цвяхами, на яких рухаються ударні бойові візки.",
                historyFact = "Колесо винайшли в Месопотамії близько 3500 р. до н.е. Перші бойові колісниці були важкими й неповороткими, але наганяли жах на ворогів!",
                costSciencePoints = 75,
                turnsRequired = 3,
                prerequisites = listOf("bronze_weapons"),
                bonusSummary = "Відкриває найм бойових колісниць на онаграх"
            ),
            Technology(
                id = "phalanx_formation",
                name = "Бойова фаланга",
                branch = TechBranch.MILITARY,
                description = "Дисциплінований зімкнутий стрій воїнів у бронзових шоломах, прикритих стіною важких прямокутних щитів.",
                historyFact = "Шумерська фаланга — найдавніший організований стрій піхоти у військовій історії, який передував грецьким гоплітам на 1500 років!",
                costSciencePoints = 90,
                turnsRequired = 3,
                prerequisites = listOf("wheel_cart"),
                bonusSummary = "+35% до захисту військ та відкриває найм царської фаланги"
            ),
            Technology(
                id = "baked_brick_walls",
                name = "Оборонні башти й цегла",
                branch = TechBranch.MILITARY,
                description = "Випалювання цегли у печах із застосуванням природного бітуму (асфальту) як міцного розчину.",
                historyFact = "Випалена у печі цегла не боялася дощів та повеней. Саме нею облицьовували ворота та нижні яруси великих зикуратів.",
                costSciencePoints = 110,
                turnsRequired = 3,
                prerequisites = listOf("phalanx_formation"),
                bonusSummary = "+50 до міцності міських мурів та фортифікацій"
            )
        )

        fun getById(id: String): Technology = ALL_TECHS.find { it.id == id } ?: ALL_TECHS.first()
    }
}

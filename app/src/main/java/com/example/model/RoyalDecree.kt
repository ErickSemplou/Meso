package com.example.model

data class RoyalDecree(
    val id: String,
    val name: String,
    val kingOrOrigin: String,
    val epoch: String,
    val description: String,
    val historyFact: String, // 6th-grade historical educational insight
    val cost: ResourceCost,
    val effectDescription: String,
    val grainBonusPercent: Int = 0,
    val silverBonusPercent: Int = 0,
    val loyaltyBonus: Int = 0,
    val defenseBonus: Int = 0,
    val pietyBonus: Int = 0
) {
    companion object {
        val ALL_DECREES = listOf(
            RoyalDecree(
                id = "urukagina_reforms",
                name = "Реформи царя Урукагіни",
                kingOrOrigin = "Урукагіна (Лагаш)",
                epoch = "бл. 2380 р. до н.е.",
                description = "Перший в історії закон про захист незахищених верств населення. Обмежує податки жерців та чиновників, забороняє відбирати майно у бідних, сиріт та вдів.",
                historyFact = "Реформи Урукагіни з міста Лагаш вважаються найпершим у світовій історії свідченням боротьби за соціальну справедливість та свободу («ама-ґі»).",
                cost = ResourceCost(silver = 50, grain = 30),
                effectDescription = "+15 до лояльності всіх міст, +10% до врожаю зерна.",
                loyaltyBonus = 15,
                grainBonusPercent = 10
            ),
            RoyalDecree(
                id = "ur_nammu_code",
                name = "Кодекс законів Ур-Намму",
                kingOrOrigin = "Ур-Намму (Ур)",
                epoch = "бл. 2100 р. до н.е.",
                description = "Найдавніший збережений звід законів у світі. Замість кривавої помсти запроваджує грошові штрафи в срібних шекелях та стандартизує міри ваги й об'єму.",
                historyFact = "Кодекс Ур-Намму був створений на три століття раніше за знамениті закони царя Хаммурапі! Він написаний шумерською мовою на глиняних табличках.",
                cost = ResourceCost(silver = 70, clay = 40),
                effectDescription = "+25% доходу від торгівлі та срібних податків, +10 лояльності.",
                silverBonusPercent = 25,
                loyaltyBonus = 10
            ),
            RoyalDecree(
                id = "akitu_festival",
                name = "Священне свято Акіту",
                kingOrOrigin = "Усі шумерські міста",
                epoch = "Весняне рівнодення",
                description = "Дванадцятиденне свято нового року та родючості під час весняної повені. Правитель складає клятву перед верховним богом у зикураті.",
                historyFact = "Під час свята Акіту цар знімав корону перед жерцем і каявся, що справедливо дбав про місто і не утискав народ.",
                cost = ResourceCost(grain = 40, silver = 30),
                effectDescription = "+25 до благословення богів (побожність), імунітет до бунтів.",
                pietyBonus = 25,
                loyaltyBonus = 10
            ),
            RoyalDecree(
                id = "edubba_standard",
                name = "Стандарт клинописного обліку",
                kingOrOrigin = "Школи Едуба",
                epoch = "Бронзова доба",
                description = "Уніфікація глиняних табличок та печаток у всіх містах. Писарі ретельно фіксують мішки з зерном, поголів'я худоби та податки.",
                historyFact = "Шумери винайшли шестидесяткову систему числення, якою ми користуємося й досі: ділимо годину на 60 хвилин, а коло — на 360 градусів!",
                cost = ResourceCost(clay = 60, silver = 40),
                effectDescription = "+15% до всіх надходжень срібла та глини.",
                silverBonusPercent = 15
            ),
            RoyalDecree(
                id = "stele_vultures_army",
                name = "Указ про Царську фалангу",
                kingOrOrigin = "Еаннатум (Лагаш)",
                epoch = "бл. 2450 р. до н.е.",
                description = "Запровадження стандартного спорядження для воїнів: суцільні бронзові шоломи, повстяні плащі з мідними бляхами та важкі щити.",
                historyFact = "На «Стелі шулік» показано воїнів, які йдуть щільною фалангою в 6 рядів, прикриваючись великими прямокутними щитами.",
                cost = ResourceCost(bronze = 30, silver = 50),
                effectDescription = "+20 до оборони всіх міських гарнізонів.",
                defenseBonus = 20
            ),
            RoyalDecree(
                id = "irrigation_corvee",
                name = "Загальна іригаційна повинність",
                kingOrOrigin = "Рада старійшин",
                epoch = "Щорічно перед повінню",
                description = "Мобілізація всіх містян на розчищення магістральних каналів від мулу та зміцнення захисних дамб.",
                historyFact = "Якщо не розчищати канали від наносного мулу річок, поля за кілька років перетворювалися на болота або засолену пустелю.",
                cost = ResourceCost(grain = 35, clay = 30),
                effectDescription = "+20% до виробництва зерна в усіх містах.",
                grainBonusPercent = 20
            )
        )

        fun getById(id: String): RoyalDecree = ALL_DECREES.find { it.id == id } ?: ALL_DECREES.first()
    }
}

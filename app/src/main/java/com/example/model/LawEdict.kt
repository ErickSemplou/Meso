package com.example.model

/**
 * Historical Mesopotamian Edict & Law Reform.
 * Represents foundational codes of law (Urukagina, Ur-Nammu, Hammurabi)
 * with authentic historical context and strategic trade-offs.
 */
data class LawEdict(
    val id: String,
    val name: String,
    val origin: String, // e.g. "Реформи Урукагіни (2380 р. до н.е.)"
    val description: String,
    val historicalBackground: String,
    val prosText: String,
    val consText: String,
    val grainModifier: Int = 0,
    val clayModifier: Int = 0,
    val bronzeModifier: Int = 0,
    val silverModifier: Int = 0,
    val defenseModifier: Int = 0,
    val pietyModifier: Int = 0,
    val militaryPowerBonusPercent: Int = 0,
    val requiredSilverToEnact: Int = 20
) {
    companion object {
        val ALL_LAWS = listOf(
            LawEdict(
                id = "misharum_debt_relief",
                name = "Едикт «Мішарум» (Прощення боргів)",
                origin = "Реформи царя Урукагіни з Лагаша",
                description = "Звільнення збіднілих селян від боргового рабства та скасування несправедливих поборів жерців.",
                historicalBackground = "Перший в історії закон про соціальну справедливість. Урукагіна проголосив, що 'сирота і вдова більше не будуть віддані на поталу могутньому'.",
                prosText = "+20 до лояльності, +15 благочестя храмів щоходу",
                consText = "-15 срібла за хід (втрата скарбницею боргових зборів)",
                silverModifier = -15,
                pietyModifier = 15,
                requiredSilverToEnact = 15
            ),
            LawEdict(
                id = "lex_talionis_eye_for_eye",
                name = "Принцип Таліону («Око за око»)",
                origin = "Клинописний звід законів царя Хаммурапі",
                description = "Суворе правосуддя рівної відплати: покарання точно відповідає скоєному злочину.",
                historicalBackground = "Закони висічені на чорній базальтовій стелі, переданій богом сонця Шамашем. Встановили залізну дисципліну у містах.",
                prosText = "+30 до міської оборони, +10% бойова міць військ",
                consText = "-10 до благочестя (страх перед суворими вироками)",
                defenseModifier = 30,
                pietyModifier = -10,
                militaryPowerBonusPercent = 10,
                requiredSilverToEnact = 25
            ),
            LawEdict(
                id = "ilku_corvee_canals",
                name = "Канальна повинність («Ілку»)",
                origin = "Шумерське царське право іригації",
                description = "Усі працездатні містяни та общинники зобов'язані щороку чистити мул та ремонтувати дамби.",
                historicalBackground = "Без постійного догляду за каналами повені Євфрату та Тигру руйнували посіви й перетворювали поля на солончаки.",
                prosText = "+40 зерна та +20 глини щоходу",
                consText = "-10 срібла на утримання наглядачів",
                grainModifier = 40,
                clayModifier = 20,
                silverModifier = -10,
                requiredSilverToEnact = 20
            ),
            LawEdict(
                id = "standard_shekel_mina",
                name = "Єдині міри зерна та срібного шекеля",
                origin = "Реформа царя Шульгі (ІІІ династія Ура)",
                description = "Запровадження стандартних царських гир для зважування срібла (шекель = 8.3 г, міна = 500 г) та мішків «гур».",
                historicalBackground = "Усунення шахрайства на базарах стимулювало жваву заморську торгівлю з Дільмуном (Бахрейн) і Маганом (Оман).",
                prosText = "+35 срібла за хід від митних зборів та ринків",
                consText = "-15 глини на виготовлення офіційних печаток",
                silverModifier = 35,
                clayModifier = -15,
                requiredSilverToEnact = 30
            ),
            LawEdict(
                id = "temple_sacred_tithe",
                name = "Храмова десятина та володіння Енліля",
                origin = "Ніппурські священні устави",
                description = "Виділення найкращих прирічкових полів богам, а зерносховища храмів стають резервом на випадок посухи.",
                historicalBackground = "Жерці вели астрономічні спостереження, вираховували дату повені та зберігали посівне зерно для всієї громади.",
                prosText = "+30 до благочестя, захист від голоду при посусі",
                consText = "-25 зерна щоходу на щоденні жертвоприношення",
                pietyModifier = 30,
                grainModifier = -25,
                requiredSilverToEnact = 20
            ),
            LawEdict(
                id = "royal_bronze_monopoly",
                name = "Царська монополія на зброярні",
                origin = "Укази Саргона Великого (Аккадська імперія)",
                description = "Вся мідь і олово надходять виключно до арсеналів правителя для кування наконечників списів та серпів.",
                historicalBackground = "Перша регулярна професійна армія з 5400 воїнів, які 'щодня їли хліб перед царем', виникла завдяки державній зброї.",
                prosText = "+25 бронзи щоходу, +20% до сили фаланги",
                consText = "-25 срібла на утримання державних зброярів",
                bronzeModifier = 25,
                silverModifier = -25,
                militaryPowerBonusPercent = 20,
                requiredSilverToEnact = 35
            )
        )
    }
}

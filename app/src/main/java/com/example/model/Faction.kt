package com.example.model

import androidx.compose.ui.graphics.Color

data class Faction(
    val id: String,
    val name: String,
    val title: String,
    val ruler: String,
    val patronDeity: String,
    val description: String,
    val bonusDescription: String,
    val bannerColorHex: String,
    val isPlayable: Boolean = true
) {
    val bannerColor: Color
        get() = try {
            Color(android.graphics.Color.parseColor(bannerColorHex))
        } catch (_: Exception) {
            Color(0xFF8C461E)
        }

    companion object {
        val URUK = Faction(
            id = "uruk",
            name = "Урук",
            title = "Місто Гільгамеша",
            ruler = "Енмеркар / Гільгамеш",
            patronDeity = "Богиня Інанна (любов та родючість)",
            description = "Найвеличніше місто-держава Південного Межиріччя. Славне своїми могутніми мурами з цегли-сирцю та храмами Білого Зикурату.",
            bonusDescription = "+20% до культури та надійності оборонних мурів. Зменшені витрати на розвиток клинопису.",
            bannerColorHex = "#1565C0", // Lapis lazuli blue
            isPlayable = true
        )

        val UR = Faction(
            id = "ur",
            name = "Ур",
            title = "Морська гавань Шумеру",
            ruler = "Ур-Намму",
            patronDeity = "Бог Нанна (місячне сяйво)",
            description = "Багате торгове місто біля гирла Євфрату та Перської затоки. Славне великим Зикуратом Ура та караванами до Дільмуна.",
            bonusDescription = "+25% доходу від торгівлі та водних караванів. Сховища зерна вміщують на 30% більше припасів.",
            bannerColorHex = "#D4AF37", // Royal Sumerian Gold
            isPlayable = true
        )

        val LAGASH = Faction(
            id = "lagash",
            name = "Лагаш",
            title = "Військова твердиня",
            ruler = "Еаннатум (володар Стели шулік)",
            patronDeity = "Бог Нінурта (воїн і буря)",
            description = "Місто мужніх воїнів та перших писаних реформ царя Урукагіни. Володіє важкою фалангою воїнів із бронзовими щитами.",
            bonusDescription = "+20% до сили важкої піхоти фаланги. Воїни мають вищу дисципліну та менші втрати в сутичках.",
            bannerColorHex = "#B71C1C", // Terracotta Crimson
            isPlayable = true
        )

        val KISH = Faction(
            id = "kish",
            name = "Кіш",
            title = "Колиска царів",
            ruler = "Етана / Мебарагесі",
            patronDeity = "Бог Забаба (покровитель битв)",
            description = "Найдавніше царство на півночі Межиріччя. Кожен лугаль (цар), який володів Кішем, вважався верховним правителем усього Межиріччя.",
            bonusDescription = "Високий дипломатичний авторитет: союзи укладаються швидше, лояльність васалів не падає.",
            bannerColorHex = "#6A1B9A", // Royal Purple
            isPlayable = true
        )

        val NIPPUR = Faction(
            id = "nippur",
            name = "Ніппур",
            title = "Священна столиця",
            ruler = "Верховний жрець Енліля",
            patronDeity = "Бог Енліль (володар повітря й вітрів)",
            description = "Релігійне серце Шумеру з великим храмом Екур. Тут коронувалися правителі, сюди стікалися прочани зі всього світу.",
            bonusDescription = "+30% до благословення богів. Повстання племен практично неможливі завдяки священному захисту.",
            bannerColorHex = "#2E7D32", // Sacred Temple Green
            isPlayable = true
        )

        // Non-playable neighbors / rivals
        val UMMA = Faction(
            id = "umma",
            name = "Умма",
            title = "Сусіднє місто-суперник",
            ruler = "Лугальзагесі",
            patronDeity = "Бог Шара",
            description = "Місто, що віками сперечається за родючі поля Ґуедінна.",
            bonusDescription = "Агресивні сусіди з швидкими лучниками.",
            bannerColorHex = "#E65100",
            isPlayable = false
        )

        val NOMADS = Faction(
            id = "martu",
            name = "Кочівники Марту",
            title = "Племена пустелі",
            ruler = "Шейх західних степів",
            patronDeity = "Духи пустельних вітрів",
            description = "Кочові скотарі Сирійської пустелі. Здійснюють несподівані набіги на поля або наймаються за срібло.",
            bonusDescription = "Швидкі вершники на онаграх, стійкі до спеки.",
            bannerColorHex = "#795548",
            isPlayable = false
        )

        val ELAM = Faction(
            id = "elam",
            name = "Елам (Сузи)",
            title = "Гірське царство Загросу",
            ruler = "Цар Аванської династії",
            patronDeity = "Богиня Пінікір",
            description = "Багаті на олово, мідь та будівельний камінь гірські землі на сході.",
            bonusDescription = "Монополія на поставки металів.",
            bannerColorHex = "#00838F",
            isPlayable = false
        )

        val ALL_PLAYABLE = listOf(URUK, UR, LAGASH, KISH, NIPPUR)
        val ALL_FACTIONS = listOf(URUK, UR, LAGASH, KISH, NIPPUR, UMMA, NOMADS, ELAM)

        fun getById(id: String): Faction = ALL_FACTIONS.find { it.id == id } ?: URUK
    }
}

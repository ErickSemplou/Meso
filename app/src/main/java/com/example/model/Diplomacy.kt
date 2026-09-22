package com.example.model

enum class DiplomaticStatus(val label: String, val colorHex: String) {
    WAR("Стан війни", "#D32F2F"),
    HOSTILE("Ворожі стосунки", "#F57C00"),
    NEUTRAL("Нейтралітет", "#757575"),
    PEACE("Мирний договір", "#388E3C"),
    TRADE_PACT("Торговий договір", "#1976D2"),
    ALLIANCE("Братній союз", "#7B1FA2")
}

data class FactionRelation(
    val factionId: String,
    val status: DiplomaticStatus = DiplomaticStatus.NEUTRAL,
    val relationshipScore: Int = 0, // -100 to +100
    val turnsOfPeace: Int = 0,
    val tributePaidByUs: Int = 0,
    val tributePaidToUs: Int = 0
)

data class TradeRoute(
    val id: String,
    val originCityId: String,
    val destinationCityId: String,
    val exportedResource: String, // "Зерно" or "Тканини"
    val importedResource: String, // "Бронза" or "Срібло" or "Лазурит"
    val turnsActive: Int = 0,
    val profitPerTurn: Int = 20
)

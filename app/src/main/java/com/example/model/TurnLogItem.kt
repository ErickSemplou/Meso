package com.example.model

import java.util.UUID

enum class LogCategory(val label: String, val iconEmoji: String) {
    ALL("Усі вісті", "📜"),
    BOT_ACTION("Дії держав (Боти)", "👑"),
    ECONOMIC("Врожай та Економіка", "🌾"),
    DEVELOPMENT("Будівництво та Наука", "🏛️"),
    MILITARY("Військові вісті", "⚔️"),
    HISTORY_FACT("Історична довідка", "💡")
}

data class TurnLogItem(
    val id: String = UUID.randomUUID().toString(),
    val turn: Int,
    val yearBCE: Int,
    val category: LogCategory,
    val title: String,
    val description: String,
    val factionId: String? = null,
    val highlightColorHex: Long = 0xFF8D6E63
)

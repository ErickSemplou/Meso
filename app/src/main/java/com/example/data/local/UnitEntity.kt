package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a military or civil unit type in the Mesopotamian Bronze Age.
 * Stores core tactical attributes: attack, defense, movement speed, upkeep, and historical context.
 */
@Entity(tableName = "units")
data class UnitEntity(
    @PrimaryKey
    val id: String,                    // e.g. "lagash_phalanx", "uruk_spearmen", "kish_chariot"
    val unitTypeId: String,            // "spearmen", "phalanx", "chariot", "archers", "priest", "caravan"
    val name: String,                  // Display name in Ukrainian
    val role: String,                  // e.g. "Важка піхота прориву"
    val factionId: String,             // Faction identifier (e.g. "lagash", "uruk")
    val attack: Int,                   // Attack power (strength)
    val defense: Int,                  // Defense / armor rating
    val movement: Int,                 // Movement speed on the campaign map
    val upkeepGrain: Int,              // Turn grain consumption
    val costGrain: Int,                // Resource cost: Grain
    val costClay: Int,                 // Resource cost: Clay
    val costBronze: Int,               // Resource cost: Bronze
    val costSilver: Int,               // Resource cost: Silver
    val description: String,           // In-game tactical role description
    val historyFact: String,           // 6th-grade historical educational fact
    val iconEmoji: String = "⚔️",      // Emoji representation
    val cityId: String? = null,        // Stationed city ID if applicable
    val count: Int = 1                 // Unit count in this regiment
)

package com.example.model

/**
 * Tactical Battle State and Commander Orders.
 */
data class BattleTactics(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val attackBonusPercent: Int,
    val defenseBonusPercent: Int,
    val wallBreachBonusPercent: Int
) {
    companion object {
        val ALL_TACTICS = listOf(
            BattleTactics(
                id = "shield_wall",
                name = "Шумерська стіна щитів",
                description = "Важка фаланга змикає бронзові щити. Максимальний захист піхоти від лобового удару та стріл.",
                icon = "🛡️",
                attackBonusPercent = 10,
                defenseBonusPercent = 45,
                wallBreachBonusPercent = 0
            ),
            BattleTactics(
                id = "chariot_flank",
                name = "Фланговий удар бойових возів",
                description = "Чотириколісні бойові вози з онаграми проривають ворожі фланги та сіють паніку.",
                icon = "🐎",
                attackBonusPercent = 50,
                defenseBonusPercent = -10,
                wallBreachBonusPercent = 15
            ),
            BattleTactics(
                id = "flaming_arrows",
                name = "Залп запальними стрілами",
                description = "Масований обстріл очеретяними стрілами з бітумом. Дезорганізує ворожі мури та гарнізон.",
                icon = "🏹",
                attackBonusPercent = 25,
                defenseBonusPercent = 15,
                wallBreachBonusPercent = 35
            ),
            BattleTactics(
                id = "night_assault",
                name = "Нічний штурм та підкоп",
                description = "Раптовий штурм при місячному сяйві з драбинами та підпалом дерев'яних воріт.",
                icon = "🗡️",
                attackBonusPercent = 35,
                defenseBonusPercent = 0,
                wallBreachBonusPercent = 50
            )
        )
    }
}

data class ActiveTacticalBattle(
    val attackerCity: City,
    val defenderCity: City,
    val attackerFaction: Faction,
    val defenderFaction: Faction,
    val attackerPower: Int,
    val defenderPower: Int,
    val defenderWallLevel: Int,
    val selectedTacticsId: String = "shield_wall",
    val isResolved: Boolean = false,
    val combatRounds: List<String> = emptyList(),
    val outcomeTitle: String = "",
    val outcomeDetails: String = "",
    val isVictory: Boolean = false,
    val attackerLosses: Int = 0,
    val defenderLosses: Int = 0,
    val silverLooted: Int = 0,
    val grainLooted: Int = 0
)

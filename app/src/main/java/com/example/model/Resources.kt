package com.example.model

data class PlayerResources(
    val grain: Int = 180,       // Зерно (харчі для населення та війська)
    val clay: Int = 140,        // Глина (будівництво споруд, цегла, таблички)
    val bronze: Int = 40,       // Бронза (зброя, шоломи, лемеші плугів)
    val silver: Int = 200,      // Срібло (скарбниця, платня воїнам, торгівля)
    val loyalty: Int = 85,      // Лояльність племен та містян (0 - 100%)
    val piety: Int = 50,        // Благословення богів / Культура
    val maxGrainStorage: Int = 400
) {
    fun add(other: PlayerResources): PlayerResources {
        return copy(
            grain = (grain + other.grain).coerceIn(0, maxGrainStorage),
            clay = (clay + other.clay).coerceAtLeast(0),
            bronze = (bronze + other.bronze).coerceAtLeast(0),
            silver = (silver + other.silver).coerceAtLeast(0),
            loyalty = (loyalty + other.loyalty).coerceIn(0, 100),
            piety = (piety + other.piety).coerceIn(0, 100)
        )
    }

    fun canAfford(cost: ResourceCost): Boolean {
        return grain >= cost.grain &&
                clay >= cost.clay &&
                bronze >= cost.bronze &&
                silver >= cost.silver
    }

    fun subtract(cost: ResourceCost): PlayerResources {
        return copy(
            grain = (grain - cost.grain).coerceAtLeast(0),
            clay = (clay - cost.clay).coerceAtLeast(0),
            bronze = (bronze - cost.bronze).coerceAtLeast(0),
            silver = (silver - cost.silver).coerceAtLeast(0)
        )
    }
}

data class ResourceCost(
    val grain: Int = 0,
    val clay: Int = 0,
    val bronze: Int = 0,
    val silver: Int = 0
)

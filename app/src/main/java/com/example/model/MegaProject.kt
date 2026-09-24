package com.example.model

data class MegaProject(
    val id: String,
    val name: String,
    val subtitle: String,
    val description: String,
    val historicalFact: String,
    val stage: Int = 1, // 1, 2, 3 (3 = Completed)
    val maxStages: Int = 3,
    val grainSpent: Int = 0,
    val grainNeeded: Int = 200,
    val claySpent: Int = 0,
    val clayNeeded: Int = 200,
    val silverSpent: Int = 0,
    val silverNeeded: Int = 150,
    val builderFactionId: String,
    val cityId: String,
    val completedEffectDescription: String,
    val isCompleted: Boolean = false
) {
    val progressPercent: Float
        get() {
            if (isCompleted) return 1f
            val totalSpent = grainSpent + claySpent + silverSpent
            val totalNeeded = grainNeeded + clayNeeded + silverNeeded
            if (totalNeeded == 0) return 1f
            val basePercent = (totalSpent.toFloat() / totalNeeded.toFloat()).coerceIn(0f, 1f)
            val stageOffset = (stage - 1) / maxStages.toFloat()
            return (stageOffset + basePercent / maxStages.toFloat()).coerceIn(0f, 1f)
        }

    companion object {
        val ALL_WONDERS = listOf(
            MegaProject(
                id = "wonder_ziggurat_nippur",
                name = "Великий Зикурат Екур у Ніппурі",
                subtitle = "Священна вежа Енліля",
                description = "Семиступінчаста башта-зикурат, що сягає небес. Символ єдності та релігійної величі всього Шумеру.",
                historicalFact = "Храм Екур у Ніппурі вважався релігійним серцем Шумеру. Зикурати виступали мостом між землею і богами.",
                grainNeeded = 250,
                clayNeeded = 300,
                silverNeeded = 200,
                builderFactionId = "nippur",
                cityId = "nippur",
                completedEffectDescription = "+30 Побожності, +15% до прибутків усіх міст та захист від повстань."
            ),
            MegaProject(
                id = "wonder_wall_uruk",
                name = "Величні Мури Гільгамеша у Уруці",
                subtitle = "Неприступне кільце з паленої цегли",
                description = "Подвійне кільце велетенських мурів завдовжки 9 км з 900 баштами, зведене епічним царем Гільгамешем.",
                historicalFact = "Мури Урука згадуються в перших рядках 'Епосу про Гільгамеша' як головне рукотворне диво Шумеру.",
                grainNeeded = 300,
                clayNeeded = 400,
                silverNeeded = 180,
                builderFactionId = "uruk",
                cityId = "uruk",
                completedEffectDescription = "+40 Оборони для всіх міст, +20 Лояльності простолюду."
            ),
            MegaProject(
                id = "wonder_canal_system",
                name = "Великий Великий Канал Євфрату",
                subtitle = "Зрошувальне диво Месопотамії",
                description = "Грандіозна мережа магістральних каналів із шлюзами, що сполучає Євфрат і Тигр через усьому Шумері.",
                historicalFact = "Будівництво загат і каналів вимагало праці тисяч людей і перетворило пустелю на найродючішу долину світу.",
                grainNeeded = 400,
                clayNeeded = 250,
                silverNeeded = 150,
                builderFactionId = "ur",
                cityId = "ur",
                completedEffectDescription = "+35% до збору зерна та приплив населення в усі міста."
            )
        )
    }
}

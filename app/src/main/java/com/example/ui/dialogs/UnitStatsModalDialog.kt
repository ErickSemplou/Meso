package com.example.ui.dialogs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.local.UnitEntity
import com.example.model.Faction
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.LapisLazuli
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

@Composable
fun UnitStatsModalDialog(
    unit: UnitEntity,
    faction: Faction,
    availableFactionUnits: List<UnitEntity> = emptyList(),
    cityName: String? = null,
    regimentCount: Int = 1,
    onSelectUnit: (UnitEntity) -> Unit = {},
    onDismiss: () -> Unit
) {
    val attackAnim = remember { Animatable(0f) }
    val defenseAnim = remember { Animatable(0f) }
    val movementAnim = remember { Animatable(0f) }

    LaunchedEffect(unit.id) {
        attackAnim.snapTo(0f)
        defenseAnim.snapTo(0f)
        movementAnim.snapTo(0f)
        attackAnim.animateTo((unit.attack / 50f).coerceIn(0f, 1f), tween(550))
        defenseAnim.animateTo((unit.defense / 50f).coerceIn(0f, 1f), tween(550))
        movementAnim.animateTo((unit.movement / 4f).coerceIn(0f, 1f), tween(550))
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = AncientParchmentLight),
            border = BorderStroke(2.dp, SumerianGold),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("unit_stats_modal_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. Faction Header Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    faction.bannerColor.copy(alpha = 0.95f),
                                    BronzeDark
                                )
                            )
                        )
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Faction Crest
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(AncientParchmentDark)
                                    .border(1.5.dp, SumerianGold, CircleShape)
                            ) {
                                Text(
                                    text = faction.name.take(1),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = SumerianGold
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = faction.name.uppercase(),
                                    color = SumerianGoldBright,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Правитель: ${faction.ruler} • ${faction.patronDeity}",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 10.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .testTag("close_unit_stats_modal")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Закрити",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // 2. Unit Identity & Stationing Card
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Large Unit Emblem Box
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    Brush.radialGradient(
                                        listOf(AncientParchmentDark, Color(0xFF2D1E10))
                                    )
                                )
                                .border(2.dp, SumerianGold, RoundedCornerShape(14.dp))
                        ) {
                            Text(
                                text = unit.iconEmoji,
                                fontSize = 32.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = unit.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark,
                                lineHeight = 19.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = SumerianGold.copy(alpha = 0.25f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, SumerianGold)
                                ) {
                                    Text(
                                        text = unit.role,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BronzeDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                if (cityName != null) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        color = AncientParchmentDark,
                                        shape = RoundedCornerShape(6.dp),
                                        border = BorderStroke(1.dp, BronzeDark.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = "📍 $cityName ($regimentCount)",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF4E342E),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. CORE DETAILED STATS (Attack, Defense, Movement)
                    Surface(
                        color = AncientParchmentDark.copy(alpha = 0.85f),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.5.dp, SumerianGold.copy(alpha = 0.7f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "⚔️ БОЙОВІ ХАРАКТЕРИСТИКИ ПІДРОЗДІЛУ",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark,
                                letterSpacing = 0.5.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // ATTACK STAT ROW
                            StatProgressRow(
                                title = "Атака (Штурм)",
                                icon = "⚔️",
                                value = unit.attack,
                                maxValue = 50,
                                progress = attackAnim.value,
                                barColor = TerracottaRed,
                                description = when {
                                    unit.attack >= 35 -> "Ударна міць прориву ворожих ліній"
                                    unit.attack >= 20 -> "Збалансований штурмовий натиск"
                                    else -> "Підтримка та стримування"
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // DEFENSE STAT ROW
                            StatProgressRow(
                                title = "Захист (Броня)",
                                icon = "🛡️",
                                value = unit.defense,
                                maxValue = 50,
                                progress = defenseAnim.value,
                                barColor = LapisLazuli,
                                description = when {
                                    unit.defense >= 35 -> "Бронзові шоломи та важкі дерев'яні щити"
                                    unit.defense >= 20 -> "Середній рівень захисту ополчення"
                                    else -> "Легкий стрій, вразливий до штурму"
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            // MOVEMENT STAT ROW
                            StatProgressRow(
                                title = "Швидкість (Рух)",
                                icon = "🏃",
                                value = unit.movement,
                                maxValue = 4,
                                progress = movementAnim.value,
                                barColor = Color(0xFF2E7D32),
                                description = when (unit.movement) {
                                    3 -> "Швидкісний марш рівнинами та дорогами"
                                    2 -> "Стандартна піша швидкість походу"
                                    else -> "Важкий неквапливий стрій фаланги"
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 4. Logistics, Upkeep and Costs Grid
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Upkeep Card
                        Surface(
                            color = AncientParchmentLight,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SumerianGold),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🌾", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Утримання",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BronzeDark
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "-${unit.upkeepGrain} зерна/хід",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = TerracottaRed
                                )
                            }
                        }

                        // Recruitment Cost Card
                        Surface(
                            color = AncientParchmentLight,
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, SumerianGold),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🪙 Вартість підготовки",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BronzeDark
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (unit.costGrain > 0) Text(text = "🌾 ${unit.costGrain}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                                    if (unit.costBronze > 0) Text(text = "🛡️ ${unit.costBronze}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                                    if (unit.costSilver > 0) Text(text = "🪙 ${unit.costSilver}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. Tactical Description Card
                    Surface(
                        color = AncientParchment,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = unit.description,
                            fontSize = 11.sp,
                            color = BronzeDark,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 6. Educational History Fact (6th Grade History)
                    Surface(
                        color = SumerianGold.copy(alpha = 0.18f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.5.dp, SumerianGold),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "📜", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "ІСТОРИЧНЕ ДЖЕРЕЛО (6 КЛАС)",
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF5D4037),
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = unit.historyFact,
                                    fontSize = 11.sp,
                                    color = BronzeDark,
                                    lineHeight = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // 7. Other Units in Faction's Roster (Switchers)
                    if (availableFactionUnits.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "АРМІЙСЬКИЙ СПИСОК (${faction.name}):",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF5D4037)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            availableFactionUnits.forEach { otherUnit ->
                                val isCurrent = otherUnit.id == unit.id
                                Surface(
                                    color = if (isCurrent) SumerianGold else AncientParchmentLight,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(
                                        if (isCurrent) 1.5.dp else 1.dp,
                                        if (isCurrent) BronzeDark else BronzeDark.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onSelectUnit(otherUnit) }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp)
                                    ) {
                                        Text(text = otherUnit.iconEmoji, fontSize = 13.sp)
                                        Text(
                                            text = otherUnit.role.split(" ").firstOrNull() ?: otherUnit.name,
                                            fontSize = 8.sp,
                                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Bold,
                                            color = BronzeDark,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 8. Room Database Persistence Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null,
                            tint = Color(0xFF795548),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Room DB: сутність UnitEntity / таблиця 'units'",
                            fontSize = 9.sp,
                            color = Color(0xFF795548),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 9. Close Action Button
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BronzeDark,
                            contentColor = SumerianGoldBright
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("dismiss_unit_modal_button")
                    ) {
                        Text(
                            text = "Зрозуміло",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatProgressRow(
    title: String,
    icon: String,
    value: Int,
    maxValue: Int,
    progress: Float,
    barColor: Color,
    description: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzeDark
                )
            }

            Text(
                text = "$value",
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = barColor
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        LinearProgressIndicator(
            progress = { progress },
            color = barColor,
            trackColor = AncientParchmentLight,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = description,
            fontSize = 9.sp,
            color = Color(0xFF6D4C41),
            lineHeight = 11.sp
        )
    }
}

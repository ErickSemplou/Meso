package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Fort
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameState
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun RomeTopBar(
    state: GameState,
    isMusicMuted: Boolean,
    onToggleMusic: () -> Unit,
    onOpenCodex: () -> Unit,
    onOpenChronicle: () -> Unit,
    onOpenTurnLogs: () -> Unit,
    onOpenFactionsOverview: () -> Unit = {},
    onOpenMegaProjects: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Surface(
        color = AncientParchmentDark,
        shadowElevation = 4.dp,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxWidth()
        ) {
            // Left: Faction Banner & Leader
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(state.playerFaction.bannerColor)
                        .border(1.5.dp, SumerianGold, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.playerFaction.name.take(1),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${state.playerFaction.name} (${state.playerFaction.ruler})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = if (state.turnsRemaining <= 10) TerracottaRed.copy(alpha = 0.2f) else SumerianGold.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (state.turnsRemaining <= 10) TerracottaRed else BronzeDark)
                        ) {
                            Text(
                                text = "Хід ${state.turn}/${state.maxTurns}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = if (state.turnsRemaining <= 10) TerracottaRed else BronzeDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "${state.yearBCE} р. до н.е. • ${state.seasonName} • Залишилось: ${state.turnsRemaining} х.",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6D4C41)
                    )
                }
            }

            // Center: Strategic Resources with Dynamic Income/Turn indicators
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val grainDelta = state.netIncomeGrain
                ResourcePill(
                    label = "Зерно",
                    value = "${state.resources.grain}",
                    delta = if (grainDelta >= 0) "+$grainDelta" else "$grainDelta",
                    emoji = "🌾",
                    textColor = if (state.resources.grain < 30) TerracottaRed else BronzeDark
                )
                val clayDelta = state.netIncomeClay
                ResourcePill(
                    label = "Глина",
                    value = "${state.resources.clay}",
                    delta = "+$clayDelta",
                    emoji = "🧱"
                )
                val bronzeDelta = state.netIncomeBronze
                ResourcePill(
                    label = "Бронза",
                    value = "${state.resources.bronze}",
                    delta = "+$bronzeDelta",
                    emoji = "🛡"
                )
                val silverDelta = state.netIncomeSilver
                ResourcePill(
                    label = "Срібло",
                    value = "${state.resources.silver}",
                    delta = "+$silverDelta",
                    emoji = "🪙",
                    textColor = SumerianGold
                )
                ResourcePill(
                    label = "Лояльність",
                    value = "${state.resources.loyalty}%",
                    delta = null,
                    emoji = "🕊",
                    textColor = if (state.resources.loyalty < 50) TerracottaRed else Color(0xFF2E7D32)
                )
            }

            // Right: Utility controls (Music, Codex, Chronicle)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onToggleMusic,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("toggle_music_button")
                ) {
                    Icon(
                        imageVector = if (isMusicMuted) Icons.Default.MusicOff else Icons.Default.MusicNote,
                        contentDescription = if (isMusicMuted) "Увімкнути музику" else "Вимкнути музику",
                        tint = if (isMusicMuted) Color.Gray else BronzePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenFactionsOverview,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("open_factions_overview_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = "Перебіг розвитку держав",
                        tint = BronzePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenMegaProjects,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("open_megaprojects_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Fort,
                        contentDescription = "Дива Світу",
                        tint = SumerianGold,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenTurnLogs,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("open_turn_logs_button")
                ) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Notifications,
                        contentDescription = "Вісті ходу",
                        tint = SumerianGold,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenCodex,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("open_codex_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                        contentDescription = "Енциклопедія Месопотамії",
                        tint = BronzePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onOpenChronicle,
                    modifier = Modifier
                        .size(34.dp)
                        .testTag("open_chronicle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = "Царський літопис",
                        tint = BronzePrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResourcePill(
    label: String,
    value: String,
    delta: String?,
    emoji: String,
    textColor: Color = BronzeDark
) {
    Surface(
        color = AncientParchmentLight,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.35f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(text = emoji, fontSize = 11.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = value,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            if (delta != null) {
                Spacer(modifier = Modifier.width(2.dp))
                val isNegative = delta.startsWith("-")
                Text(
                    text = "($delta)",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isNegative) TerracottaRed else Color(0xFF2E7D32)
                )
            }
        }
    }
}

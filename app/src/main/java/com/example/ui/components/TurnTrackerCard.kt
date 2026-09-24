package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun TurnTrackerCard(
    state: GameState,
    onOpenTurnLogs: () -> Unit,
    onEndTurn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = (state.turn.toFloat() / state.maxTurns.toFloat()).coerceIn(0f, 1f)
    val isCritical = state.turnsRemaining <= 10

    val badgeColor by animateColorAsState(
        targetValue = if (isCritical) TerracottaRed else SumerianGold,
        animationSpec = tween(500),
        label = "turnBadgeColor"
    )

    Surface(
        color = AncientParchmentDark.copy(alpha = 0.95f),
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, badgeColor),
        shadowElevation = 8.dp,
        modifier = modifier
            .testTag("turn_tracker_card")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Turn Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(badgeColor)
                            .border(1.dp, BronzeDark, CircleShape)
                    ) {
                        Text(
                            text = "${state.turn}",
                            color = BronzeDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Хід ${state.turn} / ${state.maxTurns}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (isCritical) TerracottaRed.copy(alpha = 0.2f) else SumerianGold.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isCritical) TerracottaRed else BronzeDark)
                            ) {
                                Text(
                                    text = "${state.yearBCE} р. до н.е.",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCritical) TerracottaRed else BronzeDark,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }

                        Text(
                            text = "Сезон: ${state.seasonName} • Залишилось: ${state.turnsRemaining} ходів",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                // Log Pill / Quick Turn Trigger
                if (state.lastTurnLogs.isNotEmpty()) {
                    Surface(
                        color = AncientParchmentLight,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                        modifier = Modifier
                            .clickable { onOpenTurnLogs() }
                            .testTag("turn_tracker_log_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Вісті ходу",
                                tint = SumerianGold,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Вісті (${state.lastTurnLogs.size})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            // Linear Turn Progress Indicator
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = badgeColor,
                trackColor = BronzeDark.copy(alpha = 0.2f),
            )
        }
    }
}

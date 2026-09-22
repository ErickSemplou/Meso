package com.example.ui.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.model.GameState
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

@Composable
fun CampaignGameOverDialog(
    state: GameState,
    onRestartCampaign: () -> Unit,
    onReturnToMainMenu: () -> Unit
) {
    val controlledCount = state.playerCities.size
    val isVictory = state.isVictory || (state.turn >= state.maxTurns && controlledCount >= 3)
    val isTotalDefeat = controlledCount == 0

    val historicalTitle = when {
        controlledCount >= 6 -> "«Лугаль-калем-ма» (Цар Країни та Всіх Земель)"
        controlledCount >= 4 -> "«Лугаль Кіша» (Великий Гегемон Шумеру)"
        controlledCount >= 2 -> "«Енсі» (Священний Правитель-Будівничий)"
        isTotalDefeat -> "«Зруйнований Трон»"
        else -> "«Випробуваний Часом»"
    }

    val historyGrade = when {
        state.correctQuizCount >= 15 -> "Відмінно (12 балів) ⭐⭐⭐"
        state.correctQuizCount >= 10 -> "Добре (10 балів) ⭐⭐"
        state.correctQuizCount >= 5 -> "Задовільно (7 балів) ⭐"
        else -> "Початківець (Спробуйте ще!)"
    }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    ) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.5.dp, if (isVictory) SumerianGold else BronzeDark),
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(8.dp)
                .testTag("game_over_dialog")
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Banner Image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = if (isVictory) R.drawable.img_sumerian_cities else R.drawable.img_sumerian_army),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = SumerianGoldBright,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isVictory) "ТРІУМФ ШУМЕРСЬКОЇ ДЕРЖАВИ!" else if (isTotalDefeat) "ПАДІННЯ ЦАРСТВА" else "ПІДСУМОК 50 ХОДІВ ПРАВЛІННЯ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = SumerianGoldBright,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title Awarded
                Text(
                    text = historicalTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = BronzeDark,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = state.gameOverReason ?: if (isVictory) {
                        "Ваше ім'я записано золотим клинописом на скрижалях історії! Народ славить правління ${state.playerFaction.ruler}."
                    } else {
                        "Епоха 50 ходів (2600-2350 рр. до н.е.) завершилася. Ваше місто вистояло крізь повені, війни та століття."
                    },
                    fontSize = 11.sp,
                    color = ClaySlate,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GameOverStatCard(
                        title = "Міст під владою",
                        value = "$controlledCount з ${state.cities.size}",
                        emoji = "🏛️",
                        modifier = Modifier.weight(1f)
                    )
                    GameOverStatCard(
                        title = "Населення імперії",
                        value = "${state.totalControlledPopulation}",
                        emoji = "👥",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GameOverStatCard(
                        title = "Технологій відкрито",
                        value = "${state.researchedTechIds.size} / 8",
                        emoji = "📜",
                        modifier = Modifier.weight(1f)
                    )
                    GameOverStatCard(
                        title = "Вікторина (6 клас)",
                        value = "${state.correctQuizCount} прав.",
                        emoji = "🧠",
                        subtitle = historyGrade,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onRestartCampaign,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SumerianGold,
                            contentColor = ClaySlate
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("game_over_restart_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = BronzeDark, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("НОВА КАМПАНІЯ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }

                    Button(
                        onClick = onReturnToMainMenu,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BronzePrimary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SumerianGold),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .testTag("game_over_menu_button")
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = SumerianGold, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ГОЛОВНЕ МЕНЮ", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun GameOverStatCard(
    title: String,
    value: String,
    emoji: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AncientParchment),
        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = emoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = title, fontSize = 9.sp, color = BronzeDark, fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = BronzeDark
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 8.sp,
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

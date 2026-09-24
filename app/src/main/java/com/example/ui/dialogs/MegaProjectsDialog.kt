package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fort
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GameState
import com.example.model.MegaProject
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold

@Composable
fun MegaProjectsDialog(
    gameState: GameState,
    onClose: () -> Unit,
    onContribute: (wonderId: String, grain: Int, clay: Int, silver: Int) -> Unit
) {
    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = AncientParchmentLight,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.88f)
                .testTag("megaprojects_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Top Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = SumerianGold,
                            shape = CircleShape,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Fort,
                                    contentDescription = null,
                                    tint = BronzeDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Дива Бронзової Доби",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark
                            )
                            Text(
                                text = "Монументальні монументи та споруди Шумеру",
                                fontSize = 11.sp,
                                color = Color(0xFF6D4C41)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_megaprojects_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрити",
                            tint = BronzeDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // List of Megaprojects
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(gameState.wonders, key = { it.id }) { wonder ->
                        MegaProjectCard(
                            wonder = wonder,
                            playerGrain = gameState.resources.grain,
                            playerClay = gameState.resources.clay,
                            playerSilver = gameState.resources.silver,
                            onContributeGrain = { onContribute(wonder.id, 50, 0, 0) },
                            onContributeClay = { onContribute(wonder.id, 0, 50, 0) },
                            onContributeSilver = { onContribute(wonder.id, 0, 0, 30) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onClose,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SumerianGold,
                        contentColor = ClaySlate
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Text(
                        text = "ПОВЕРНУТИСЯ ДО УПРАВЛІННЯ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )
                }
            }
        }
    }
}

@Composable
private fun MegaProjectCard(
    wonder: MegaProject,
    playerGrain: Int,
    playerClay: Int,
    playerSilver: Int,
    onContributeGrain: () -> Unit,
    onContributeClay: () -> Unit,
    onContributeSilver: () -> Unit
) {
    Surface(
        color = AncientParchment,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
        shadowElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = wonder.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = BronzeDark
                    )
                    Text(
                        text = wonder.subtitle,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D4C41)
                    )
                }

                Surface(
                    color = if (wonder.isCompleted) Color(0xFF2E7D32) else SumerianGold,
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (wonder.isCompleted) "🏛️ ЗБУДОВАНО!" else "Етап ${wonder.stage} з ${wonder.maxStages}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (wonder.isCompleted) Color.White else BronzeDark,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = wonder.description,
                fontSize = 11.sp,
                color = Color(0xFF4E342E)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Historical Lore Box
            Surface(
                color = AncientParchmentDark,
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "💡", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = wonder.historicalFact,
                        fontSize = 9.sp,
                        color = Color(0xFF3E2723),
                        lineHeight = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Прогрес дива:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzeDark
                )
                Text(
                    text = "${(wonder.progressPercent * 100).toInt()}%",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzeDark
                )
            }
            Spacer(modifier = Modifier.height(3.dp))
            LinearProgressIndicator(
                progress = wonder.progressPercent,
                color = SumerianGold,
                trackColor = AncientParchmentDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Bonus effect
            Text(
                text = "Ефект після завершення: ${wonder.completedEffectDescription}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B5E20)
            )

            // Contribution Action Buttons if not completed
            if (!wonder.isCompleted) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = onContributeGrain,
                        enabled = playerGrain >= 50,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2E7D32),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+50 🌾", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onContributeClay,
                        enabled = playerClay >= 50,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8D6E63),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+50 🧱", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onContributeSilver,
                        enabled = playerSilver >= 30,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SumerianGold,
                            contentColor = BronzeDark
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("+30 🪙", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

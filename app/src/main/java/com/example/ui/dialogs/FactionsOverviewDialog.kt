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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
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
import com.example.model.DiplomaticStatus
import com.example.model.Faction
import com.example.model.GameState
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold

@Composable
fun FactionsOverviewDialog(
    gameState: GameState,
    onClose: () -> Unit,
    onOpenDiplomacy: () -> Unit
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
                .testTag("factions_overview_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Top Header Bar
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
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = BronzeDark,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Перебіг Розвитку Держав",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark
                            )
                            Text(
                                text = "Геополітика, військова міць та дива Межиріччя",
                                fontSize = 11.sp,
                                color = Color(0xFF6D4C41)
                            )
                        }
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.testTag("close_factions_overview_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрити",
                            tint = BronzeDark
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Factions Ranking List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(Faction.ALL_FACTIONS) { faction ->
                        val factionCities = gameState.cities.filter { it.factionId == faction.id }
                        val totalPower = factionCities.sumOf { it.totalMilitaryPower }
                        val totalPop = factionCities.sumOf { it.population }
                        val isPlayer = faction.id == gameState.playerFactionId
                        val relation = gameState.relations[faction.id]
                        val wonder = gameState.wonders.find { it.builderFactionId == faction.id }

                        FactionStatusCard(
                            faction = faction,
                            citiesCount = factionCities.size,
                            totalPower = totalPower,
                            totalPopulation = totalPop,
                            isPlayer = isPlayer,
                            relationStatus = relation?.status ?: DiplomaticStatus.NEUTRAL,
                            wonderName = wonder?.name,
                            wonderProgress = wonder?.progressPercent ?: 0f
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onClose()
                            onOpenDiplomacy()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AncientParchmentDark,
                            contentColor = BronzeDark
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ДИПЛОМАТІЯ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SumerianGold,
                            contentColor = ClaySlate
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ПОВЕРНУТИСЯ ДО КАРТИ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                    }
                }
            }
        }
    }
}

@Composable
private fun FactionStatusCard(
    faction: Faction,
    citiesCount: Int,
    totalPower: Int,
    totalPopulation: Int,
    isPlayer: Boolean,
    relationStatus: DiplomaticStatus,
    wonderName: String?,
    wonderProgress: Float
) {
    Surface(
        color = AncientParchment,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            if (isPlayer) 2.dp else 1.dp,
            if (isPlayer) SumerianGold else BronzeDark.copy(alpha = 0.4f)
        ),
        shadowElevation = if (isPlayer) 6.dp else 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Header Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Faction Banner Badge
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(faction.bannerColor)
                            .border(1.5.dp, SumerianGold, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = faction.name.take(1),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = faction.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                            if (isPlayer) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = SumerianGold,
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "ВАША ДЕРЖАВА",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BronzeDark,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Правитель: ${faction.ruler} • ${faction.patronDeity}",
                            fontSize = 9.sp,
                            color = Color(0xFF5D4037)
                        )
                    }
                }

                // Diplomatic Stance Badge
                if (!isPlayer) {
                    Surface(
                        color = when (relationStatus) {
                            DiplomaticStatus.ALLIANCE, DiplomaticStatus.TRADE_PACT -> Color(0xFF1B5E20)
                            DiplomaticStatus.HOSTILE, DiplomaticStatus.WAR -> Color(0xFFB71C1C)
                            else -> Color(0xFFE65100)
                        },
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = relationStatus.label,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stats Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatChip(icon = "🏰", label = "Міста", value = "$citiesCount")
                StatChip(icon = "👥", label = "Населення", value = "${totalPopulation / 1000}k")
                StatChip(icon = "⚔️", label = "Військова сила", value = "$totalPower")
            }

            // Wonder Progress if applicable
            if (wonderName != null && wonderProgress > 0f) {
                Spacer(modifier = Modifier.height(6.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "🏛️ Будівництво дива: $wonderName",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BronzeDark
                        )
                        Text(
                            text = "${(wonderProgress * 100).toInt()}%",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    LinearProgressIndicator(
                        progress = wonderProgress,
                        color = SumerianGold,
                        trackColor = AncientParchmentDark,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun StatChip(icon: String, label: String, value: String) {
    Surface(
        color = AncientParchmentDark,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Text(text = icon, fontSize = 10.sp)
            Spacer(modifier = Modifier.width(3.dp))
            Text(text = "$label: ", fontSize = 9.sp, color = Color(0xFF6D4C41))
            Text(text = value, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
        }
    }
}

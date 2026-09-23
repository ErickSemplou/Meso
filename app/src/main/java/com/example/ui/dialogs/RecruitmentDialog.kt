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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.City
import com.example.model.PlayerResources
import com.example.model.UnitType
import com.example.ui.components.UnitAvatar
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun RecruitmentDialog(
    city: City,
    resources: PlayerResources,
    unlockedTechIds: Set<String>,
    onRecruitUnit: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(6.dp)
                .testTag("recruitment_dialog")
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Header Banner with Ancient Sumerian Army Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sumerian_army),
                        contentDescription = "Шумерське військо",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Black.copy(alpha = 0.4f))
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech,
                                contentDescription = "Найм війська",
                                tint = SumerianGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Царські казарми: ${city.name}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SumerianGold
                                )
                                Text(
                                    text = "Гарнізон: ${city.garrison.values.sum()} полків • Оборона: ${city.defenseRating}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_recruitment_dialog")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    items(UnitType.ALL_UNITS) { unit ->
                        val canAfford = resources.canAfford(unit.cost)
                        val isTechLocked = when (unit.id) {
                            "chariot" -> !unlockedTechIds.contains("wheel")
                            "phalanx" -> !unlockedTechIds.contains("bronze_foundry_tech")
                            else -> false
                        }

                        val unitEmoji = when (unit.id) {
                            "spearmen" -> "🛡️"
                            "phalanx" -> "⚔️"
                            "chariot" -> "🛞"
                            "archers" -> "🏹"
                            "priest" -> "🕊️"
                            "caravan" -> "🐫"
                            else -> "⚔️"
                        }

                        Surface(
                            color = if (isTechLocked) Color(0xFFE0E0E0).copy(alpha = 0.6f) else AncientParchment,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        UnitAvatar(
                                            unitId = unit.id,
                                            size = 38.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = unit.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = BronzeDark
                                            )
                                            Text(
                                                text = "${unit.role} • Сила: ⚔️${unit.strength} • Броня: 🛡️${unit.defense}",
                                                fontSize = 9.sp,
                                                color = Color(0xFF6D4C41),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }

                                    if (isTechLocked) {
                                        Text(
                                            text = "🔒 Потрібна технологія",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TerracottaRed
                                        )
                                    } else {
                                        Button(
                                            onClick = { onRecruitUnit(unit.id) },
                                            enabled = canAfford,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = BronzePrimary,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.testTag("recruit_button_${unit.id}")
                                        ) {
                                            Text(
                                                text = "Найняти",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = unit.description,
                                    fontSize = 9.sp,
                                    color = BronzeDark,
                                    lineHeight = 12.sp
                                )

                                Text(
                                    text = "💡 ${unit.historyFact}",
                                    fontSize = 8.sp,
                                    color = Color(0xFF4E342E),
                                    lineHeight = 11.sp,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (unit.cost.grain > 0) Text("🌾 ${unit.cost.grain}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (unit.cost.clay > 0) Text("🧱 ${unit.cost.clay}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (unit.cost.bronze > 0) Text("🛡 ${unit.cost.bronze}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (unit.cost.silver > 0) Text("🪙 ${unit.cost.silver}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    Text("• 🌾 -${unit.upkeepGrain}/хід", fontSize = 8.sp, color = TerracottaRed, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

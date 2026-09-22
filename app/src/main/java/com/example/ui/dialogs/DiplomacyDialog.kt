package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Handshake
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.DiplomaticStatus
import com.example.model.Faction
import com.example.model.FactionRelation
import com.example.model.PlayerResources
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun DiplomacyDialog(
    playerFactionId: String,
    relations: Map<String, FactionRelation>,
    resources: PlayerResources,
    onSendGift: (String) -> Unit,
    onProposePeace: (String) -> Unit,
    onProposeTrade: (String) -> Unit,
    onDemandTribute: (String) -> Unit,
    onDeclareWar: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val rivalFactions = Faction.ALL_FACTIONS.filter { it.id != playerFactionId }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .testTag("diplomacy_dialog")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Handshake,
                            contentDescription = "Дипломатія",
                            tint = BronzeDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Посольська палата Межиріччя",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_diplomacy_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                ) {
                    items(rivalFactions) { faction ->
                        val rel = relations[faction.id] ?: FactionRelation(faction.id)
                        val statusColor = when (rel.status) {
                            DiplomaticStatus.WAR -> TerracottaRed
                            DiplomaticStatus.HOSTILE -> Color(0xFFE65100)
                            DiplomaticStatus.PEACE -> Color(0xFF2E7D32)
                            DiplomaticStatus.TRADE_PACT -> Color(0xFF1565C0)
                            DiplomaticStatus.ALLIANCE -> SumerianGold
                            else -> Color.Gray
                        }

                        Surface(
                            color = AncientParchment,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, faction.bannerColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(faction.bannerColor)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${faction.name} (${faction.ruler})",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = BronzeDark
                                        )
                                        Text(
                                            text = "Культ: ${faction.patronDeity}",
                                            fontSize = 10.sp,
                                            color = Color(0xFF6D4C41)
                                        )
                                    }

                                    Surface(
                                        color = statusColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor)
                                    ) {
                                        Text(
                                            text = rel.status.label,
                                            color = statusColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = faction.description,
                                    fontSize = 10.sp,
                                    color = BronzeDark,
                                    lineHeight = 13.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Diplomatic action buttons
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Button(
                                        onClick = { onSendGift(faction.id) },
                                        enabled = resources.silver >= 50,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SumerianGold,
                                            contentColor = BronzeDark
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Дари (50 🪙)", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }

                                    if (rel.status == DiplomaticStatus.WAR || rel.status == DiplomaticStatus.HOSTILE) {
                                        Button(
                                            onClick = { onProposePeace(faction.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF2E7D32),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Мир", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    } else {
                                        Button(
                                            onClick = { onProposeTrade(faction.id) },
                                            enabled = rel.status != DiplomaticStatus.TRADE_PACT,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF1565C0),
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Торгівля", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }

                                    Button(
                                        onClick = { onDemandTribute(faction.id) },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = BronzePrimary,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Данина", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                    }

                                    if (rel.status != DiplomaticStatus.WAR) {
                                        Button(
                                            onClick = { onDeclareWar(faction.id) },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = TerracottaRed,
                                                contentColor = Color.White
                                            ),
                                            shape = RoundedCornerShape(4.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("Війна", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

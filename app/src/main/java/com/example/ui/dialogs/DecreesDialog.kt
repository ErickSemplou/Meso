package com.example.ui.dialogs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.model.PlayerResources
import com.example.model.RoyalDecree
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun DecreesDialog(
    activeDecreeIds: Set<String>,
    resources: PlayerResources,
    onEnactDecree: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 18.dp,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .padding(6.dp)
                .testTag("decrees_dialog")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = SumerianGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ЦАРСЬКІ ЗАКОНИ ТА РЕФОРМИ",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = BronzeDark
                            )
                            Text(
                                text = "Прадавні правові кодекси та едикти Месопотамії",
                                fontSize = 9.sp,
                                color = ClaySlate
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // List of Royal Decrees
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .height(380.dp)
                ) {
                    items(RoyalDecree.ALL_DECREES) { decree ->
                        val isEnacted = activeDecreeIds.contains(decree.id)
                        val canAfford = resources.canAfford(decree.cost)

                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isEnacted) AncientParchmentDark else AncientParchment
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (isEnacted) 2.dp else 1.dp,
                                color = if (isEnacted) SumerianGold else BronzeDark.copy(alpha = 0.4f)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = decree.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BronzeDark
                                        )
                                        Text(
                                            text = "Автор: ${decree.kingOrOrigin} • ${decree.epoch}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = BronzePrimary
                                        )
                                    }

                                    if (isEnacted) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .background(Color(0xFF2E7D32).copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = Color(0xFF2E7D32),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(3.dp))
                                            Text(
                                                text = "ДІЄ В ДЕРЖАВІ",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF2E7D32)
                                            )
                                        }
                                    } else {
                                        Button(
                                            onClick = { onEnactDecree(decree.id) },
                                            enabled = canAfford,
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = SumerianGold,
                                                contentColor = ClaySlate,
                                                disabledContainerColor = Color(0xFFD7CCC8)
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            modifier = Modifier.height(34.dp)
                                        ) {
                                            Text(
                                                text = "Видати указ",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = decree.description,
                                    fontSize = 10.sp,
                                    color = ClaySlate,
                                    lineHeight = 14.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Effect badge
                                Surface(
                                    color = SumerianGold.copy(alpha = 0.18f),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "⚡ Ефект: ${decree.effectDescription}",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BronzeDark,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // 6th-grade Educational historical fact
                                Surface(
                                    color = Color(0xFFFFF9C4),
                                    shape = RoundedCornerShape(4.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBC02D))
                                ) {
                                    Text(
                                        text = "📖 Історія (6 клас): ${decree.historyFact}",
                                        fontSize = 9.sp,
                                        color = Color(0xFF5D4037),
                                        lineHeight = 13.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                if (!isEnacted) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Витрати: ${decree.cost.summary}",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (canAfford) BronzeDark else TerracottaRed
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

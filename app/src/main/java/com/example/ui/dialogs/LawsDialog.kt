package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoundEffects
import com.example.model.LawEdict
import com.example.model.PlayerResources
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

@Composable
fun LawsDialog(
    activeLaws: Set<String>,
    resources: PlayerResources,
    onEnactLaw: (String) -> Unit,
    onRepealLaw: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedLaw by remember { mutableStateOf(LawEdict.ALL_LAWS.first()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = AncientParchmentLight,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .testTag("laws_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header: Title & Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Balance,
                        contentDescription = "Кодекс Законів",
                        tint = BronzeDark,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "КОДЕКС ЗАКОНІВ ТА ЦАРСЬКИХ РЕФОРМ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = BronzeDark
                        )
                        Text(
                            text = "Правові устави стародавнього Шумеру, Аккаду та Вавилону • Скарбниця: ${resources.silver} шекелів",
                            fontSize = 10.sp,
                            color = BronzePrimary
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_laws_dialog_button")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Content: Two-column layout (List of Edicts on left, Detail panel on right)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Column: List of Laws
                    LazyColumn(
                        modifier = Modifier
                            .weight(0.48f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(LawEdict.ALL_LAWS) { law ->
                            val isEnacted = law.id in activeLaws
                            val isSelected = law.id == selectedLaw.id

                            Surface(
                                color = if (isSelected) Color(0xFFFFF9E6) else Color(0xFFF4EDE0),
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) SumerianGold else if (isEnacted) Color(0xFF2E7D32) else BronzeDark.copy(alpha = 0.35f)
                                ),
                                shadowElevation = if (isSelected) 4.dp else 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedLaw = law
                                        SoundEffects.playClayStamp()
                                    }
                                    .testTag("law_item_${law.id}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isEnacted) Icons.Default.CheckCircle else Icons.Default.Gavel,
                                        contentDescription = null,
                                        tint = if (isEnacted) Color(0xFF2E7D32) else BronzeDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = law.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            color = BronzeDark
                                        )
                                        Text(
                                            text = if (isEnacted) "✓ ДІЮЧИЙ ЕДИКТ" else "Потрібно: ${law.requiredSilverToEnact} срібла",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isEnacted) Color(0xFF2E7D32) else BronzePrimary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Right Column: Detail & Enact/Repeal View
                    Surface(
                        color = Color(0xFFFFFDF8),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.5.dp, SumerianGold),
                        shadowElevation = 3.dp,
                        modifier = Modifier
                            .weight(0.52f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                // Title & Origin
                                Text(
                                    text = selectedLaw.name,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BronzeDark
                                )
                                Text(
                                    text = "📜 Джерело: ${selectedLaw.origin}",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BronzePrimary
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Description
                                Surface(
                                    color = Color(0xFFF3ECE0),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = selectedLaw.description,
                                        fontSize = 11.sp,
                                        color = Color(0xFF1E1106),
                                        lineHeight = 15.sp,
                                        modifier = Modifier.padding(8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Historical Background for 6th Grade History
                                Surface(
                                    color = Color(0xFFEFEBE9),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, SumerianGold.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "🏛️ Історична довідка:",
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF4E342E)
                                        )
                                        Text(
                                            text = selectedLaw.historicalBackground,
                                            fontSize = 10.sp,
                                            color = Color(0xFF2E1C14),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Pros & Cons
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "✓ Вигода: ${selectedLaw.prosText}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20),
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Surface(
                                    color = Color(0xFFFFEBEE),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(1.dp, TerracottaRed),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚠ Ціна: ${selectedLaw.consText}",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB71C1C),
                                        modifier = Modifier.padding(6.dp)
                                    )
                                }
                            }

                            // Action Button: Enact or Repeal
                            val isEnacted = selectedLaw.id in activeLaws
                            val canAfford = resources.silver >= selectedLaw.requiredSilverToEnact

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (isEnacted) {
                                    OutlinedButton(
                                        onClick = {
                                            SoundEffects.playClayStamp()
                                            onRepealLaw(selectedLaw.id)
                                        },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TerracottaRed),
                                        border = BorderStroke(1.5.dp, TerracottaRed),
                                        modifier = Modifier.testTag("repeal_law_button")
                                    ) {
                                        Text("СКАСУВАТИ ЕДИКТ", fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            SoundEffects.playCoinClink()
                                            SoundEffects.playClayStamp()
                                            onEnactLaw(selectedLaw.id)
                                        },
                                        enabled = canAfford,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = BronzePrimary,
                                            contentColor = Color.White
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.5.dp, SumerianGold),
                                        modifier = Modifier.testTag("enact_law_button")
                                    ) {
                                        Text(
                                            text = if (canAfford) "ПРИЙНЯТИ ЗАКОН (${selectedLaw.requiredSilverToEnact} срібла)" else "БРАКУЄ СРІБЛА (${selectedLaw.requiredSilverToEnact})",
                                            fontWeight = FontWeight.Black,
                                            fontSize = 11.5.sp
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
}

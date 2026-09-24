package com.example.ui.dialogs

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.City
import com.example.model.DefensiveSiegeEngagement
import com.example.model.Faction
import com.example.ui.components.UnitAvatar
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

enum class BattleTactic(
    val title: String,
    val description: String,
    val attackMod: Float,
    val defenseMod: Float,
    val historyFact: String
) {
    PHALANX_WALL(
        title = "Стіна бронзових щитів",
        description = "Воїни змикають прямокутні щити в монолітний стрій. Списи виставлені вперед.",
        attackMod = 1.1f,
        defenseMod = 1.4f,
        historyFact = "Шумерська фаланга — найдавніший організований стрій у світі! Жоден кінний набіг не міг пробити цю бронзову стіну."
    ),
    CHARIOT_CHARGE(
        title = "Фланговий удар онагрів",
        description = "Важкі чотириколісні колісниці таранять фланг ворога на повній швидкості.",
        attackMod = 1.45f,
        defenseMod = 0.85f,
        historyFact = "Колісниці Шумеру важили понад пів тонни! Вони діяли як танки Бронзової доби, розсіюючи ворожу піхоту."
    ),
    ARCHER_VOLLEY(
        title = "Залп очеретяних стріл",
        description = "Стрільці зі стін та насипів осипають ворога густим градом стріл.",
        attackMod = 1.25f,
        defenseMod = 1.1f,
        historyFact = "Шумерські стріли мали мідні вістря з шипами, які важко було витягти без важких ушкоджень."
    ),
    DEFENSIVE_WALLS(
        title = "Оборона за мурами міста",
        description = "Захисники утримують браму та вежі з цегли-сирцю, скидаючи каміння та бітум.",
        attackMod = 0.95f,
        defenseMod = 1.55f,
        historyFact = "Мури міст Межиріччя сягали 10 метрів завтовшки, тому прямий штурм без облоги був майже самогубством."
    )
}

@Composable
fun TacticalBattleDialog(
    siege: DefensiveSiegeEngagement,
    city: City,
    playerFactionId: String,
    onResolveTacticalBattle: (BattleTactic, payRansom: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val attackingFaction = Faction.getById(siege.attackingFactionId)
    var selectedTactic by remember { mutableStateOf(BattleTactic.PHALANX_WALL) }
    var isBattling by remember { mutableStateOf(false) }
    val battleClashAnim = remember { Animatable(0.5f) }

    LaunchedEffect(isBattling) {
        if (isBattling) {
            battleClashAnim.animateTo(0.8f, tween(800, easing = LinearEasing))
            battleClashAnim.animateTo(0.2f, tween(800, easing = LinearEasing))
            battleClashAnim.animateTo(0.6f, tween(600, easing = LinearEasing))
            onResolveTacticalBattle(selectedTactic, false)
        }
    }

    Dialog(onDismissRequest = { if (!isBattling) onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.5.dp, TerracottaRed),
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxWidth(0.98f)
                .padding(4.dp)
                .testTag("tactical_battle_dialog")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Battle Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(TerracottaRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ТАКТИЧНИЙ БІЙ: ОБЛОГА ${city.name.uppercase()}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black,
                                color = TerracottaRed
                            )
                            Text(
                                text = "Напад ворожих сил: ${attackingFaction.name}",
                                fontSize = 9.sp,
                                color = ClaySlate
                            )
                        }
                    }
                    if (!isBattling) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(26.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Armies Comparison Display
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Our garrison
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AncientParchmentDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Захисники ${city.name}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                            Text(text = "Сила оборони: 🛡️ ${city.defenseRating}", fontSize = 10.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                city.garrison.forEach { (unitId, count) ->
                                    if (count > 0) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            UnitAvatar(unitId = unitId, size = 20.dp)
                                            Text(text = "x$count", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Text(
                        text = "VS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = TerracottaRed,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    // Attacker army
                    Card(
                        colors = CardDefaults.cardColors(containerColor = AncientParchmentDark),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TerracottaRed),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = attackingFaction.name, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TerracottaRed)
                            Text(text = "Сила нападу: ⚔️ ${siege.attackerStrength}", fontSize = 10.sp, color = TerracottaRed, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                siege.attackerGarrison.forEach { (unitId, count) ->
                                    if (count > 0) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            UnitAvatar(unitId = unitId, size = 20.dp)
                                            Text(text = "x$count", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = TerracottaRed)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tactical Ticker / Clash Animation Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Баланс сил", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = BronzeDark)
                        Text(
                            text = if (isBattling) "⚔️ БИТВА ТРИВАЄ..." else "Оберіть тактичну формацію",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isBattling) TerracottaRed else BronzePrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    LinearProgressIndicator(
                        progress = { if (isBattling) battleClashAnim.value else (city.defenseRating.toFloat() / (city.defenseRating + siege.attackerStrength)).coerceIn(0.1f, 0.9f) },
                        color = Color(0xFF2E7D32),
                        trackColor = TerracottaRed,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (!isBattling) {
                    Text(
                        text = "Оберіть бойову тактику шумерського війська:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        BattleTactic.values().forEach { tactic ->
                            val isSelected = selectedTactic == tactic
                            Surface(
                                color = if (isSelected) SumerianGold.copy(alpha = 0.25f) else AncientParchmentDark,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) SumerianGold else BronzeDark.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedTactic = tactic }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) SumerianGold else Color.Transparent)
                                            .border(1.5.dp, BronzeDark, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tactic.title,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BronzeDark
                                        )
                                        Text(
                                            text = tactic.description,
                                            fontSize = 9.sp,
                                            color = ClaySlate
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Educational note for selected tactic
                    Surface(
                        color = Color(0xFFFFF9C4),
                        shape = RoundedCornerShape(4.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFBC02D))
                    ) {
                        Text(
                            text = "💡 Тактика Шумеру (6 клас): ${selectedTactic.historyFact}",
                            fontSize = 9.sp,
                            color = Color(0xFF5D4037),
                            lineHeight = 13.sp,
                            modifier = Modifier.padding(6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isBattling = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TerracottaRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("start_tactical_clash_button")
                        ) {
                            Icon(Icons.Default.MilitaryTech, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Прийняти бій!", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        if (siege.attackingFactionId == "martu") {
                            Button(
                                onClick = { onResolveTacticalBattle(selectedTactic, true) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SumerianGold,
                                    contentColor = ClaySlate
                                ),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .height(44.dp)
                            ) {
                                Text(text = "Відкупитися (35 🪙)", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚔️ Фаланги зійшлися у запеклій сутичці під градом очеретяних стріл...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TerracottaRed
                        )
                    }
                }
            }
        }
    }
}

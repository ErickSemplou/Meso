package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.audio.SoundEffects
import com.example.model.ActiveTacticalBattle
import com.example.model.BattleTactics
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun TacticalBattleDialog(
    battle: ActiveTacticalBattle,
    onExecuteBattle: (selectedTacticsId: String) -> Unit,
    onAutoResolve: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedTacticsId by remember { mutableStateOf(battle.selectedTacticsId) }
    var isBattling by remember { mutableStateOf(false) }
    var combatPhase by remember { mutableIntStateOf(0) } // 0: Briefing/Plan, 1: Clash, 2: Final Result

    var attackerMorale by remember { mutableFloatStateOf(1.0f) }
    var defenderMorale by remember { mutableFloatStateOf(1.0f) }
    val roundLogs = remember { mutableStateListOf<String>() }

    val coroutineScope = rememberCoroutineScope()

    Dialog(
        onDismissRequest = {
            if (!isBattling) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false, dismissOnClickOutside = false)
    ) {
        Surface(
            color = Color(0xFF1B1208),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(2.5.dp, SumerianGold),
            shadowElevation = 24.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.92f)
                .testTag("tactical_battle_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚔️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ТАКТИЧНИЙ ЕКРАН БОЮ: БИТВА ЗА ${battle.defenderCity.name.uppercase()}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = SumerianGoldBright
                        )
                        Text(
                            text = "Штурм та польовий бій армій Бронзової доби • 2600 р. до н.е.",
                            fontSize = 10.sp,
                            color = Color(0xFFD7CCC8)
                        )
                    }
                    if (!isBattling && combatPhase != 1) {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = SumerianGold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Armies Comparison Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2B1B0E), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attacker (Player)
                    Column(horizontalAlignment = Alignment.Start) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = battle.attackerFaction.bannerColor,
                                shape = RoundedCornerShape(3.dp),
                                modifier = Modifier.size(14.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${battle.attackerFaction.name}: ${battle.attackerCity.name}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Військова міць: ${battle.attackerPower} воїнів",
                            color = SumerianGold,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        LinearProgressIndicator(
                            progress = { attackerMorale },
                            color = Color(0xFF2E7D32),
                            trackColor = Color(0xFF1E3A20),
                            modifier = Modifier
                                .width(130.dp)
                                .height(6.dp)
                        )
                    }

                    Text(
                        text = "VS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TerracottaRed
                    )

                    // Defender
                    Column(horizontalAlignment = Alignment.End) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${battle.defenderFaction.name}: ${battle.defenderCity.name}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = battle.defenderFaction.bannerColor,
                                shape = RoundedCornerShape(3.dp),
                                modifier = Modifier.size(14.dp)
                            ) {}
                        }
                        Text(
                            text = "Оборона: ${battle.defenderPower} (+${battle.defenderWallLevel} мури)",
                            color = TerracottaRed,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        LinearProgressIndicator(
                            progress = { defenderMorale },
                            color = TerracottaRed,
                            trackColor = Color(0xFF3E1C1C),
                            modifier = Modifier
                                .width(130.dp)
                                .height(6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Phase 0: Plan & Tactic Selection
                if (combatPhase == 0) {
                    Text(
                        text = "ОБЕРІТЬ ПОЛКОВОДНИЦЬКУ ТАКТИКУ БОЮ:",
                        color = SumerianGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(BattleTactics.ALL_TACTICS) { tactic ->
                            val isSelected = tactic.id == selectedTacticsId
                            Surface(
                                color = if (isSelected) Color(0xFF3E2714) else Color(0xFF26170A),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    if (isSelected) 2.dp else 1.dp,
                                    if (isSelected) SumerianGoldBright else BronzeDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedTacticsId = tactic.id
                                        SoundEffects.playClayStamp()
                                    }
                                    .testTag("tactic_${tactic.id}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = tactic.icon, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tactic.name,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = if (isSelected) SumerianGoldBright else Color.White
                                        )
                                        Text(
                                            text = tactic.description,
                                            fontSize = 10.sp,
                                            color = Color(0xFFBCAAA4)
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row {
                                            if (tactic.attackBonusPercent != 0) {
                                                Text(
                                                    text = "Атака: +${tactic.attackBonusPercent}%  ",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF81C784)
                                                )
                                            }
                                            if (tactic.defenseBonusPercent != 0) {
                                                Text(
                                                    text = "Захист: ${if (tactic.defenseBonusPercent > 0) "+" else ""}${tactic.defenseBonusPercent}%  ",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (tactic.defenseBonusPercent > 0) Color(0xFF81C784) else TerracottaRed
                                                )
                                            }
                                            if (tactic.wallBreachBonusPercent != 0) {
                                                Text(
                                                    text = "Прорив мурів: +${tactic.wallBreachBonusPercent}%",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SumerianGold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Action Buttons: Engage in Tactical Battle vs Quick Auto-Resolve
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                SoundEffects.playSwordClash()
                                onAutoResolve()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SumerianGold),
                            border = BorderStroke(1.5.dp, SumerianGold),
                            modifier = Modifier
                                .weight(0.4f)
                                .height(46.dp)
                                .testTag("auto_resolve_button")
                        ) {
                            Text("ШВИДКИЙ АВТОБІЙ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                SoundEffects.playWarHorn()
                                combatPhase = 1
                                isBattling = true

                                coroutineScope.launch {
                                    roundLogs.clear()
                                    roundLogs.add("📯 Лунає шумерський бойовий ріг! Війська розгортаються на рівнині біля ${battle.defenderCity.name}.")
                                    delay(800)
                                    SoundEffects.playSwordClash()
                                    roundLogs.add("🏹 Залп очеретяних стріл затьмарив сонце! Ворожі стрільці відповідають з міських бійниць.")
                                    attackerMorale = 0.85f
                                    defenderMorale = 0.80f
                                    delay(900)
                                    SoundEffects.playSwordClash()
                                    roundLogs.add("🛡️ Важка бронзова фаланга змикає стрій та врізається у ворожі передові загони!")
                                    attackerMorale = 0.75f
                                    defenderMorale = 0.55f
                                    delay(900)
                                    SoundEffects.playSwordClash()
                                    val tacticObj = BattleTactics.ALL_TACTICS.find { it.id == selectedTacticsId }
                                    roundLogs.add("⚡ Застосовано маневр «${tacticObj?.name}»! Оборону зламано, воїни прориваються до брами!")
                                    defenderMorale = 0.15f
                                    attackerMorale = 0.70f
                                    delay(800)
                                    isBattling = false
                                    onExecuteBattle(selectedTacticsId)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = TerracottaRed,
                                contentColor = Color.White
                            ),
                            border = BorderStroke(1.5.dp, SumerianGoldBright),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.6f)
                                .height(46.dp)
                                .testTag("start_tactical_clash_button")
                        ) {
                            Text("ВСТУПИТИ В БІЙ (ТАКТИКА)", fontSize = 12.sp, fontWeight = FontWeight.Black)
                        }
                    }
                } else {
                    // Phase 1: Interactive Combat Simulation & Log
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(Color(0xFF140D05), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "⚔️ ХІД БИТВИ:",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = SumerianGold
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            items(roundLogs) { log ->
                                Text(
                                    text = log,
                                    color = Color(0xFFF5EBE6),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    if (isBattling) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            color = SumerianGoldBright,
                            trackColor = Color(0xFF2E1C0C),
                            modifier = Modifier.fillMaxWidth().height(4.dp)
                        )
                    }
                }
            }
        }
    }
}

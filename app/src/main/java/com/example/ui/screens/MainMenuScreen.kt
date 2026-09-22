package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.R
import com.example.data.SavedGameSummary
import com.example.model.Faction
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.DesertSand
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

enum class MainMenuTab {
    MAIN_HUB,
    FACTION_SELECTION
}

@Composable
fun MainMenuScreen(
    hasSavedGame: Boolean,
    savedGameSummary: SavedGameSummary? = null,
    isMusicMuted: Boolean,
    onContinueGame: () -> Unit,
    onStartNewGame: (String) -> Unit,
    onOpenCodex: () -> Unit,
    onToggleMusic: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(MainMenuTab.MAIN_HUB) }
    var selectedFaction by remember { mutableStateOf(Faction.URUK) }
    var showHelpDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DesertSand, AncientParchmentDark, AncientParchment)
                )
            )
    ) {
        // Background illustration overlay with soft opacity
        Image(
            painter = painterResource(id = R.drawable.img_mesopotamia_splash),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.88f)),
            alpha = 0.22f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Music Mute Button
                IconButton(
                    onClick = onToggleMusic,
                    modifier = Modifier.testTag("menu_music_toggle")
                ) {
                    Icon(
                        imageVector = if (isMusicMuted) Icons.Default.MusicOff else Icons.Default.MusicNote,
                        contentDescription = "Музика",
                        tint = BronzeDark
                    )
                }

                // Title Banner
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "МЕСОПОТАМІЯ: ЦАРСТВА",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = BronzeDark,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Покрокова стратегія перших цивілізацій Межиріччя (2600 р. до н.е.)",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BronzePrimary
                    )
                }

                // Action Icons (Help & Codex)
                Row {
                    IconButton(
                        onClick = { showHelpDialog = true },
                        modifier = Modifier.testTag("menu_help_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Правила гри",
                            tint = BronzeDark
                        )
                    }

                    IconButton(
                        onClick = onOpenCodex,
                        modifier = Modifier.testTag("menu_codex_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Енциклопедія",
                            tint = BronzeDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when (currentTab) {
                MainMenuTab.MAIN_HUB -> {
                    MainMenuHubView(
                        hasSavedGame = hasSavedGame,
                        savedGameSummary = savedGameSummary,
                        onContinueGame = onContinueGame,
                        onGoToNewGame = { currentTab = MainMenuTab.FACTION_SELECTION },
                        onOpenCodex = onOpenCodex,
                        onOpenHelp = { showHelpDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }

                MainMenuTab.FACTION_SELECTION -> {
                    FactionSelectionView(
                        selectedFaction = selectedFaction,
                        onSelectFaction = { selectedFaction = it },
                        onStartGame = { onStartNewGame(selectedFaction.id) },
                        onBack = { currentTab = MainMenuTab.MAIN_HUB },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }

    if (showHelpDialog) {
        GameRulesHelpDialog(onDismiss = { showHelpDialog = false })
    }
}

@Composable
private fun MainMenuHubView(
    hasSavedGame: Boolean,
    savedGameSummary: SavedGameSummary?,
    onContinueGame: () -> Unit,
    onGoToNewGame: () -> Unit,
    onOpenCodex: () -> Unit,
    onOpenHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Decorative Mesopotamian Emblem Banner
        Surface(
            color = AncientParchmentLight.copy(alpha = 0.95f),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, SumerianGold),
            shadowElevation = 4.dp,
            modifier = Modifier
                .widthIn(max = 520.dp)
                .fillMaxWidth(0.9f)
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "❖ ГОЛОВНЕ МЕНЮ КАМПАНІЇ ❖",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzePrimary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Очольте одне з п'яти великих міст-держав Шумеру, підкоріть розливи Тигру й Євфрату та зведіть першу в історії імперію.",
                    fontSize = 11.sp,
                    color = ClaySlate,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )
            }
        }

        // Primary Action 1: CONTINUE CAMPAIGN
        val savedFaction = savedGameSummary?.let { summary ->
            Faction.ALL_PLAYABLE.find { it.id == summary.factionId }
        }

        Button(
            onClick = onContinueGame,
            enabled = hasSavedGame,
            colors = ButtonDefaults.buttonColors(
                containerColor = SumerianGold,
                contentColor = ClaySlate,
                disabledContainerColor = AncientParchmentLight.copy(alpha = 0.6f),
                disabledContentColor = Color.Gray
            ),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                if (hasSavedGame) BronzeDark else Color.Gray.copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(0.85f)
                .height(60.dp)
                .testTag("continue_campaign_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Продовжити",
                    tint = if (hasSavedGame) BronzeDark else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "ПРОДОВЖИТИ КАМПАНІЮ",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (hasSavedGame && savedGameSummary != null) {
                            "Царство: ${savedFaction?.name ?: "Шумер"} • Хід ${savedGameSummary.turn}/50 (${savedGameSummary.yearBCE} р. до н.е.)"
                        } else if (hasSavedGame) {
                            "Відновити збережений стан гри"
                        } else {
                            "Немає збереженої кампанії"
                        },
                        fontSize = 10.sp,
                        color = if (hasSavedGame) BronzePrimary else Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Primary Action 2: NEW CAMPAIGN
        Button(
            onClick = onGoToNewGame,
            colors = ButtonDefaults.buttonColors(
                containerColor = BronzePrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, SumerianGold),
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(0.85f)
                .height(54.dp)
                .testTag("new_campaign_menu_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Нова гра",
                    tint = SumerianGold,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "НОВА КАМПАНІЯ",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Обрати місто-державу та почати правління",
                        fontSize = 10.sp,
                        color = SumerianGoldBright
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Secondary Actions Row: Codex & Rules
        Row(
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth(0.85f),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onOpenCodex,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AncientParchmentLight,
                    contentColor = BronzeDark
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("hub_codex_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = BronzeDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ЕНЦИКЛОПЕДІЯ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = onOpenHelp,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AncientParchmentLight,
                    contentColor = BronzeDark
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .testTag("hub_help_button")
            ) {
                Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = null,
                    tint = BronzeDark,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ПРАВИЛА ГРИ",
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun FactionSelectionView(
    selectedFaction: Faction,
    onSelectFaction: (Faction) -> Unit,
    onStartGame: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Back navigation bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_to_main_menu_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Назад",
                    tint = BronzeDark
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "ГОЛОВНЕ МЕНЮ",
                    color = BronzeDark,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "ОБЕРІТЬ МІСТО-ДЕРЖАВУ ДЛЯ КАМПАНІЇ:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = BronzeDark
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Faction Selection Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(Faction.ALL_PLAYABLE) { faction ->
                val isSelected = faction.id == selectedFaction.id
                Surface(
                    color = if (isSelected) SumerianGold.copy(alpha = 0.25f) else AncientParchmentLight,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 3.dp else 1.5.dp,
                        if (isSelected) SumerianGold else BronzeDark
                    ),
                    shadowElevation = if (isSelected) 8.dp else 2.dp,
                    modifier = Modifier
                        .width(230.dp)
                        .clickable { onSelectFaction(faction) }
                        .testTag("faction_card_${faction.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Faction crest
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(faction.bannerColor)
                                .border(2.dp, SumerianGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = faction.name.take(2),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = faction.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = BronzeDark
                        )
                        Text(
                            text = faction.title,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzePrimary
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Володар: ${faction.ruler}",
                            fontSize = 10.sp,
                            color = Color(0xFF4E342E)
                        )
                        Text(
                            text = "Покровитель: ${faction.patronDeity}",
                            fontSize = 9.sp,
                            color = Color(0xFF6D4C41),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = faction.description,
                            fontSize = 10.sp,
                            color = BronzeDark,
                            lineHeight = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = AncientParchmentDark,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⭐ ${faction.bonusDescription}",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B5E20),
                                modifier = Modifier.padding(6.dp),
                                lineHeight = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Start Campaign Button
        Button(
            onClick = onStartGame,
            colors = ButtonDefaults.buttonColors(
                containerColor = BronzePrimary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, SumerianGold),
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .height(48.dp)
                .testTag("start_campaign_button")
        ) {
            Icon(Icons.Default.Castle, contentDescription = "Почати", tint = SumerianGold)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "РОЗПОЧАТИ ПРАВЛІННЯ: ${selectedFaction.name.uppercase()}",
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun GameRulesHelpDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = BronzeDark)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Правила та настанови правителю",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BronzeDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RuleTipItem(
                    emoji = "🌾",
                    title = "Ячмінь та Зерносховища",
                    description = "Ячмінь — основа життя Месопотамії. Ним харчуються містяни та воїни. Без зерносховищ надлишок врожаю псується."
                )
                RuleTipItem(
                    emoji = "🧱",
                    title = "Річкова глина та канали",
                    description = "Глина з мулу Євфрату потрібна для спорудження зрошувальних систем, мурів та величних зикуратів."
                )
                RuleTipItem(
                    emoji = "🛡️",
                    title = "Збройова бронза та армія",
                    description = "Бронза виплавляється в кузнях і витрачається на найм важких списоносців та бойових колісниць."
                )
                RuleTipItem(
                    emoji = "🪙",
                    title = "Срібні шекелі та торгівля",
                    description = "Срібло слугує мірою вартості. Прокладайте караванні шляхи до сусідніх міст у вкладці Торгівлі."
                )
                RuleTipItem(
                    emoji = "📜",
                    title = "Історична вікторина (6 клас)",
                    description = "Після кожного завершеного ходу мудреці ставлять запитання з історії Месопотамії. Правильна відповідь дає додаткові ресурси та підтримує лояльність!"
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = BronzePrimary)
            ) {
                Text("ЗРОЗУМІЛО", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = AncientParchmentLight
    )
}

@Composable
private fun RuleTipItem(emoji: String, title: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = AncientParchment),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = emoji, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = BronzeDark
                )
                Text(
                    text = description,
                    fontSize = 10.sp,
                    color = ClaySlate,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.model.Faction
import com.example.model.LogCategory
import com.example.model.TurnLogItem
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold

@Composable
fun TurnLogOverlay(
    isVisible: Boolean,
    turn: Int,
    yearBCE: Int,
    logs: List<TurnLogItem>,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf(LogCategory.ALL) }

    val filteredLogs = remember(logs, selectedCategory) {
        if (selectedCategory == LogCategory.ALL) {
            logs
        } else {
            logs.filter { it.category == selectedCategory }
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }) + fadeIn(),
        exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth }) + fadeOut(),
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.45f))
                .clickable { onClose() }
        ) {
            // Slide-in Parchment Panel on the Right
            Surface(
                color = AncientParchmentLight,
                shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, SumerianGold),
                shadowElevation = 20.dp,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.85f)
                    .align(Alignment.CenterEnd)
                    .clickable(enabled = false) {} // Prevent click-through
                    .testTag("turn_log_overlay")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    // Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = SumerianGold,
                                shape = CircleShape,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.HistoryEdu,
                                        contentDescription = "Вісті",
                                        tint = BronzeDark,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Вісті Межиріччя",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = BronzeDark
                                )
                                Text(
                                    text = "$yearBCE р. до н.е. • Хід $turn • Події та дії ботів",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6D4C41)
                                )
                            }
                        }

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier.testTag("close_turn_log_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Закрити",
                                tint = BronzeDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Category Filter Scrollable Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        items(LogCategory.values()) { category ->
                            val isSelected = category == selectedCategory
                            Surface(
                                color = if (isSelected) SumerianGold else AncientParchmentDark,
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) BronzeDark else BronzeDark.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier.clickable { selectedCategory = category }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(text = category.iconEmoji, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = category.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) ClaySlate else BronzeDark
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Log Items List
                    if (filteredLogs.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Немає нових подій у цій категорії",
                                fontSize = 12.sp,
                                color = BronzeDark.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(filteredLogs, key = { it.id }) { item ->
                                TurnLogCard(item = item)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Footer Action Button
                    Button(
                        onClick = onClose,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SumerianGold,
                            contentColor = ClaySlate
                        ),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("turn_log_continue_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = BronzeDark,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ПРОДОВЖИТИ ПРАВЛІННЯ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TurnLogCard(item: TurnLogItem) {
    val faction = item.factionId?.let { Faction.getById(it) }

    Surface(
        color = AncientParchment,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            Color(item.highlightColorHex).copy(alpha = 0.6f)
        ),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left Category Avatar Badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(item.highlightColorHex).copy(alpha = 0.15f))
                    .border(1.dp, Color(item.highlightColorHex), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.category.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = item.title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )

                    if (faction != null) {
                        Surface(
                            color = faction.bannerColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, faction.bannerColor)
                        ) {
                            Text(
                                text = faction.name,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = item.description,
                    fontSize = 11.sp,
                    color = Color(0xFF4E342E),
                    lineHeight = 15.sp
                )
            }
        }
    }
}

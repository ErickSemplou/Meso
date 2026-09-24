package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
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
import com.example.model.DiplomaticStatus
import com.example.model.Faction
import com.example.model.GameState
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun FactionFilterBar(
    state: GameState,
    selectedFactionId: String?,
    onFactionSelected: (String?) -> Unit,
    onOpenFactionsOverview: () -> Unit,
    onOpenDiplomacy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = AncientParchmentDark.copy(alpha = 0.92f),
        shape = RoundedCornerShape(24.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SumerianGold),
        shadowElevation = 8.dp,
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("faction_filter_bar")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            // "All Factions" Filter Chip
            val isAllSelected = selectedFactionId == null
            Surface(
                color = if (isAllSelected) SumerianGold else AncientParchmentLight,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isAllSelected) BronzeDark else BronzeDark.copy(alpha = 0.4f)
                ),
                modifier = Modifier
                    .clickable { onFactionSelected(null) }
                    .testTag("faction_chip_all")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(text = "🗺️", fontSize = 11.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Всі Держави",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Individual Faction Chips
            Faction.ALL_FACTIONS.forEach { faction ->
                val isPlayer = faction.id == state.playerFactionId
                val isSelected = selectedFactionId == faction.id
                val citiesCount = state.cities.count { it.factionId == faction.id }
                val relation = state.relations[faction.id]?.status ?: DiplomaticStatus.NEUTRAL

                Surface(
                    color = when {
                        isSelected -> SumerianGold
                        isPlayer -> AncientParchmentLight
                        else -> AncientParchmentDark
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected || isPlayer) 1.5.dp else 1.dp,
                        when {
                            isSelected -> BronzeDark
                            isPlayer -> SumerianGold
                            else -> BronzeDark.copy(alpha = 0.3f)
                        }
                    ),
                    modifier = Modifier
                        .clickable {
                            if (isSelected) onFactionSelected(null) else onFactionSelected(faction.id)
                        }
                        .testTag("faction_chip_${faction.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        // Banner Color Indicator
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(faction.bannerColor)
                                .border(1.dp, SumerianGold, CircleShape)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = faction.name,
                                    fontSize = 10.5.sp,
                                    fontWeight = if (isSelected || isPlayer) FontWeight.Black else FontWeight.Bold,
                                    color = BronzeDark
                                )
                                if (citiesCount > 0) {
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "($citiesCount)",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF5D4037)
                                    )
                                }
                            }
                        }

                        // Diplomatic status icon badge (for non-player factions)
                        if (!isPlayer) {
                            Spacer(modifier = Modifier.width(4.dp))
                            val (badgeColor, badgeIcon) = when (relation) {
                                DiplomaticStatus.ALLIANCE, DiplomaticStatus.TRADE_PACT -> Color(0xFF2E7D32) to "🤝"
                                DiplomaticStatus.HOSTILE, DiplomaticStatus.WAR -> TerracottaRed to "⚔️"
                                else -> Color(0xFFE65100) to "🕊️"
                            }
                            Text(text = badgeIcon, fontSize = 9.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
            }

            // Quick Link to Factions Overview Modal
            Surface(
                color = SumerianGold,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
                modifier = Modifier
                    .clickable { onOpenFactionsOverview() }
                    .testTag("faction_bar_overview_btn")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = BronzeDark,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Рейтинг",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = BronzeDark
                    )
                }
            }
        }
    }
}

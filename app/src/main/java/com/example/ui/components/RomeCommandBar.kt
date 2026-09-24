package com.example.ui.components

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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Building
import com.example.model.City
import com.example.model.Faction
import com.example.model.Technology
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun RomeCommandBar(
    selectedCity: City?,
    playerFactionId: String,
    currentTechId: String?,
    currentTechTurnsRemaining: Int,
    activeDecreesCount: Int = 0,
    onOpenBuildingDialog: () -> Unit,
    onOpenRecruitmentDialog: () -> Unit,
    onOpenDiplomacyDialog: () -> Unit,
    onOpenTechDialog: () -> Unit,
    onOpenDecreesDialog: () -> Unit = {},
    onAttackCity: () -> Unit,
    onEndTurn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPlayerOwned = selectedCity?.factionId == playerFactionId
    val currentTech = currentTechId?.let { Technology.getById(it) }

    Surface(
        color = AncientParchmentDark,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 5.dp)
                .fillMaxWidth()
        ) {
            // Left: Selected City Overview Card with Unit Avatars & City Trait
            if (selectedCity != null) {
                val cityFaction = Faction.getById(selectedCity.factionId)
                Surface(
                    color = AncientParchmentLight,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, cityFaction.bannerColor),
                    modifier = Modifier.width(260.dp)
                ) {
                    Column(modifier = Modifier.padding(5.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(cityFaction.bannerColor)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedCity.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            // City Unique Trait Badge
                            Surface(
                                color = SumerianGold.copy(alpha = 0.35f),
                                shape = RoundedCornerShape(4.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark)
                            ) {
                                Text(
                                    text = selectedCity.uniqueTrait,
                                    fontSize = 7.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BronzeDark,
                                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = if (isPlayerOwned) "Наше" else cityFaction.name.take(4),
                                fontSize = 8.5.sp,
                                color = if (isPlayerOwned) Color(0xFF2E7D32) else TerracottaRed,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Garrison Units with Avatars
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier.padding(vertical = 1.dp)
                        ) {
                            Text(
                                text = "Військо: ",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                            selectedCity.garrison.forEach { (unitId, count) ->
                                if (count > 0) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(AncientParchmentDark, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 2.dp, vertical = 1.dp)
                                    ) {
                                        UnitAvatar(unitId = unitId, size = 16.dp)
                                        Spacer(modifier = Modifier.width(1.dp))
                                        Text(
                                            text = "x$count",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = BronzeDark
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "🛡️ ${selectedCity.defenseRating}",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                        }

                        if (selectedCity.buildingInProgress != null) {
                            val bld = Building.getById(selectedCity.buildingInProgress)
                            Text(
                                text = "⏳ Будується: ${bld.name} (${selectedCity.buildingTurnsRemaining} х.)",
                                fontSize = 7.5.sp,
                                color = BronzePrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Оберіть місто на карті",
                    fontSize = 10.sp,
                    color = BronzeDark,
                    modifier = Modifier.width(180.dp)
                )
            }

            // Center: Command Action Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isPlayerOwned) {
                    CommandTabButton(
                        icon = Icons.Default.AccountBalance,
                        title = "Будівлі",
                        testTag = "tab_buildings",
                        onClick = onOpenBuildingDialog
                    )

                    CommandTabButton(
                        icon = Icons.Default.MilitaryTech,
                        title = "Військо",
                        testTag = "tab_recruitment",
                        onClick = onOpenRecruitmentDialog
                    )
                } else if (selectedCity != null) {
                    // Foreign city: Option to launch military campaign!
                    Button(
                        onClick = onAttackCity,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TerracottaRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("attack_city_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Штурм",
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "Похід на ${selectedCity.name}", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                CommandTabButton(
                    icon = Icons.Default.Handshake,
                    title = "Дипломатія",
                    testTag = "tab_diplomacy",
                    onClick = onOpenDiplomacyDialog
                )

                CommandTabButton(
                    icon = Icons.Default.Gavel,
                    title = "Закони",
                    testTag = "tab_decrees",
                    badge = if (activeDecreesCount > 0) "$activeDecreesCount" else null,
                    onClick = onOpenDecreesDialog
                )

                CommandTabButton(
                    icon = Icons.Default.Science,
                    title = if (currentTech != null) "${currentTech.name.take(5)}..(${currentTechTurnsRemaining})" else "Наука",
                    testTag = "tab_tech",
                    badge = if (currentTech == null) "!" else null,
                    onClick = onOpenTechDialog
                )
            }

            // Right: Massive Classic Rome Total War End Turn Button
            Button(
                onClick = onEndTurn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SumerianGold,
                    contentColor = ClaySlate
                ),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
                modifier = Modifier
                    .height(48.dp)
                    .width(125.dp)
                    .testTag("end_turn_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = "Кінець ходу",
                            tint = BronzeDark,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Кінець ходу",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    Text(
                        text = "Хід гравця",
                        fontSize = 7.5.sp,
                        color = Color(0xFF4E342E)
                    )
                }
            }
        }
    }
}

@Composable
fun CommandTabButton(
    icon: ImageVector,
    title: String,
    testTag: String,
    badge: String? = null,
    onClick: () -> Unit
) {
    Surface(
        color = AncientParchmentLight,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = BronzePrimary,
                        modifier = Modifier.size(17.dp)
                    )
                    if (badge != null) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .clip(CircleShape)
                                .background(TerracottaRed)
                                .align(Alignment.TopEnd),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = badge, fontSize = 6.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = title,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzeDark
                )
            }
        }
    }
}

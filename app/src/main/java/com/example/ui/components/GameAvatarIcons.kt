package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Castle
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Faction
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

/**
 * Returns the generated AI ruler portrait resource ID for each faction.
 */
fun getRulerImageResource(factionId: String): Int {
    return when (factionId) {
        "uruk" -> R.drawable.img_ruler_gilgamesh
        "ur" -> R.drawable.img_ruler_urnammu
        "lagash" -> R.drawable.img_ruler_eannatum
        "kish" -> R.drawable.img_ruler_enmebaragesi
        "nippur" -> R.drawable.img_ruler_nippur
        else -> R.drawable.img_ruler_gilgamesh
    }
}

/**
 * Rich illustrated avatar for military units and agents.
 */
@Composable
fun UnitAvatar(
    unitId: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val (bgColors, iconVector, badgeEmoji, borderColor) = when (unitId) {
        "spearmen" -> Quadruple(
            listOf(Color(0xFF8D6E63), Color(0xFF4E342E)),
            Icons.Default.Shield,
            "🛡️",
            Color(0xFFD7CCC8)
        )
        "phalanx" -> Quadruple(
            listOf(Color(0xFFB71C1C), Color(0xFF4A148C)),
            Icons.Default.MilitaryTech,
            "⚔️",
            SumerianGoldBright
        )
        "chariot" -> Quadruple(
            listOf(Color(0xFFE65100), Color(0xFFBF360C)),
            Icons.Default.Shield,
            "🛞",
            SumerianGold
        )
        "archers" -> Quadruple(
            listOf(Color(0xFF2E7D32), Color(0xFF1B5E20)),
            Icons.Default.Shield,
            "🏹",
            Color(0xFFA5D6A7)
        )
        "priest" -> Quadruple(
            listOf(Color(0xFF0277BD), Color(0xFF01579B)),
            Icons.Default.Handshake,
            "🕊️",
            Color(0xFF80DEEA)
        )
        "caravan" -> Quadruple(
            listOf(Color(0xFFF57F17), Color(0xFF827717)),
            Icons.Default.Storefront,
            "🐫",
            SumerianGoldBright
        )
        else -> Quadruple(
            listOf(BronzePrimary, BronzeDark),
            Icons.Default.Shield,
            "⚔️",
            SumerianGold
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        shadowElevation = 4.dp,
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(bgColors)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeEmoji,
                fontSize = (size.value * 0.44f).sp
            )
            // Tiny bottom corner insignia
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
                    .size((size.value * 0.32f).dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((size.value * 0.22f).dp)
                )
            }
        }
    }
}

/**
 * Rich illustrated avatar for city buildings and wonders.
 */
@Composable
fun BuildingAvatar(
    buildingId: String,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    val (bgColors, iconVector, badgeEmoji, borderColor) = when (buildingId) {
        "canals" -> Quadruple(
            listOf(Color(0xFF00838F), Color(0xFF006064)),
            Icons.Default.WaterDrop,
            "🌊",
            Color(0xFF80DEEA)
        )
        "granary" -> Quadruple(
            listOf(Color(0xFFF57F17), Color(0xFFE65100)),
            Icons.Default.Grass,
            "🌾",
            SumerianGold
        )
        "walls" -> Quadruple(
            listOf(Color(0xFF5D4037), Color(0xFF3E2723)),
            Icons.Default.Castle,
            "🧱",
            Color(0xFFBCAAA4)
        )
        "ziggurat" -> Quadruple(
            listOf(Color(0xFFFFB300), Color(0xFFE65100)),
            Icons.Default.AccountBalance,
            "🏛️",
            SumerianGoldBright
        )
        "edubba" -> Quadruple(
            listOf(Color(0xFF6A1B9A), Color(0xFF4A148C)),
            Icons.Default.AutoStories,
            "📜",
            Color(0xFFCE93D8)
        )
        "bronze_foundry" -> Quadruple(
            listOf(Color(0xFFC62828), Color(0xFFBF360C)),
            Icons.Default.Fireplace,
            "⚒️",
            SumerianGold
        )
        "harbor" -> Quadruple(
            listOf(Color(0xFF0277BD), Color(0xFF01579B)),
            Icons.Default.DirectionsBoat,
            "⛵",
            Color(0xFF81D4FA)
        )
        "temple_inanna" -> Quadruple(
            listOf(Color(0xFF8E24AA), Color(0xFF4A148C)),
            Icons.Default.Star,
            "⭐",
            SumerianGoldBright
        )
        else -> Quadruple(
            listOf(BronzePrimary, BronzeDark),
            Icons.Default.AccountBalance,
            "🏛️",
            SumerianGold
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor),
        shadowElevation = 4.dp,
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(bgColors)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = badgeEmoji,
                fontSize = (size.value * 0.44f).sp
            )
            // Tiny bottom corner insignia
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(2.dp)
                    .size((size.value * 0.32f).dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size((size.value * 0.22f).dp)
                )
            }
        }
    }
}

/**
 * High-definition framed ruler portrait card with royal Sumerian border.
 */
@Composable
fun RulerPortraitCard(
    faction: Faction,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 130.dp
) {
    val imageRes = getRulerImageResource(faction.id)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = AncientParchmentDark,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.5.dp else 1.2.dp,
            color = if (isSelected) SumerianGoldBright else BronzeDark
        ),
        shadowElevation = if (isSelected) 8.dp else 3.dp,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(imageHeight)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = faction.ruler,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Bottom gradient vignette with ruler name
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.4f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Faction crest at top corner
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(faction.bannerColor)
                    .border(1.5.dp, SumerianGold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = faction.name.take(1),
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Ruler Title Plate at Bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = faction.ruler,
                    color = SumerianGoldBright,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Лугаль міста ${faction.name}",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

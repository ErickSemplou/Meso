package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.City
import com.example.model.Faction
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.RiverEuphrates
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright
import com.example.ui.theme.TerracottaRed

@Composable
fun StrategicMapCanvas(
    cities: List<City>,
    selectedCityId: String?,
    playerFactionId: String,
    onCitySelected: (String) -> Unit,
    botCampaignSourceCityId: String? = null,
    botCampaignTargetCityId: String? = null,
    filteredFactionId: String? = null,
    onArmyUnitTapped: (factionId: String, unitTypeId: String, cityName: String, regimentCount: Int) -> Unit = { _, _, _, _ -> },
    modifier: Modifier = Modifier
) {
    val pulseAnim = remember { Animatable(0.4f) }
    val riverFlowAnim = remember { Animatable(0f) }
    val boatAnim = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        pulseAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    LaunchedEffect(Unit) {
        riverFlowAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(6000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {
        boatAnim.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(14000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1E1308))
            .border(3.dp, BronzeDark, RoundedCornerShape(12.dp))
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // 1. High Resolution Historical Mesopotamia Terrain Art
        Image(
            painter = painterResource(id = R.drawable.img_mesopotamia_map_terrain),
            contentDescription = "Стратегічна карта стародавнього Межиріччя",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Vintage Vignette & Warm Ancient Glaze
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xFF2E1705).copy(alpha = 0.40f)
                        ),
                        radius = widthPx.coerceAtLeast(heightPx) * 0.8f
                    )
                )
        )

        // 3. Vector Canvas: Faction Influence Territories, Roads, Rivers, Marshlands, Canals
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            // A. Faction Territorial Influence Zones (Colored aura around controlled cities)
            cities.forEach { city ->
                val faction = Faction.getById(city.factionId)
                val isPlayer = city.factionId == playerFactionId
                val cityCenter = Offset(city.mapX * widthPx, city.mapY * heightPx)
                val territoryRadius = 55.dp.toPx()

                // Soft territorial wash
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            faction.bannerColor.copy(alpha = if (isPlayer) 0.32f else 0.22f),
                            faction.bannerColor.copy(alpha = 0.05f),
                            Color.Transparent
                        ),
                        center = cityCenter,
                        radius = territoryRadius
                    ),
                    radius = territoryRadius,
                    center = cityCenter
                )

                // Dotted border of territorial realm
                drawCircle(
                    color = faction.bannerColor.copy(alpha = 0.45f),
                    radius = territoryRadius * 0.85f,
                    center = cityCenter,
                    style = Stroke(
                        width = 1.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                    )
                )
            }

            // B. Strategic Royal Trade Roads connecting City-States
            val roadConnections = listOf(
                Pair("kish", "nippur"),
                Pair("nippur", "umma"),
                Pair("nippur", "uruk"),
                Pair("umma", "lagash"),
                Pair("umma", "uruk"),
                Pair("uruk", "ur"),
                Pair("lagash", "ur"),
                Pair("ur", "eridu"),
                Pair("lagash", "susa"),
                Pair("kish", "uruk")
            )

            val roadColor = Color(0xFFE0D4B8).copy(alpha = 0.70f)
            roadConnections.forEach { (c1Id, c2Id) ->
                val city1 = cities.find { it.id == c1Id }
                val city2 = cities.find { it.id == c2Id }
                if (city1 != null && city2 != null) {
                    val p1 = Offset(city1.mapX * widthPx, city1.mapY * heightPx)
                    val p2 = Offset(city2.mapX * widthPx, city2.mapY * heightPx)
                    drawLine(
                        color = roadColor,
                        start = p1,
                        end = p2,
                        strokeWidth = 2.5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
                    )
                }
            }

            // C. Euphrates River (Пуратту) - Water Arteries with Banks
            val euphrates = Path().apply {
                moveTo(widthPx * 0.12f, 0f)
                cubicTo(
                    widthPx * 0.22f, heightPx * 0.24f,
                    widthPx * 0.27f, heightPx * 0.54f,
                    widthPx * 0.50f, heightPx * 0.76f
                )
                lineTo(widthPx * 0.54f, heightPx * 0.82f)
            }
            // River mud bank
            drawPath(
                euphrates,
                color = Color(0xFF4A3520).copy(alpha = 0.6f),
                style = Stroke(width = 14f, pathEffect = PathEffect.cornerPathEffect(24f))
            )
            // River water
            drawPath(
                euphrates,
                color = RiverEuphrates.copy(alpha = 0.92f),
                style = Stroke(width = 9f, pathEffect = PathEffect.cornerPathEffect(24f))
            )
            // Water glint
            drawPath(
                euphrates,
                color = Color(0xFFB2EBF2).copy(alpha = 0.60f),
                style = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 10f), riverFlowAnim.value * 50f)
                )
            )

            // D. Tigris River (Ідіґлат)
            val tigris = Path().apply {
                moveTo(widthPx * 0.44f, 0f)
                cubicTo(
                    widthPx * 0.54f, heightPx * 0.24f,
                    widthPx * 0.66f, heightPx * 0.48f,
                    widthPx * 0.74f, heightPx * 0.88f
                )
            }
            drawPath(
                tigris,
                color = Color(0xFF4A3520).copy(alpha = 0.6f),
                style = Stroke(width = 13f, pathEffect = PathEffect.cornerPathEffect(24f))
            )
            drawPath(
                tigris,
                color = RiverEuphrates.copy(alpha = 0.88f),
                style = Stroke(width = 8f, pathEffect = PathEffect.cornerPathEffect(24f))
            )
            drawPath(
                tigris,
                color = Color(0xFFB2EBF2).copy(alpha = 0.55f),
                style = Stroke(
                    width = 2.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f), riverFlowAnim.value * 50f)
                )
            )

            // E. Ancient Irrigation Canals between Euphrates & Tigris (The Eden of Sumer)
            val royalCanal = Path().apply {
                moveTo(widthPx * 0.24f, heightPx * 0.24f) // From Kish/Euphrates
                lineTo(widthPx * 0.42f, heightPx * 0.30f) // To Nippur
                lineTo(widthPx * 0.60f, heightPx * 0.35f) // To Umma
                lineTo(widthPx * 0.68f, heightPx * 0.52f) // To Lagash/Tigris
            }
            drawPath(
                royalCanal,
                color = Color(0xFF0097A7).copy(alpha = 0.75f),
                style = Stroke(
                    width = 3.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f)
                )
            )

            // F. Active Bot Campaign Marching Arrow
            if (botCampaignSourceCityId != null && botCampaignTargetCityId != null) {
                val sourceCity = cities.find { it.id == botCampaignSourceCityId }
                val targetCity = cities.find { it.id == botCampaignTargetCityId }
                if (sourceCity != null && targetCity != null) {
                    val p1 = Offset(sourceCity.mapX * widthPx, sourceCity.mapY * heightPx)
                    val p2 = Offset(targetCity.mapX * widthPx, targetCity.mapY * heightPx)

                    drawLine(
                        color = TerracottaRed.copy(alpha = 0.9f),
                        start = p1,
                        end = p2,
                        strokeWidth = 5f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 8f), boatAnim.value * 44f)
                    )

                    drawCircle(
                        color = Color(0xFFD32F2F),
                        radius = 14f * pulseAnim.value,
                        center = p2
                    )
                }
            }

            // G. Selected City Tactical Glowing Rings
            val selectedCity = cities.find { it.id == selectedCityId }
            if (selectedCity != null) {
                val center = Offset(selectedCity.mapX * widthPx, selectedCity.mapY * heightPx)
                // Radiant pulse aura
                drawCircle(
                    color = SumerianGoldBright.copy(alpha = 0.35f * pulseAnim.value),
                    radius = 46.dp.toPx(),
                    center = center
                )
                // Golden crenellated ring
                drawCircle(
                    color = SumerianGold,
                    radius = 42.dp.toPx(),
                    center = center,
                    style = Stroke(
                        width = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                    )
                )
            }
        }

        // 4. Moving River Trade Barge on the Euphrates
        val boatX = (0.16f + boatAnim.value * 0.34f) * maxWidth.value
        val boatY = (0.10f + boatAnim.value * 0.65f) * maxHeight.value
        Box(
            modifier = Modifier
                .offset(x = boatX.dp, y = boatY.dp)
                .alpha(0.85f)
        ) {
            Surface(
                color = AncientParchmentLight,
                shape = RoundedCornerShape(4.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark),
                shadowElevation = 2.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(text = "⛵", fontSize = 9.sp)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "Баржа",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )
                }
            }
        }

        // 5. Geographic Historical Landscape Labels & Cartouche
        // Persian Gulf ("Lower Sea")
        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.60f), y = (maxHeight * 0.86f))
                .alpha(0.88f)
        ) {
            Surface(
                color = Color(0xFF00363A).copy(alpha = 0.70f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "⚓ ПЕРСЬКА ЗАТОКА («НИЖНЄ МОРЕ»)",
                    color = Color(0xFFE0F7FA),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }
        }

        // Zagros Mountains
        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.78f) - 30.dp, y = (maxHeight * 0.10f))
                .alpha(0.85f)
        ) {
            Surface(
                color = Color(0xFF3E2723).copy(alpha = 0.75f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "▲ ГОРИ ЗАГРОСУ (ЕЛАМ) ▲",
                    color = Color(0xFFFFE082),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // Syrian Desert
        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.04f), y = (maxHeight * 0.45f))
                .alpha(0.85f)
        ) {
            Surface(
                color = Color(0xFF4E342E).copy(alpha = 0.70f),
                shape = RoundedCornerShape(6.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "≈ СИРІЙСЬКИЙ СТЕП (МАРТУ) ≈",
                    color = Color(0xFFFFECB3),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }

        // Region Title Badge: Sumer / Lower Mesopotamia
        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.32f), y = (maxHeight * 0.46f))
                .alpha(0.85f)
        ) {
            Text(
                text = "✦ Ш У М Е Р ✦",
                color = Color(0xFFD7CCC8).copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )
        }

        // 6. Marching Bot Army Token
        if (botCampaignSourceCityId != null && botCampaignTargetCityId != null) {
            val sourceCity = cities.find { it.id == botCampaignSourceCityId }
            val targetCity = cities.find { it.id == botCampaignTargetCityId }
            if (sourceCity != null && targetCity != null) {
                val midX = (sourceCity.mapX + targetCity.mapX) / 2f
                val midY = (sourceCity.mapY + targetCity.mapY) / 2f
                val botFaction = Faction.getById(sourceCity.factionId)
                val botUnitId = sourceCity.garrison.keys.firstOrNull() ?: "phalanx"
                Box(
                    modifier = Modifier
                        .offset(
                            x = (midX * maxWidth.value).dp - 36.dp,
                            y = (midY * maxHeight.value).dp - 14.dp
                        )
                        .clickable {
                            onArmyUnitTapped(sourceCity.factionId, botUnitId, sourceCity.name, 2)
                        }
                        .testTag("marching_army_${sourceCity.factionId}")
                ) {
                    Surface(
                        color = TerracottaRed,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, SumerianGoldBright),
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        ) {
                            Text(text = "⚔️", fontSize = 10.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Похід: ${botFaction.name.take(4)}",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }

        // 7. Render Authentic Total War Style Mesopotamian Settlements
        cities.forEach { city ->
            val faction = Faction.getById(city.factionId)
            val isPlayerCity = city.factionId == playerFactionId
            val isSelected = city.id == selectedCityId
            val isDimmedByFilter = filteredFactionId != null && filteredFactionId != city.factionId

            val cityX = city.mapX * maxWidth.value
            val cityY = city.mapY * maxHeight.value

            val topUnitEntry = city.garrison.maxByOrNull { it.value }
            val topUnitId = topUnitEntry?.key ?: "phalanx"
            val topUnitCount = topUnitEntry?.value ?: 1

            Box(
                modifier = Modifier
                    .offset(x = cityX.dp - 48.dp, y = cityY.dp - 46.dp)
                    .alpha(if (isDimmedByFilter) 0.35f else 1.0f)
                    .clickable { onCitySelected(city.id) }
                    .testTag("map_settlement_${city.id}")
            ) {
                AuthenticMesopotamianSettlementModel(
                    city = city,
                    faction = faction,
                    isPlayerCity = isPlayerCity,
                    isSelected = isSelected,
                    topUnitId = topUnitId,
                    topUnitCount = topUnitCount,
                    onArmyUnitTapped = onArmyUnitTapped
                )
            }
        }

        // 8. Decorative Ancient Compass Cartouche (Bottom Left)
        Surface(
            color = AncientParchmentLight.copy(alpha = 0.88f),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "🧭",
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "КАРТА МЕЖИРІЧЧЯ",
                        fontSize = 8.5.sp,
                        fontWeight = FontWeight.Black,
                        color = BronzeDark
                    )
                    Text(
                        text = "Пн ▲ • 2600 р. до н.е.",
                        fontSize = 7.5.sp,
                        color = BronzePrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthenticMesopotamianSettlementModel(
    city: City,
    faction: Faction,
    isPlayerCity: Boolean,
    isSelected: Boolean,
    topUnitId: String,
    topUnitCount: Int,
    onArmyUnitTapped: (factionId: String, unitTypeId: String, cityName: String, regimentCount: Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        // Upper: 3D Ziggurat Temple Model + Faction Standard Flag + Garrison Token
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            // Faction Flag Standard
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 2.dp)
            ) {
                Surface(
                    color = faction.bannerColor,
                    shape = RoundedCornerShape(2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGoldBright),
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(width = 16.dp, height = 13.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = faction.name.take(1),
                            color = Color.White,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                // Copper Flagpole
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(11.dp)
                        .background(SumerianGold)
                )
            }

            // Stepped 3D Ziggurat with Golden/Blue Glazed Sanctuary
            Surface(
                color = if (city.hasBuilding("ziggurat")) Color(0xFFE5A93B) else Color(0xFFC7A87A),
                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                shadowElevation = if (isSelected) 8.dp else 4.dp,
                modifier = Modifier.size(width = 32.dp, height = 24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Shrine of the God (Shakhuru)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(6.dp)
                            .background(if (city.hasBuilding("ziggurat")) SumerianGoldBright else Color(0xFF00838F))
                    )
                    // City Battlements & Arch Gate
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 5.dp, height = 10.dp)
                                .background(BronzeDark)
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 8.dp, height = 8.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(Color(0xFF3E2723))
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 5.dp, height = 10.dp)
                                .background(BronzeDark)
                        )
                    }
                }
            }

            // Stationed Garrison Token / Interactive Army Badge
            val unitEmoji = when (topUnitId) {
                "phalanx" -> "🛡️"
                "chariot" -> "🐎"
                "archers" -> "🏹"
                "spearmen" -> "🗡️"
                "priest" -> "📜"
                "caravan" -> "🐪"
                else -> "⚔️"
            }

            Surface(
                color = if (isPlayerCity) Color(0xFF1B5E20) else Color(0xFFB71C1C),
                shape = RoundedCornerShape(5.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGoldBright),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(start = 2.dp, bottom = 2.dp)
                    .clickable {
                        onArmyUnitTapped(city.factionId, topUnitId, city.name, topUnitCount)
                    }
                    .testTag("unit_badge_${city.id}")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 3.dp, vertical = 1.dp)
                ) {
                    Text(text = unitEmoji, fontSize = 9.sp)
                    Text(
                        text = "$topUnitCount",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Lower: City Name Banner with Population & Defense
        Surface(
            color = if (isSelected) SumerianGoldBright else if (isPlayerCity) Color(0xFFFFFDF5) else AncientParchmentLight,
            shape = RoundedCornerShape(6.dp),
            border = androidx.compose.foundation.BorderStroke(
                if (isSelected) 2.dp else 1.dp,
                if (isSelected) BronzeDark else faction.bannerColor
            ),
            shadowElevation = if (isSelected) 8.dp else 3.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = city.name.uppercase(),
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) Color(0xFF2E1A0E) else BronzeDark,
                    letterSpacing = 0.5.sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "👥${city.population / 1000}k",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4E342E)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "🛡️${city.defenseRating}",
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isPlayerCity) Color(0xFF1B5E20) else TerracottaRed
                    )
                }
            }
        }
    }
}

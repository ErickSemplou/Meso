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
import androidx.compose.material.icons.filled.Castle
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
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.DesertSand
import com.example.ui.theme.FertileValleyGreen
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
    modifier: Modifier = Modifier
) {
    val pulseAnim = remember { Animatable(0.4f) }
    val caravanProgress = remember { Animatable(0f) }

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
        caravanProgress.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(8000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2B1D0C))
            .border(2.5.dp, BronzeDark, RoundedCornerShape(10.dp))
    ) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()

        // 1. Base Volumetric Painted Landscape Background
        Image(
            painter = painterResource(id = R.drawable.img_mesopotamia_map_terrain),
            contentDescription = "Стратегічна карта Межиріччя",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Dark historical vignette for contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color.Transparent, Color(0xFF1E1005).copy(alpha = 0.45f)),
                        radius = (widthPx.coerceAtLeast(heightPx) * 0.75f)
                    )
                )
        )

        // 3. Vector Tactical Overlays: Rivers, Canals, Roads & Caravans
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            // -- Strategic Trade Roads between cities --
            val roadColor = Color(0xFFD7CCC8).copy(alpha = 0.75f)
            val roadStroke = Stroke(
                width = 3f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f)
            )

            val roadConnections = listOf(
                Pair("kish", "nippur"),
                Pair("nippur", "umma"),
                Pair("nippur", "uruk"),
                Pair("umma", "lagash"),
                Pair("umma", "uruk"),
                Pair("uruk", "ur"),
                Pair("lagash", "ur"),
                Pair("ur", "eridu"),
                Pair("lagash", "susa")
            )

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
                        strokeWidth = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f)
                    )
                }
            }

            // -- Euphrates & Tigris Dynamic Water Lines --
            val euphrates = Path().apply {
                moveTo(widthPx * 0.15f, 0f)
                cubicTo(
                    widthPx * 0.24f, heightPx * 0.26f, // Near Kish
                    widthPx * 0.28f, heightPx * 0.56f, // Near Uruk
                    widthPx * 0.52f, heightPx * 0.74f  // Near Ur & into Gulf
                )
            }
            drawPath(
                euphrates,
                color = RiverEuphrates.copy(alpha = 0.9f),
                style = Stroke(width = 9f, pathEffect = PathEffect.cornerPathEffect(20f))
            )
            drawPath(
                euphrates,
                color = Color(0xFF80DEEA).copy(alpha = 0.5f),
                style = Stroke(width = 3.5f, pathEffect = PathEffect.cornerPathEffect(20f))
            )

            val tigris = Path().apply {
                moveTo(widthPx * 0.45f, 0f)
                cubicTo(
                    widthPx * 0.55f, heightPx * 0.25f,
                    widthPx * 0.68f, heightPx * 0.50f, // Near Lagash
                    widthPx * 0.76f, heightPx * 0.90f
                )
            }
            drawPath(
                tigris,
                color = RiverEuphrates.copy(alpha = 0.85f),
                style = Stroke(width = 8f, pathEffect = PathEffect.cornerPathEffect(20f))
            )
            drawPath(
                tigris,
                color = Color(0xFF80DEEA).copy(alpha = 0.5f),
                style = Stroke(width = 3f, pathEffect = PathEffect.cornerPathEffect(20f))
            )

            // -- Irrigation canals --
            val canal = Path().apply {
                moveTo(widthPx * 0.42f, heightPx * 0.32f) // Nippur
                lineTo(widthPx * 0.60f, heightPx * 0.34f) // Umma
                lineTo(widthPx * 0.68f, heightPx * 0.52f) // Lagash
            }
            drawPath(
                canal,
                color = Color(0xFF00ACC1).copy(alpha = 0.8f),
                style = Stroke(width = 3.5f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 6f), 0f))
            )

            // -- Selected City Tactical Aura --
            val selectedCity = cities.find { it.id == selectedCityId }
            if (selectedCity != null) {
                val center = Offset(selectedCity.mapX * widthPx, selectedCity.mapY * heightPx)
                drawCircle(
                    color = SumerianGoldBright.copy(alpha = 0.3f * pulseAnim.value),
                    radius = 42f,
                    center = center
                )
                drawCircle(
                    color = SumerianGold,
                    radius = 38f,
                    center = center,
                    style = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 6f), 0f))
                )
            }
        }

        // 4. Geographic Landmark Labels
        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.82f) - 30.dp, y = (maxHeight * 0.12f))
                .alpha(0.85f)
        ) {
            Text(
                text = "▲ ГОРИ ЗАГРОСУ ▲",
                color = Color(0xFFD7CCC8),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.05f), y = (maxHeight * 0.48f))
                .alpha(0.85f)
        ) {
            Text(
                text = "≈ СИРІЙСЬКА ПУСТЕЛЯ ≈",
                color = Color(0xFFFFECB3),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Box(
            modifier = Modifier
                .offset(x = (maxWidth * 0.68f), y = (maxHeight * 0.88f))
                .alpha(0.85f)
        ) {
            Text(
                text = "⚓ ПЕРСЬКА ЗАТОКА",
                color = Color(0xFFE0F7FA),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        // 5. Total War Style Settlement Tokens
        cities.forEach { city ->
            val faction = Faction.getById(city.factionId)
            val isPlayerCity = city.factionId == playerFactionId
            val isSelected = city.id == selectedCityId

            Box(
                modifier = Modifier
                    .offset(
                        x = (city.mapX * maxWidth.value).dp - 42.dp,
                        y = (city.mapY * maxHeight.value).dp - 36.dp
                    )
                    .clickable { onCitySelected(city.id) }
                    .testTag("city_${city.id}")
            ) {
                TotalWarSettlementModel(
                    city = city,
                    faction = faction,
                    isPlayerCity = isPlayerCity,
                    isSelected = isSelected
                )
            }
        }
    }
}

@Composable
private fun TotalWarSettlementModel(
    city: City,
    faction: Faction,
    isPlayerCity: Boolean,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(84.dp)
    ) {
        // Upper: 3D Miniature Fortress / Ziggurat Model + Fluttering Banner
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center
        ) {
            // Faction Flag / Standard on pole
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 2.dp)
            ) {
                Surface(
                    color = faction.bannerColor,
                    shape = RoundedCornerShape(2.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGoldBright),
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(width = 16.dp, height = 12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = faction.name.take(1),
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                // Brass Flagpole
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(10.dp)
                        .background(SumerianGold)
                )
            }

            // 3D Stepped Ziggurat / Fortress Structure
            Surface(
                color = if (city.hasBuilding("ziggurat")) Color(0xFFD7A15C) else Color(0xFFBCAAA4),
                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzeDark),
                shadowElevation = if (isSelected) 8.dp else 4.dp,
                modifier = Modifier.size(width = 28.dp, height = 22.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Top temple (Shakhuru)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(5.dp)
                            .background(if (city.hasBuilding("ziggurat")) SumerianGold else BronzeDark)
                    )
                    // Gate & Battlements
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 2.dp, vertical = 1.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 4.dp, height = 9.dp)
                                .background(BronzeDark)
                        )
                        // Gate arch
                        Box(
                            modifier = Modifier
                                .size(width = 7.dp, height = 7.dp)
                                .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                                .background(Color(0xFF3E2723))
                        )
                        Box(
                            modifier = Modifier
                                .size(width = 4.dp, height = 9.dp)
                                .background(BronzeDark)
                        )
                    }
                }
            }

            // Stationed Garrison Token / Soldier Badge
            Surface(
                color = if (isPlayerCity) Color(0xFF1B5E20) else Color(0xFFB71C1C),
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                modifier = Modifier
                    .size(16.dp)
                    .padding(start = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "${city.garrison.values.sum()}",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Lower: Total War Stone / Clay City Plaque
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isSelected) SumerianGold else AncientParchmentLight,
            shadowElevation = if (isSelected) 8.dp else 3.dp,
            border = androidx.compose.foundation.BorderStroke(
                width = if (isSelected) 2.dp else 1.2.dp,
                color = if (isPlayerCity) faction.bannerColor else BronzeDark
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = city.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = BronzeDark
                    )
                    if (city.hasBuilding("walls")) {
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.Castle,
                            contentDescription = "Мури",
                            tint = BronzePrimary,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "${city.population / 1000}k",
                        fontSize = 8.sp,
                        color = ClaySlate,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "• ${if (isPlayerCity) "Влада" else faction.name.take(3)}",
                        fontSize = 8.sp,
                        color = if (isPlayerCity) Color(0xFF2E7D32) else TerracottaRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

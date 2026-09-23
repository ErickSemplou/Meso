package com.example.ui.dialogs

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.Building
import com.example.model.City
import com.example.model.PlayerResources
import com.example.ui.components.BuildingAvatar
import com.example.ui.components.UnitAvatar
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.ClaySlate
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.TerracottaRed

@Composable
fun CityManageDialog(
    city: City,
    resources: PlayerResources,
    onConstructBuilding: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(6.dp)
                .testTag("city_manage_dialog")
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                // Header Banner with Ancient City Illustration
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(65.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_sumerian_cities),
                        contentDescription = "Архітектура Месопотамії",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Black.copy(alpha = 0.4f))
                                )
                            )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = "Будівництво",
                                tint = SumerianGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Розбудова міста: ${city.name}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = SumerianGold
                                )
                                Text(
                                    text = "Населення: ${city.population} • Оборона: ${city.defenseRating}",
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("close_city_dialog")
                        ) {
                            Icon(Icons.Default.Close, contentDescription = "Закрити", tint = Color.White)
                        }
                    }
                }

                // City Unique Trait Box
                Surface(
                    color = SumerianGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(6.dp)
                    ) {
                        Text(text = "👑", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "Унікальна властивість: ${city.uniqueTrait}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = BronzeDark
                            )
                            Text(
                                text = city.traitDescription,
                                fontSize = 9.sp,
                                color = Color(0xFF5D4037)
                            )
                        }
                    }
                }

                // Garrison Troop Avatars Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AncientParchmentDark, RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Гарнізон міста: ",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = BronzeDark
                    )
                    city.garrison.forEach { (unitId, count) ->
                        if (count > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(AncientParchmentLight, RoundedCornerShape(4.dp))
                                    .padding(horizontal = 4.dp, vertical = 2.dp)
                            ) {
                                UnitAvatar(unitId = unitId, size = 18.dp)
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "x$count",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BronzeDark
                                )
                            }
                        }
                    }
                }

                if (city.buildingInProgress != null) {
                    val inProgress = Building.getById(city.buildingInProgress)
                    Surface(
                        color = SumerianGold.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(
                            text = "⏳ Зараз тривають будівельні роботи: ${inProgress.name} (Залишилося: ${city.buildingTurnsRemaining} хід)",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                ) {
                    items(Building.ALL_BUILDINGS) { building ->
                        val isBuilt = city.hasBuilding(building.id)
                        val isBuilding = city.buildingInProgress == building.id
                        val canAfford = resources.canAfford(building.cost)

                        Surface(
                            color = if (isBuilt) AncientParchmentDark.copy(alpha = 0.5f) else AncientParchment,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isBuilt) Color(0xFF2E7D32) else BronzeDark.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        BuildingAvatar(
                                            buildingId = building.id,
                                            size = 36.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = building.name,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = BronzeDark
                                            )
                                            Text(
                                                text = building.subtitle,
                                                fontSize = 9.sp,
                                                color = Color(0xFF6D4C41)
                                            )
                                        }
                                    }

                                    when {
                                        isBuilt -> {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Збудовано",
                                                    tint = Color(0xFF2E7D32),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(2.dp))
                                                Text(
                                                    text = "Збудовано",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF2E7D32)
                                                )
                                            }
                                        }
                                        isBuilding -> {
                                            Text(
                                                text = "Будується...",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SumerianGold
                                            )
                                        }
                                        else -> {
                                            Button(
                                                onClick = { onConstructBuilding(building.id) },
                                                enabled = canAfford && city.buildingInProgress == null,
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = BronzePrimary,
                                                    contentColor = Color.White
                                                ),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier.testTag("build_button_${building.id}")
                                            ) {
                                                Text(
                                                    text = "Збудувати (${building.turnsToBuild} х.)",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = building.description,
                                    fontSize = 9.sp,
                                    color = BronzeDark,
                                    lineHeight = 12.sp
                                )

                                // Educational note
                                Text(
                                    text = "💡 ${building.historyFact}",
                                    fontSize = 8.sp,
                                    color = Color(0xFF4E342E),
                                    lineHeight = 11.sp,
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )

                                // Cost display
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    if (building.cost.grain > 0) Text("🌾 ${building.cost.grain}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (building.cost.clay > 0) Text("🧱 ${building.cost.clay}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (building.cost.bronze > 0) Text("🛡 ${building.cost.bronze}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                    if (building.cost.silver > 0) Text("🪙 ${building.cost.silver}", fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

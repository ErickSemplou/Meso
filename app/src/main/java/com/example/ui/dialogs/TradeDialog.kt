package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.PlayerResources
import com.example.model.TradeRoute
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold

@Composable
fun TradeDialog(
    resources: PlayerResources,
    tradeRoutes: List<TradeRoute>,
    onExchangeGrainForSilver: () -> Unit,
    onExchangeSilverForBronze: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("trade_dialog")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Storefront,
                            contentDescription = "Торгівля",
                            tint = BronzeDark,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Базарний майдан (Карум)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = BronzeDark
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_trade_dialog")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Закрити", tint = BronzeDark)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Діючі караванні шляхи:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = BronzePrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                tradeRoutes.forEach { route ->
                    Surface(
                        color = AncientParchment,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text(
                                text = "🐪 Маршрут: ${route.originCityId.uppercase()} ➔ ${route.destinationCityId.uppercase()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = BronzeDark
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "+${route.profitPerTurn} 🪙/хід",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Бартерний обмін купців (Тамкарів):",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = BronzePrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Exchange 1: Grain for Silver
                Surface(
                    color = AncientParchment,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column {
                            Text("Продати ячмінь за срібло", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BronzeDark)
                            Text("Віддати: 40 🌾 ➔ Отримати: 35 🪙", fontSize = 10.sp, color = Color(0xFF5D4037))
                        }
                        Button(
                            onClick = onExchangeGrainForSilver,
                            enabled = resources.grain >= 40,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SumerianGold,
                                contentColor = BronzeDark
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Обмін", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Exchange 2: Silver for Bronze
                Surface(
                    color = AncientParchment,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BronzeDark.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Column {
                            Text("Купити олов'яну руду й мідь", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = BronzeDark)
                            Text("Віддати: 40 🪙 ➔ Отримати: 25 🛡", fontSize = 10.sp, color = Color(0xFF5D4037))
                        }
                        Button(
                            onClick = onExchangeSilverForBronze,
                            enabled = resources.silver >= 40,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BronzePrimary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Купити", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

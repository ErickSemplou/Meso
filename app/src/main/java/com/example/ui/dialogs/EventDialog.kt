package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.GameEvent
import com.example.ui.theme.AncientParchment
import com.example.ui.theme.AncientParchmentDark
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.BronzePrimary
import com.example.ui.theme.SumerianGold

@Composable
fun EventDialog(
    event: GameEvent,
    onOptionSelected: (Int) -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AncientParchmentLight,
            border = androidx.compose.foundation.BorderStroke(2.dp, BronzeDark),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("event_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(AncientParchmentDark, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "Подія",
                        tint = BronzeDark,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Державна подія",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = BronzeDark
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = event.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = BronzeDark,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Historical context box
                Surface(
                    color = AncientParchmentDark.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SumerianGold)
                ) {
                    Text(
                        text = "📜 ${event.historicalContext}",
                        fontSize = 11.sp,
                        color = Color(0xFF4E342E),
                        modifier = Modifier.padding(8.dp),
                        lineHeight = 15.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = event.description,
                    fontSize = 13.sp,
                    color = BronzeDark,
                    lineHeight = 18.sp,
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Рішення володаря:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = BronzePrimary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Options
                event.options.forEachIndexed { index, option ->
                    Surface(
                        color = AncientParchment,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, BronzePrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onOptionSelected(index) }
                            .testTag("event_option_$index")
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = option.text,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = BronzeDark
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.description,
                                fontSize = 11.sp,
                                color = Color(0xFF6D4C41)
                            )
                        }
                    }
                }
            }
        }
    }
}

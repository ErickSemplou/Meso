package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Faction
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.SumerianGold
import com.example.ui.theme.SumerianGoldBright

@Composable
fun TurnTransitionOverlay(
    visible: Boolean,
    turn: Int,
    yearBCE: Int,
    season: String,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically { -it / 2 },
        exit = fadeOut() + slideOutVertically { -it / 2 },
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BronzeDark,
                border = androidx.compose.foundation.BorderStroke(2.5.dp, SumerianGoldBright),
                shadowElevation = 24.dp,
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF3E2723), Color(0xFF1E1005))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "≈ ПЛИН ЧАСУ У МЕЖИРІЧЧІ ≈",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = SumerianGold,
                            letterSpacing = 2.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "$yearBCE р. до н.е. • Хід $turn",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = AncientParchmentLight
                        )

                        Text(
                            text = "Сезон: $season",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SumerianGoldBright
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // AI factions processing indicator
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Faction.ALL_PLAYABLE.forEach { faction ->
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(faction.bannerColor)
                                        .border(1.dp, SumerianGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = faction.name.take(1),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = SumerianGold,
                                strokeWidth = 2.5.dp,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Міста Шумеру збирають врожай та рухають полки...",
                                fontSize = 10.sp,
                                color = AncientParchmentLight
                            )
                        }
                    }
                }
            }
        }
    }
}

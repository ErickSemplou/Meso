package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AncientParchmentLight
import com.example.ui.theme.BronzeDark
import com.example.ui.theme.SumerianGold
import kotlinx.coroutines.delay

@Composable
fun FloatingResourceDeltas(
    visible: Boolean,
    grainDelta: Int,
    clayDelta: Int,
    bronzeDelta: Int,
    silverDelta: Int,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            color = AncientParchmentLight,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, SumerianGold),
            shadowElevation = 12.dp
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(text = "✨ УРОЖАЙ:", fontSize = 10.sp, fontWeight = FontWeight.Black, color = BronzeDark)

                DeltaItem(icon = "🌾", value = grainDelta)
                DeltaItem(icon = "🧱", value = clayDelta)
                DeltaItem(icon = "🥉", value = bronzeDelta)
                DeltaItem(icon = "🪙", value = silverDelta)
            }
        }
    }
}

@Composable
private fun DeltaItem(icon: String, value: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = icon, fontSize = 11.sp)
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = if (value >= 0) "+$value" else "$value",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (value >= 0) Color(0xFF2E7D32) else Color(0xFFB71C1C)
        )
    }
}

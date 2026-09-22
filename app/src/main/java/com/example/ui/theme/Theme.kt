package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MesopotamianColorScheme = lightColorScheme(
    primary = BronzePrimary,
    onPrimary = Color.White,
    primaryContainer = SumerianGoldBright.copy(alpha = 0.25f),
    onPrimaryContainer = BronzeDark,
    secondary = SumerianGold,
    onSecondary = MudbrickBrown,
    secondaryContainer = AncientParchmentDark,
    onSecondaryContainer = MudbrickBrown,
    tertiary = LapisLazuli,
    onTertiary = Color.White,
    background = AncientParchment,
    onBackground = CuneiformInk,
    surface = AncientParchmentLight,
    onSurface = CuneiformInk,
    surfaceVariant = AncientParchmentDark,
    onSurfaceVariant = MudbrickBrown,
    error = TerracottaRed,
    onError = Color.White
)

private val MesopotamianDarkColorScheme = darkColorScheme(
    primary = SumerianGold,
    onPrimary = MudbrickBrown,
    primaryContainer = BronzePrimary,
    onPrimaryContainer = AncientParchmentLight,
    secondary = BronzeLight,
    onSecondary = Color.White,
    secondaryContainer = MudbrickBrown,
    onSecondaryContainer = SumerianGoldBright,
    tertiary = LapisLazuliLight,
    onTertiary = ClaySlate,
    background = ClaySlate,
    onBackground = AncientParchment,
    surface = MudbrickBrown,
    onSurface = AncientParchment,
    surfaceVariant = Color(0xFF33201C),
    onSurfaceVariant = AncientParchmentDark,
    error = TerracottaAccent,
    onError = Color.White
)

@Composable
fun MesopotamiaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) MesopotamianDarkColorScheme else MesopotamianColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

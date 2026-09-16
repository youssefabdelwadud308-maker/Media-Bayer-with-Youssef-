package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = Color(0xFF97F0FF),
    secondary = AmberGold,
    onSecondary = Color(0xFF432C00),
    secondaryContainer = Color(0xFF604100),
    onSecondaryContainer = Color(0xFFFFDEA3),
    tertiary = EmeraldGreen,
    background = NavyDeep,
    onBackground = TextPrimaryDark,
    surface = NavySurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextSecondaryDark,
    error = CrimsonRed
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006877),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF97F0FF),
    onPrimaryContainer = Color(0xFF001F25),
    secondary = Color(0xFF815600),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDEA3),
    onSecondaryContainer = Color(0xFF281800),
    tertiary = Color(0xFF006C4C),
    background = BackgroundLight,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = TextSecondaryLight,
    error = CrimsonRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek futuristic dark media buyer dashboard
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

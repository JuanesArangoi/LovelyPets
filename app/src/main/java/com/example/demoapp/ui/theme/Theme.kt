package com.example.demoapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definimos nuestra paleta de colores personalizada
val CustomGreenDark = Color(0xFF003913)
val CustomGreenLight = Color(0xFFAFD8C0)
val CustomBlueLight = Color(0xFFE3F2FD)

private val DarkColorScheme = darkColorScheme(
    primary = CustomGreenLight,
    secondary = CustomBlueLight,
    tertiary = CustomGreenDark
)

private val LightColorScheme = lightColorScheme(
    primary = CustomGreenDark,
    onPrimary = Color.White,
    primaryContainer = CustomGreenLight,
    onPrimaryContainer = CustomGreenDark,
    secondary = CustomBlueLight,
    onSecondary = Color.Black,
    surface = Color.White,
    onSurface = CustomGreenDark
)

@Composable
fun DemoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // DESACTIVADO POR DEFECTO PARA VER TUS COLORES
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

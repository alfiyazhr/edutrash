package com.example.edutrash.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF5B7338),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF50593C),
    onPrimaryContainer = Color(0xFFF2EBDF),
    secondary = Color(0xFFBFB7A8),
    onSecondary = Color.Black,
    background = Color(0xFFF2EBDF),
    onBackground = Color(0xFF24261A),
    surface = Color(0xFFF2EBDF),
    onSurface = Color(0xFF24261A),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF5B7338),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF50593C),
    onPrimaryContainer = Color(0xFFF2EBDF),
    secondary = Color(0xFFBFB7A8),
    onSecondary = Color.Black,
    background = Color(0xFF24261A),
    onBackground = Color(0xFFF2EBDF),
    surface = Color(0xFF24261A),
    onSurface = Color(0xFFF2EBDF),
)

@Composable
fun EDUTRASHTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = getTypography(),
        shapes = Shapes,
        content = content
    )
}

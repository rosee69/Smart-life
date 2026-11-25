package ua.edu.mobile.smartlife.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Pink80,
    onPrimary = Color(0xFF4A0038),
    secondary = Pink80,
    onSecondary = Color(0xFF4A0038),
    tertiary = Pink80,
    background = Color(0xFF171219),
    onBackground = Color(0xFFFFF7FC),
    surface = Color(0xFF28202B),
    onSurface = Color(0xFFFFF7FC),
    surfaceVariant = Color(0xFF403542),
    onSurfaceVariant = Color(0xFFE4D5E2),
    primaryContainer = Color(0xFF493047),
    onPrimaryContainer = Color(0xFFFFB8E8),
    outline = Color(0xFFBBA8B8)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFF600BE),
    onPrimary = Color.White,
    secondary = Color(0xFFE879C7),
    tertiary = Color(0xFFB84B91),
    background = Color(0xFFFFF7FC),
    onBackground = Color(0xFF302832),
    surface = Color(0xFFF3EFF7),
    onSurface = Color(0xFF302832),
    surfaceVariant = Color(0xFFEAE2EF),
    onSurfaceVariant = Color(0xFF665B68),
    outline = Color(0xFF9A8C9B)
)

@Composable
fun SmartlifeTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
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

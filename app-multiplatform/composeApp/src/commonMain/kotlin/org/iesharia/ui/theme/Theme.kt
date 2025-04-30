package org.iesharia.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Definición de colores para tema OLED/Dark
val darkBackground = Color(0xFF000000)
val darkSurface = Color(0xFF121212)
val darkPrimary = Color(0xFF7B68EE)  // Violeta medio
val darkAccent = Color(0xFF00BFFF)   // Azul celeste
val glassSurface = Color(0x80121212) // Color con transparencia para efecto glassy

// Colores para tema claro (opcional)
val lightBackground = Color(0xFFF5F5F5)
val lightSurface = Color(0xFFFFFFFF)
val lightPrimary = Color(0xFF6200EE)
val lightAccent = Color(0xFF03DAC5)

// Paleta de colores para tema oscuro
private val DarkColorPalette = darkColors(
    primary = darkPrimary,
    primaryVariant = darkPrimary.copy(alpha = 0.7f),
    secondary = darkAccent,
    background = darkBackground,
    surface = darkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White
)

// Paleta de colores para tema claro (opcional)
private val LightColorPalette = lightColors(
    primary = lightPrimary,
    primaryVariant = lightPrimary.copy(alpha = 0.7f),
    secondary = lightAccent,
    background = lightBackground,
    surface = lightSurface,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

// Tema principal de la aplicación
@Composable
fun LuchaAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Por defecto usa el tema del sistema
    forceOledTheme: Boolean = true, // Para forzar el tema OLED
    content: @Composable () -> Unit
) {
    // Si se fuerza el tema OLED o el sistema está en modo oscuro
    val colors = if (forceOledTheme || darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
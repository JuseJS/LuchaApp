package org.iesharia.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

// Esquema de colores oscuro optimizado para OLED
private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = SandDark,
    onPrimaryContainer = Color.White,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = CanaryBlue,
    onSecondaryContainer = Color.White,

    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = LaurisilvaGreen,
    onTertiaryContainer = Color.White,

    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = TraditionalRed,
    onErrorContainer = Color.White,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurface.copy(alpha = 0.8f),
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    // Colores adicionales para mejorar la UI
    surfaceTint = DarkPrimary.copy(alpha = 0.05f),
    inverseSurface = Color(0xFFEEEEEE),
    inverseOnSurface = Color(0xFF1A1A1A),
    scrim = Color.Black.copy(alpha = 0.6f)
)

// Esquema de colores claro (aunque nos centraremos en el oscuro para OLED)
private val LightColorScheme = lightColorScheme(
    primary = SandDark,
    onPrimary = Color.White,
    primaryContainer = SandLight,
    onPrimaryContainer = Color(0xFF3C2E1A),

    secondary = CanaryBlue,
    onSecondary = Color.White,
    secondaryContainer = CanaryBlueLight,
    onSecondaryContainer = Color(0xFF071B33),

    tertiary = LaurisilvaGreen,
    onTertiary = Color.White,
    tertiaryContainer = LaurisilvaGreenLight,
    onTertiaryContainer = Color(0xFF002111),

    background = Color(0xFFF8F8F8),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    error = TraditionalRed,
    onError = Color.White,
    errorContainer = TraditionalRedLight,
    onErrorContainer = Color(0xFF410000),

    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF444444),
    outline = Color(0xFFAAAAAA)
)

/**
 * Objeto para acceder al tema globalmente
 */
object WrestlingTheme {
    val dimensions: Dimensions
        @Composable
        @ReadOnlyComposable
        get() = LocalAppDimensions.current

    val shapes: AppShapes
        @Composable
        @ReadOnlyComposable
        get() = LocalAppShapes.current
}

@Composable
fun WrestlingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // No usamos colores dinámicos en esta versión
    content: @Composable () -> Unit
) {
    // Siempre forzamos el tema oscuro para aprovechar pantallas OLED
    val colorScheme = DarkColorScheme

    // Proveemos las dimensiones y formas personalizadas
    CompositionLocalProvider(
        LocalAppDimensions provides Dimensions(),
        LocalAppShapes provides AppShapes()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
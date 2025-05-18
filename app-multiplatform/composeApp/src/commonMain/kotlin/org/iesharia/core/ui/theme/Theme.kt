package org.iesharia.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color

/**
 * Dark color scheme optimized for OLED screens
 */
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,

    // Secondary colors
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    // Tertiary colors
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,

    // Background colors
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    // Error colors
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    // Other colors
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = Color.Black.copy(alpha = Alpha.Scrim)
)

/**
 * Light color scheme (maintained for potential future light mode)
 * Note: Currently not in use as the app forces dark mode for OLED optimization
 */
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = SandMedium,
    onPrimary = Color.White,
    primaryContainer = SandLight,
    onPrimaryContainer = SandDark,

    // Secondary colors
    secondary = CanaryBlue,
    onSecondary = Color.White,
    secondaryContainer = CanaryBlueLight,
    onSecondaryContainer = CanaryBlueDark,

    // Tertiary colors
    tertiary = LaurisilvaGreen,
    onTertiary = Color.White,
    tertiaryContainer = LaurisilvaGreenLight,
    onTertiaryContainer = LaurisilvaGreenDark,

    // Background colors
    background = Color(0xFFF8F8F8),
    onBackground = Color(0xFF1A1A1A),
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),

    // Other colors
    error = TraditionalRed,
    onError = Color.White
)

/**
 * Object for globally accessing theme properties
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

    // Extension functions for common theme operations

    /**
     * Returns a surface color with elevation effect for OLED screens
     * (subtle gradient rather than shadows)
     */
    @Composable
    fun elevatedSurfaceColor(elevation: Int): Color {
        return when(elevation) {
            0 -> DarkSurface1
            1 -> DarkSurface2
            2 -> DarkSurface3
            else -> DarkSurface4
        }
    }

    /**
     * Returns appropriate alpha value based on emphasis level
     */
    fun getAlpha(emphasis: Emphasis): Float {
        return when(emphasis) {
            Emphasis.HIGH -> Alpha.High
            Emphasis.MEDIUM -> Alpha.Medium
            Emphasis.LOW -> Alpha.Low
            Emphasis.DISABLED -> Alpha.Disabled
        }
    }
}

/**
 * Emphasis levels for content
 */
enum class Emphasis {
    HIGH, MEDIUM, LOW, DISABLED
}

/**
 * Main theme composable for the Wrestling app
 */
@Composable
fun WrestlingAppTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    // Use dark color scheme for OLED optimization
    val colorScheme = DarkColorScheme

    // Provide dimensions and shapes
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
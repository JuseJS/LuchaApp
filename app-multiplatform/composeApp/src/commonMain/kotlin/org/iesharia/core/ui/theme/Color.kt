package org.iesharia.core.ui.theme

import androidx.compose.ui.graphics.Color

/**
 * Base colors for the Lucha Canaria app
 */
// Pure black for optimal OLED energy efficiency
val PureBlack = Color(0xFF000000)

// Dark theme surface variations (from darkest to lightest)
val DarkSurface1 = PureBlack
val DarkSurface2 = Color(0xFF0A0A0A) // Slightly lighter than pure black
val DarkSurface3 = Color(0xFF121212) // Subtle elevation
val DarkSurface4 = Color(0xFF1A1A1A) // Higher elevation

// Identity colors (Canary Islands theme)
val SandLight = Color(0xFFE0C9A6)
val SandMedium = Color(0xFFAA8F66)
val SandDark = Color(0xFF6D5B42)

val CanaryBlue = Color(0xFF0F3C6E)
val CanaryBlueLight = Color(0xFF275A98)
val CanaryBlueDark = Color(0xFF072547)

val TraditionalRed = Color(0xFF8C2929)
val TraditionalRedLight = Color(0xFFB24C4C)
val TraditionalRedDark = Color(0xFF5E1C1C)

val LaurisilvaGreen = Color(0xFF0E5930)
val LaurisilvaGreenLight = Color(0xFF1A844A)
val LaurisilvaGreenDark = Color(0xFF072F1A)

// Accent colors
val AccentGold = Color(0xFFDAAA3F)
val AccentSilver = Color(0xFFB8B8B8)

// Standard content colors with defined opacity levels
val White100 = Color(0xFFFFFFFF)
val White90 = Color(0xE6FFFFFF)  // For primary content
val White80 = Color(0xCCFFFFFF)  // For secondary content
val White60 = Color(0x99FFFFFF)  // For tertiary/disabled content

/**
 * Semantic colors for dark theme
 */
// Background and surfaces
val DarkBackground = PureBlack
val DarkSurface = DarkSurface2
val DarkSurfaceVariant = DarkSurface3
val DarkSurfaceHighlight = DarkSurface4

// Primary and secondary colors
val DarkPrimary = SandLight
val DarkOnPrimary = SandDark
val DarkPrimaryContainer = SandMedium.copy(alpha = 0.2f)
val DarkOnPrimaryContainer = SandLight

val DarkSecondary = CanaryBlueLight
val DarkOnSecondary = Color(0xFF072547)
val DarkSecondaryContainer = CanaryBlue.copy(alpha = 0.2f)
val DarkOnSecondaryContainer = CanaryBlueLight

val DarkTertiary = LaurisilvaGreenLight
val DarkOnTertiary = LaurisilvaGreenDark
val DarkTertiaryContainer = LaurisilvaGreen.copy(alpha = 0.2f)
val DarkOnTertiaryContainer = LaurisilvaGreenLight

// Content colors
val DarkOnBackground = White90
val DarkOnSurface = White90
val DarkOnSurfaceVariant = White80
val DarkOnSurfaceDisabled = White60

// Error colors
val DarkError = TraditionalRedLight
val DarkOnError = TraditionalRedDark
val DarkErrorContainer = TraditionalRed.copy(alpha = 0.2f)
val DarkOnErrorContainer = TraditionalRedLight

// Outline colors
val DarkOutline = Color(0xFF353535)
val DarkOutlineVariant = Color(0xFF2A2A2A)

// Transparency constants for reuse throughout the app
object Alpha {
    const val High = 0.9f
    const val Medium = 0.7f
    const val Low = 0.5f
    const val Lowest = 0.2f
    const val Disabled = 0.38f
    const val Scrim = 0.6f
}
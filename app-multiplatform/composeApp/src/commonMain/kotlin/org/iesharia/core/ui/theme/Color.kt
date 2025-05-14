package org.iesharia.core.ui.theme

import androidx.compose.ui.graphics.Color

// El negro puro para fondos, optimizado para pantallas OLED
val PureBlack = Color(0xFF000000)
val DeepDark = Color(0xFF0A0A0A)
val CharcoalDark = Color(0xFF121212)

// Color arena - tonos más cálidos y suaves
val SandLight = Color(0xFFE0C9A6)
val SandDark = Color(0xFFAA8F66)
val SandDeep = Color(0xFF6D5B42)

// Azul canario - tonos más profundos y vibrantes
val CanaryBlue = Color(0xFF0F3C6E)
val CanaryBlueLight = Color(0xFF275A98)
val CanaryBlueDeep = Color(0xFF072547)

// Rojo tradicional - tonos más ricos y menos brillantes
val TraditionalRed = Color(0xFF8C2929)
val TraditionalRedLight = Color(0xFFB24C4C)
val TraditionalRedDeep = Color(0xFF5E1C1C)

// Verde laurisilva - tonos más profundos y naturales
val LaurisilvaGreen = Color(0xFF0E5930)
val LaurisilvaGreenLight = Color(0xFF1A844A)
val LaurisilvaGreenDeep = Color(0xFF072F1A)

// Colores de acento
val AccentGold = Color(0xFFDAAA3F)
val AccentSilver = Color(0xFFB8B8B8)

// Colores para el tema oscuro (optimizados para OLED)
val DarkBackground = PureBlack
val DarkSurface = CharcoalDark
val DarkPrimary = SandLight
val DarkSecondary = CanaryBlueLight
val DarkTertiary = LaurisilvaGreenLight
val DarkError = TraditionalRedLight

// Colores para textos y contenido
val DarkOnBackground = Color(0xFFF0F0F0)  // Ligeramente menos brillante que blanco puro
val DarkOnSurface = Color(0xFFE0E0E0)
val DarkOnPrimary = SandDeep
val DarkOnSecondary = CanaryBlueDeep
val DarkOnTertiary = LaurisilvaGreenDeep
val DarkOnError = TraditionalRedDeep

// Colores para elementos de la interfaz
val DarkSurfaceVariant = Color(0xFF1A1A1A)
val DarkOutline = Color(0xFF353535)
val DarkOutlineVariant = Color(0xFF2A2A2A)
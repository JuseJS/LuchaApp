package org.iesharia.core.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

/**
 * Shapes personalizados para la aplicación
 */
data class AppShapes(
    val small: CornerBasedShape = RoundedCornerShape(4.dp),
    val medium: CornerBasedShape = RoundedCornerShape(8.dp),
    val large: CornerBasedShape = RoundedCornerShape(16.dp),
    val extraLarge: CornerBasedShape = RoundedCornerShape(24.dp),

    // Formas personalizadas
    val buttonShape: CornerBasedShape = RoundedCornerShape(8.dp),
    val cardShape: CornerBasedShape = RoundedCornerShape(16.dp),
    val inputFieldShape: CornerBasedShape = RoundedCornerShape(8.dp),

    // Formas asymétricas
    val topRoundedShape: CornerBasedShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp
    ),
    val bottomRoundedShape: CornerBasedShape = RoundedCornerShape(
        topStart = 0.dp,
        topEnd = 0.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
)

/**
 * Composition Local para acceder a las formas desde cualquier parte de la app
 */
val LocalAppShapes = staticCompositionLocalOf { AppShapes() }
package org.iesharia.features.wrestlers.ui.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Devuelve un color basado en el porcentaje de efectividad
 */
@Composable
fun getEffectivenessColor(percentage: Double): Color {
    return when {
        percentage >= 80.0 -> MaterialTheme.colorScheme.primary
        percentage >= 60.0 -> MaterialTheme.colorScheme.secondary
        percentage >= 40.0 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }
}

/**
 * Obtiene el color basado en el rendimiento (alias para consistencia)
 */
@Composable
fun getPerformanceColor(percentage: Double): Color = getEffectivenessColor(percentage)
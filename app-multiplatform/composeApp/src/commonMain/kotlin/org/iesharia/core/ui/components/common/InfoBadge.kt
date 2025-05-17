package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Tipos de estado para badges
 */
enum class BadgeStatus {
    SUCCESS, WARNING, ERROR, NEUTRAL
}

/**
 * Badge unificado para información y estados
 */
@Composable
fun InfoBadge(
    text: String,
    modifier: Modifier = Modifier,
    // Parámetros para uso con BadgeStatus
    status: BadgeStatus? = null,
    // Parámetros para colores personalizados (se usan si status == null)
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = color.copy(alpha = 0.1f),
    fontWeight: FontWeight = FontWeight.Medium
) {
    // Determinar colores según el modo
    val (contentColor, bgColor) = if (status != null) {
        when (status) {
            BadgeStatus.SUCCESS -> Pair(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            )
            BadgeStatus.WARNING -> Pair(
                MaterialTheme.colorScheme.tertiary,
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
            )
            BadgeStatus.ERROR -> Pair(
                MaterialTheme.colorScheme.error,
                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
            )
            BadgeStatus.NEUTRAL -> Pair(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
            )
        }
    } else {
        Pair(color, backgroundColor)
    }

    Surface(
        shape = WrestlingTheme.shapes.small,
        color = bgColor,
        contentColor = contentColor,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = fontWeight,
            modifier = Modifier.padding(
                horizontal = WrestlingTheme.dimensions.spacing_8,
                vertical = WrestlingTheme.dimensions.spacing_4
            )
        )
    }
}
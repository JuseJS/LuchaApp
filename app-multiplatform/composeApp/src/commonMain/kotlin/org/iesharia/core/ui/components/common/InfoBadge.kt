package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.LuchaTheme

/**
 * Componente para mostrar etiquetas/badges informativos
 */
@Composable
fun InfoBadge(
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = color.copy(alpha = 0.1f),
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Surface(
        shape = LuchaTheme.shapes.small,
        color = backgroundColor,
        contentColor = color,
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = fontWeight,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

/**
 * Variante para mostrar estados (victoria, derrota, etc.)
 */
@Composable
fun StatusBadge(
    text: String,
    status: BadgeStatus = BadgeStatus.NEUTRAL,
    modifier: Modifier = Modifier
) {
    val (color, bgColor) = when (status) {
        BadgeStatus.SUCCESS -> MaterialTheme.colorScheme.primary to
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        BadgeStatus.WARNING -> MaterialTheme.colorScheme.tertiary to
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
        BadgeStatus.ERROR -> MaterialTheme.colorScheme.error to
                MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
        BadgeStatus.NEUTRAL -> MaterialTheme.colorScheme.secondary to
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
    }

    InfoBadge(
        text = text,
        color = color,
        backgroundColor = bgColor,
        modifier = modifier,
        fontWeight = FontWeight.Medium
    )
}

/**
 * Estados para el componente StatusBadge
 */
enum class BadgeStatus {
    SUCCESS, WARNING, ERROR, NEUTRAL
}
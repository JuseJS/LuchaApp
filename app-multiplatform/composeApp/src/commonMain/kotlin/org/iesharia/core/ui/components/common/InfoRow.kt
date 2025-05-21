package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle

/**
 * Estilos disponibles para el InfoRow
 */
enum class InfoRowStyle {
    HORIZONTAL,
    VERTICAL,
    CENTERED
}

/**
 * Componente reutilizable para mostrar información en formato etiqueta-valor
 * Actúa como un delegador para los diferentes estilos de implementación
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: InfoRowStyle = InfoRowStyle.HORIZONTAL,
    labelStyle: TextStyle = androidx.compose.material3.MaterialTheme.typography.bodySmall,
    valueStyle: TextStyle = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
    labelColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
    valueColor: Color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween
) {
    when (style) {
        InfoRowStyle.HORIZONTAL -> HorizontalInfoRow(
            label = label,
            value = value,
            modifier = modifier,
            icon = icon,
            labelStyle = labelStyle,
            valueStyle = valueStyle,
            labelColor = labelColor,
            valueColor = valueColor,
            horizontalArrangement = horizontalArrangement
        )
        InfoRowStyle.VERTICAL -> VerticalInfoRow(
            label = label,
            value = value,
            modifier = modifier,
            icon = icon,
            labelStyle = labelStyle,
            valueStyle = valueStyle,
            labelColor = labelColor,
            valueColor = valueColor
        )
        InfoRowStyle.CENTERED -> CenteredInfoRow(
            label = label,
            value = value,
            modifier = modifier,
            icon = icon,
            labelStyle = labelStyle,
            valueStyle = valueStyle,
            labelColor = labelColor,
            valueColor = valueColor
        )
    }
}
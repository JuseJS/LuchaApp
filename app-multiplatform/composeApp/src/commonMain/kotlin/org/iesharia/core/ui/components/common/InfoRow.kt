package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import org.iesharia.core.ui.theme.WrestlingTheme

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
 *
 * @param label Texto de la etiqueta
 * @param value Texto del valor
 * @param modifier Modificador para personalizar el componente
 * @param icon Icono opcional para mostrar junto a la etiqueta
 * @param style Estilo de presentación del componente
 * @param labelStyle Estilo del texto de la etiqueta
 * @param valueStyle Estilo del texto del valor
 * @param labelColor Color del texto de la etiqueta
 * @param valueColor Color del texto del valor
 * @param horizontalArrangement Alineación horizontal cuando se usa el estilo HORIZONTAL
 */
@Composable
fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    style: InfoRowStyle = InfoRowStyle.HORIZONTAL,
    labelStyle: TextStyle = MaterialTheme.typography.bodySmall,
    valueStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    labelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
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

@Composable
private fun HorizontalInfoRow(
    label: String,
    value: String,
    modifier: Modifier,
    icon: ImageVector?,
    labelStyle: TextStyle,
    valueStyle: TextStyle,
    labelColor: Color,
    valueColor: Color,
    horizontalArrangement: Arrangement.Horizontal
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono (si existe)
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = labelColor,
                    modifier = Modifier.padding(end = WrestlingTheme.dimensions.spacing_4)
                )
            }

            // Etiqueta
            Text(
                text = label,
                style = labelStyle,
                color = labelColor
            )
        }

        // Valor
        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun VerticalInfoRow(
    label: String,
    value: String,
    modifier: Modifier,
    icon: ImageVector?,
    labelStyle: TextStyle,
    valueStyle: TextStyle,
    labelColor: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier
    ) {
        // Fila con etiqueta e icono (si existe)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = labelColor,
                    modifier = Modifier.padding(end = WrestlingTheme.dimensions.spacing_4)
                )
            }

            Text(
                text = label,
                style = labelStyle,
                color = labelColor
            )
        }

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_2))

        // Valor
        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CenteredInfoRow(
    label: String,
    value: String,
    modifier: Modifier,
    icon: ImageVector?,
    labelStyle: TextStyle,
    valueStyle: TextStyle,
    labelColor: Color,
    valueColor: Color
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Etiqueta
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = labelColor,
                    modifier = Modifier.padding(end = WrestlingTheme.dimensions.spacing_4)
                )
            }

            Text(
                text = label,
                style = labelStyle,
                color = labelColor
            )
        }

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_2))

        // Valor
        Text(
            text = value,
            style = valueStyle,
            color = valueColor,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
    }
}
package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import org.iesharia.core.ui.theme.WrestlingTheme

@Composable
fun HorizontalInfoRow(
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
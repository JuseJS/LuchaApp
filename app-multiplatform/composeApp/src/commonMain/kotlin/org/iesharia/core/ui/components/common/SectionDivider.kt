package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente reutilizable para cabeceras de sección con una línea decorativa
 *
 * @param title Título de la sección
 * @param modifier Modificador para personalizar el componente
 * @param lineThickness Grosor de la línea decorativa
 * @param lineColor Color de la línea decorativa (por defecto, el color primario del tema)
 */
@Composable
fun SectionDivider(
    title: String,
    modifier: Modifier = Modifier,
    leftLineWeight: Float = 0.1f,
    rightLineWeight: Float = 1f,
    lineThickness: Dp = 3.dp,
    leftLineColor: Color = MaterialTheme.colorScheme.primary,
    rightLineColor: Color = MaterialTheme.colorScheme.outlineVariant,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    textColor: Color = MaterialTheme.colorScheme.onBackground
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(leftLineWeight),
            thickness = lineThickness,
            color = leftLineColor
        )

        Text(
            text = title,
            style = textStyle,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_8)
        )

        HorizontalDivider(
            modifier = Modifier.weight(rightLineWeight),
            color = rightLineColor
        )
    }
}
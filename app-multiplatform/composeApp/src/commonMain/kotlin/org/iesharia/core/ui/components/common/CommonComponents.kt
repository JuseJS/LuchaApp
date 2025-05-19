package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.ui.theme.White60
import org.iesharia.core.ui.theme.White80
import org.iesharia.core.ui.theme.White90
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componentes comunes optimizados para su reutilización en la aplicación
 */

/**
 * Componente para mostrar mensajes de estado vacío con formato consistente
 */
@Composable
fun EmptyStateMessage(
    message: String,
    modifier: Modifier = Modifier,
    color: Color = White60
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    )
}

/**
 * Componente para mostrar una etiqueta con valor (par clave-valor)
 */
@Composable
fun LabeledValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    labelColor: Color = White80,
    valueColor: Color = White90
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        color = labelColor,
        modifier = modifier
    )
}
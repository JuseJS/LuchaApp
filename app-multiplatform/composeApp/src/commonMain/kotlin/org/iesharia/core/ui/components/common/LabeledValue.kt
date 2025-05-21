package org.iesharia.core.ui.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.iesharia.core.ui.theme.White80
import org.iesharia.core.ui.theme.White90

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
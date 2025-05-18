package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.ui.theme.White60
import org.iesharia.core.ui.theme.White80
import org.iesharia.core.ui.theme.White90
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componentes comunes optimizados para su reutilización en la aplicación
 */

/**
 * Componente para mostrar un título de sección con formato consistente
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier,
    color: Color = White90
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    )
}

/**
 * Componente para mostrar un subtítulo con formato consistente
 */
@Composable
fun SectionSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary
) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.titleMedium,
        color = color,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    )
    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
}

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
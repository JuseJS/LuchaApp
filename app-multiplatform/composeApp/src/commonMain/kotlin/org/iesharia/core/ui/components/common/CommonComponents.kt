package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.ui.theme.LuchaTheme

/**
 * Componentes comunes optimizados para su reutilización en la aplicación
 */

/**
 * Componente para mostrar un título de sección con formato consistente
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
    )
}

/**
 * Componente para mostrar un subtítulo con formato consistente
 */
@Composable
fun SectionSubtitle(
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = subtitle,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.secondary,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
    )
    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))
}

/**
 * Componente para mostrar mensajes de estado vacío con formato consistente
 */
@Composable
fun EmptyStateMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
    )
}

/**
 * Componente para mostrar una etiqueta con valor (par clave-valor)
 */
@Composable
fun LabeledValue(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$label: $value",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier
    )
}
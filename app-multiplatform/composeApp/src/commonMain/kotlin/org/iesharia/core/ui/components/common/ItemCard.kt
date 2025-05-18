package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente Card unificado con opción clickable/no-clickable y alineaciones configurables
 */
@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(WrestlingTheme.dimensions.spacing_16),
    containerColor: Color = Color.Unspecified,
    titleAlignment: Alignment.Horizontal = Alignment.Start, // Nuevo parámetro
    contentAlignment: Alignment.Horizontal = Alignment.Start, // Nuevo parámetro
    content: @Composable ColumnScope.() -> Unit = {}
) {
    val cardColors = if (containerColor != Color.Unspecified) {
        CardDefaults.cardColors(containerColor = containerColor)
    } else {
        CardDefaults.cardColors()
    }

    if (onClick != null) {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = WrestlingTheme.shapes.cardShape,
            onClick = onClick,
            colors = cardColors
        ) {
            CardContent(title, subtitle, contentPadding, titleAlignment, contentAlignment, content)
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = WrestlingTheme.shapes.cardShape,
            colors = cardColors
        ) {
            CardContent(title, subtitle, contentPadding, titleAlignment, contentAlignment, content)
        }
    }
}

@Composable
private fun CardContent(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)?,
    contentPadding: PaddingValues,
    titleAlignment: Alignment.Horizontal,
    contentAlignment: Alignment.Horizontal,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding),
        horizontalAlignment = titleAlignment
    ) {
        title()
        subtitle?.invoke()

        // Contenido principal con su propio alineamiento
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = contentAlignment
        ) {
            content()
        }
    }
}
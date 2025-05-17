package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente Card unificado con opción clickable/no-clickable
 */
@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(WrestlingTheme.dimensions.spacing_16),
    containerColor: Color = Color.Unspecified,
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
            CardContent(title, subtitle, contentPadding, content)
        }
    } else {
        Card(
            modifier = modifier.fillMaxWidth(),
            shape = WrestlingTheme.shapes.cardShape,
            colors = cardColors
        ) {
            CardContent(title, subtitle, contentPadding, content)
        }
    }
}

@Composable
private fun CardContent(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)?,
    contentPadding: PaddingValues,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
    ) {
        title()
        subtitle?.invoke()
        content()
    }
}
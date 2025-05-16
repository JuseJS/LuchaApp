package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.iesharia.core.ui.theme.LuchaTheme

/**
 * Componente Card base para elementos con estructura común
 */
@Composable
fun ItemCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(LuchaTheme.dimensions.spacing_16),
    containerColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = LuchaTheme.shapes.cardShape,
        onClick = onClick,
        colors = if(containerColor != Color.Unspecified)
            CardDefaults.cardColors(containerColor = containerColor)
        else CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
        ) {
            // Título
            title()

            // Subtítulo opcional
            subtitle?.invoke()

            // Contenido principal
            content()
        }
    }
}

/**
 * Variante de ItemCard sin evento onClick para elementos no clicables
 */
@Composable
fun StaticItemCard(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(LuchaTheme.dimensions.spacing_16),
    containerColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = LuchaTheme.shapes.cardShape,
        colors = if(containerColor != Color.Unspecified)
            CardDefaults.cardColors(containerColor = containerColor)
        else CardDefaults.cardColors()
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
}
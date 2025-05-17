package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente para mostrar una lista de elementos en una sección con título
 */
@Composable
fun <T> SectionList(
    items: List<T>,
    title: String,
    emptyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionTitle(title = title)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        if (items.isEmpty()) {
            emptyContent()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_12)
            ) {
                items.forEach { item ->
                    itemContent(item)
                }
            }
        }
    }
}

/**
 * Variante de SectionList que usa un subtítulo en lugar de un título
 */
@Composable
fun <T> SubSectionList(
    items: List<T>,
    subtitle: String,
    emptyContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 0.dp),
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        SectionSubtitle(
            subtitle = subtitle,
            modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
        )

        if (items.isEmpty()) {
            emptyContent()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                items.forEach { item ->
                    itemContent(item)
                }
            }
        }
    }
}
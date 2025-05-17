package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Tipo de sección para reutilización
 */
enum class SectionType {
    PRIMARY,   // Título principal
    SECONDARY  // Subtítulo
}

/**
 * Componente de sección unificado
 */
@Composable
fun <T> SectionList(
    items: List<T>,
    title: String,
    modifier: Modifier = Modifier,
    type: SectionType = SectionType.PRIMARY,
    emptyContent: @Composable () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        horizontal = if (type == SectionType.PRIMARY)
            WrestlingTheme.dimensions.spacing_16 else 0.dp
    ),
    itemContent: @Composable (T) -> Unit
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Título según tipo
        if (type == SectionType.PRIMARY) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        } else {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
        }

        // Contenido
        if (items.isEmpty()) {
            emptyContent()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                verticalArrangement = Arrangement.spacedBy(
                    if (type == SectionType.PRIMARY)
                        WrestlingTheme.dimensions.spacing_12
                    else
                        WrestlingTheme.dimensions.spacing_8
                )
            ) {
                items.forEach { item ->
                    itemContent(item)
                }
            }
        }
    }
}
package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.iesharia.core.ui.theme.DarkSurface2
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente genérico para mostrar elementos de entidad en listas o grids
 * Simplifica la creación de elementos consistentes reutilizando ItemCard
 */
@Composable
fun EntityListItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = DarkSurface2,
    leadingContent: @Composable (() -> Unit)? = null,
    titleContent: @Composable () -> Unit,
    subtitleContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    detailContent: @Composable (ColumnScope.() -> Unit)? = null
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        titleAlignment = Alignment.Start,
        contentAlignment = Alignment.Start,
        contentPadding = PaddingValues(
            horizontal = WrestlingTheme.dimensions.spacing_16,
            vertical = WrestlingTheme.dimensions.spacing_12
        ),
        title = {
            // Layout horizontal para todos los casos, con mejor distribución del espacio
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                // Contenido principal (icono o imagen) a la izquierda
                if (leadingContent != null) {
                    leadingContent()
                    Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_16))
                }

                // Columna de título y subtítulo en el centro
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    titleContent()
                    if (subtitleContent != null) {
                        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))
                        subtitleContent()
                    }
                }

                // Contenido secundario (chips, etc.) a la derecha
                if (trailingContent != null) {
                    trailingContent()
                }
            }
        }
    ) {
        // Contenido detallado opcional
        if (detailContent != null) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))
            detailContent()
        }
    }
}
package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente de filtro por isla
 */
@Composable
fun IslandFilter(
    selectedIsland: Island?,
    onIslandSelected: (Island?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = AppStrings.Competitions.island,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
        ) {
            Island.entries.forEach { island ->
                FilterChip(
                    selected = selectedIsland == island,
                    onClick = {
                        onIslandSelected(if (selectedIsland == island) null else island)
                    },
                    label = { Text(island.displayName()) }
                )
            }
        }
    }
}
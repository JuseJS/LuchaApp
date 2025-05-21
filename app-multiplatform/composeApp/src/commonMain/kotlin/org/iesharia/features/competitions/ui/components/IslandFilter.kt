package org.iesharia.features.competitions.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.FilterSelector

/**
 * Componente de filtro por isla, implementado usando el
 * componente genérico FilterSelector para reducir código duplicado.
 */
@Composable
fun IslandFilter(
    selectedIsland: Island?,
    onIslandSelected: (Island?) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterSelector(
        title = AppStrings.Competitions.island,
        options = Island.entries,
        selectedOption = selectedIsland,
        optionToString = { it.displayName() },
        onOptionSelected = onIslandSelected,
        modifier = modifier
    )
}
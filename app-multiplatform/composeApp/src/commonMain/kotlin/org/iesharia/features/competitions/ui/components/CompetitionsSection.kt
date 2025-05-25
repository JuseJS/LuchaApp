package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.common.domain.model.CompetitionFilters

@Composable
fun CompetitionsSection(
    competitions: List<Competition>,
    currentFilters: CompetitionFilters,
    onFilterChanged: (ageCategory: AgeCategory?, divisionCategory: DivisionCategory?, island: Island?) -> Unit,
    onClearFilters: () -> Unit,
    onCompetitionClick: (Competition) -> Unit,
    modifier: Modifier = Modifier,
    showEmptyState: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Título y botón de filtro
        CompetitionSectionHeader(
            hasActiveFilters = currentFilters.ageCategory != null ||
                    currentFilters.divisionCategory != null ||
                    currentFilters.island != null,
            onFilterClick = { showFiltersDialog ->
                showFiltersDialog.value = true
            }
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Diálogo de filtros
        val showFiltersDialog = remember { mutableStateOf(false) }
        if (showFiltersDialog.value) {
            FiltersDialog(
                currentFilters = currentFilters,
                onFilterChanged = onFilterChanged,
                onClearFilters = onClearFilters,
                onDismiss = { showFiltersDialog.value = false }
            )
        }

        // Solo mostrar el estado vacío si showEmptyState es true
        if (competitions.isEmpty() && showEmptyState) {
            EmptyStateMessage(message = AppStrings.Home.noCompetitionsWithFilter)
        }
    }
}
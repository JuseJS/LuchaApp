package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.home.ui.viewmodel.CompetitionFilters

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

@Composable
private fun CompetitionSectionHeader(
    hasActiveFilters: Boolean,
    onFilterClick: (MutableState<Boolean>) -> Unit
) {
    val showFiltersDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SectionDivider(
            title = AppStrings.Competitions.activeCompetitions,
            type = SectionDividerType.PRIMARY,
            modifier = Modifier.weight(1f)
        )

        IconButton(onClick = { onFilterClick(showFiltersDialog) }) {
            if (hasActiveFilters) {
                BadgedBox(
                    badge = {
                        Badge {
                            Text("!")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = AppStrings.Competitions.filterCompetitions
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = AppStrings.Competitions.filterCompetitions
                )
            }
        }
    }
}

@Composable
fun FiltersDialog(
    currentFilters: CompetitionFilters,
    onFilterChanged: (ageCategory: AgeCategory?, divisionCategory: DivisionCategory?, island: Island?) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedAgeCategory by remember { mutableStateOf(currentFilters.ageCategory) }
    var selectedDivisionCategory by remember { mutableStateOf(currentFilters.divisionCategory) }
    var selectedIsland by remember { mutableStateOf(currentFilters.island) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppStrings.Competitions.filterCompetitions) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Filtro de categoría por edad
                Text(
                    text = AppStrings.Competitions.category,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                ) {
                    AgeCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedAgeCategory == category,
                            onClick = {
                                selectedAgeCategory = if (selectedAgeCategory == category) null else category
                            },
                            label = { Text(category.displayName()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de división
                Text(
                    text = AppStrings.Competitions.division,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                ) {
                    DivisionCategory.entries.forEach { division ->
                        FilterChip(
                            selected = selectedDivisionCategory == division,
                            onClick = {
                                selectedDivisionCategory = if (selectedDivisionCategory == division) null else division
                            },
                            label = { Text(division.displayName()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de isla
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
                                selectedIsland = if (selectedIsland == island) null else island
                            },
                            label = { Text(island.displayName()) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onFilterChanged(selectedAgeCategory, selectedDivisionCategory, selectedIsland)
                    onDismiss()
                }
            ) {
                Text(AppStrings.Common.apply)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClearFilters()
                    onDismiss()
                }
            ) {
                Text(AppStrings.Competitions.clearFilters)
            }
        }
    )
}
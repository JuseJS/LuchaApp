package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.home.ui.viewmodel.CompetitionFilters

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
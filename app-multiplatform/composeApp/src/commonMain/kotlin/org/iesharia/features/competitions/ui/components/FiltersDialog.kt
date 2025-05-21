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
                AgeCategoryFilter(
                    selectedCategory = selectedAgeCategory,
                    onCategorySelected = { selectedAgeCategory = it }
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de división
                DivisionCategoryFilter(
                    selectedDivision = selectedDivisionCategory,
                    onDivisionSelected = { selectedDivisionCategory = it }
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de isla
                IslandFilter(
                    selectedIsland = selectedIsland,
                    onIslandSelected = { selectedIsland = it }
                )
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
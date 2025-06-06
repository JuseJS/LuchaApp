package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Island
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.FilterSelector
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.common.domain.model.CompetitionFilters

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

    // Utilizar los componentes FilterSelector existentes en lugar de implementación manual
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(AppStrings.Competitions.filterCompetitions) },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Filtro de categoría por edad
                FilterSelector(
                    title = AppStrings.Competitions.category,
                    options = AgeCategory.entries,
                    selectedOption = selectedAgeCategory,
                    optionToString = { it.displayName() },
                    onOptionSelected = { selectedAgeCategory = it }
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de división
                FilterSelector(
                    title = AppStrings.Competitions.division,
                    options = DivisionCategory.entries,
                    selectedOption = selectedDivisionCategory,
                    optionToString = { it.displayName() },
                    onOptionSelected = { selectedDivisionCategory = it }
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                // Filtro de isla
                FilterSelector(
                    title = AppStrings.Competitions.island,
                    options = Island.entries,
                    selectedOption = selectedIsland,
                    optionToString = { it.displayName() },
                    onOptionSelected = { selectedIsland = it }
                )
            }
        },
        confirmButton = {
            WrestlingButton(
                text = AppStrings.Common.apply,
                onClick = {
                    onFilterChanged(selectedAgeCategory, selectedDivisionCategory, selectedIsland)
                    onDismiss()
                },
                type = WrestlingButtonType.PRIMARY
            )
        },
        dismissButton = {
            WrestlingButton(
                text = AppStrings.Competitions.clearFilters,
                onClick = {
                    onClearFilters()
                    onDismiss()
                },
                type = WrestlingButtonType.TEXT
            )
        }
    )
}
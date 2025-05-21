package org.iesharia.features.competitions.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.FilterSelector
import org.iesharia.features.competitions.domain.model.DivisionCategory

/**
 * Componente de filtro por categoría de división, implementado usando el
 * componente genérico FilterSelector para reducir código duplicado.
 */
@Composable
fun DivisionCategoryFilter(
    selectedDivision: DivisionCategory?,
    onDivisionSelected: (DivisionCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterSelector(
        title = AppStrings.Competitions.division,
        options = DivisionCategory.entries,
        selectedOption = selectedDivision,
        optionToString = { it.displayName() },
        onOptionSelected = onDivisionSelected,
        modifier = modifier
    )
}
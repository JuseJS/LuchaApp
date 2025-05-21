package org.iesharia.features.competitions.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.FilterSelector
import org.iesharia.features.competitions.domain.model.AgeCategory

/**
 * Componente de filtro por categoría de edad, implementado usando el 
 * componente genérico FilterSelector para reducir código duplicado.
 */
@Composable
fun AgeCategoryFilter(
    selectedCategory: AgeCategory?,
    onCategorySelected: (AgeCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterSelector(
        title = AppStrings.Competitions.category,
        options = AgeCategory.entries,
        selectedOption = selectedCategory,
        optionToString = { it.displayName() },
        onOptionSelected = onCategorySelected,
        modifier = modifier
    )
}
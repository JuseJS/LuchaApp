package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.AgeCategory

/**
 * Componente de filtro por categoría de edad
 */
@Composable
fun AgeCategoryFilter(
    selectedCategory: AgeCategory?,
    onCategorySelected: (AgeCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(if (selectedCategory == category) null else category)
                    },
                    label = { Text(category.displayName()) }
                )
            }
        }
    }
}
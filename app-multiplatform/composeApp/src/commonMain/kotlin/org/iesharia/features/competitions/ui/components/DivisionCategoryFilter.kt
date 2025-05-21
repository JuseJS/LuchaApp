package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.DivisionCategory

/**
 * Componente de filtro por categoría de división
 */
@Composable
fun DivisionCategoryFilter(
    selectedDivision: DivisionCategory?,
    onDivisionSelected: (DivisionCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
                    selected = selectedDivision == division,
                    onClick = {
                        onDivisionSelected(if (selectedDivision == division) null else division)
                    },
                    label = { Text(division.displayName()) }
                )
            }
        }
    }
}
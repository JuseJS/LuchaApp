package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme

@Composable
fun CompetitionSectionHeader(
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
package org.iesharia.features.home.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.*
import org.iesharia.features.home.ui.viewmodel.FavoriteType

@Composable
fun FavoritesSectionHeader(
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = WrestlingTheme.dimensions.spacing_16)
    ) {
        SectionDivider(
            title = AppStrings.Home.favorites,
            type = SectionDividerType.PRIMARY
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

        // Filtros de tipo de favorito
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
        ) {
            FavoriteType.entries.forEach { type ->
                FilterChip(
                    selected = type == selectedType,
                    onClick = { onTypeSelected(type) },
                    label = {
                        Text(
                            text = type.displayName(),
                            color = if (type == selectedType) White90 else White80
                        )
                    },
                    modifier = Modifier.padding(end = WrestlingTheme.dimensions.spacing_8),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        containerColor = DarkSurface2
                    )
                )
            }
        }
    }
}

@Composable
fun FavoritesSectionDivider() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        HorizontalDivider(color = DarkOutline)
        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
    }
}

// Helper functions for checking favorite types
fun shouldShowCompetitions(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.COMPETITIONS
}

fun shouldShowTeams(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.TEAMS
}

fun shouldShowWrestlers(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.WRESTLERS
}
package org.iesharia.features.home.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionTitle
import org.iesharia.features.home.ui.viewmodel.FavoriteType
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.core.resources.AppStrings

@Composable
fun FavoritesSectionHeader(
    selectedType: FavoriteType,
    onTypeSelected: (FavoriteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = LuchaTheme.dimensions.spacing_16)
    ) {
        // Título de la sección
        SectionTitle(
            title = AppStrings.Home.favorites
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_12))

        // Filtros de tipo de favorito
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = LuchaTheme.dimensions.spacing_16)
        ) {
            FavoriteType.entries.forEach { type ->
                FilterChip(
                    selected = type == selectedType,
                    onClick = { onTypeSelected(type) },
                    label = { Text(type.displayName()) },
                    modifier = Modifier.padding(end = LuchaTheme.dimensions.spacing_8)
                )
            }
        }
    }
}

@Composable
fun EmptyFavorites(selectedType: FavoriteType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        contentAlignment = Alignment.Center
    ) {
        EmptyStateMessage(
            AppStrings.Home.noFavorites.format(selectedType.displayName().lowercase())
        )
    }
}

@Composable
fun FavoritesSectionDivider() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
    }
}

fun shouldShowCompetitions(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.COMPETITIONS
}

fun shouldShowTeams(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.TEAMS
}

fun shouldShowWrestlers(selectedType: FavoriteType): Boolean {
    return selectedType == FavoriteType.ALL || selectedType == FavoriteType.WRESTLERS
}
package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.DarkSurface3
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.home.ui.components.FavoritesSectionDivider
import org.iesharia.features.home.ui.components.FavoritesSectionHeader
import org.iesharia.features.home.ui.components.shouldShowCompetitions
import org.iesharia.features.home.ui.components.shouldShowTeams
import org.iesharia.features.home.ui.components.shouldShowWrestlers
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.ui.components.WrestlerItem

/**
 * Sección de favoritos en la pantalla principal
 */
@Composable
fun HomeFavoritesSection(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Surface(
        color = DarkSurface3,
        shape = WrestlingTheme.shapes.medium,
        modifier = modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Encabezado de sección de favoritos con filtros
            FavoritesSectionHeader(
                selectedType = uiState.selectedFavoriteType,
                onTypeSelected = { viewModel.setFavoriteType(it) }
            )

            // Contenido de favoritos basado en el filtro seleccionado
            val favorites = viewModel.getFilteredFavorites()

            if (favorites.isEmpty()) {
                EmptyStateMessage(
                    message = AppStrings.Home.noFavorites.format(uiState.selectedFavoriteType.displayName().lowercase())
                )
            } else {
                // Mostrar favoritos por tipo
                val competitions = favorites.filterIsInstance<Favorite.CompetitionFavorite>()
                val teams = favorites.filterIsInstance<Favorite.TeamFavorite>()
                val wrestlers = favorites.filterIsInstance<Favorite.WrestlerFavorite>()

                // Sección de competiciones favoritas
                if (competitions.isNotEmpty() && shouldShowCompetitions(uiState.selectedFavoriteType)) {
                    DisplayFavoriteCompetitions(
                        competitions = competitions,
                        viewModel = viewModel
                    )

                    if ((teams.isNotEmpty() && shouldShowTeams(uiState.selectedFavoriteType)) ||
                        (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType))) {
                        FavoritesSectionDivider()
                    }
                }

                // Sección de equipos favoritos
                if (teams.isNotEmpty() && shouldShowTeams(uiState.selectedFavoriteType)) {
                    SectionDivider(
                        title = AppStrings.Teams.favoriteTeams,
                        type = SectionDividerType.TITLE
                    )

                    TeamsByDivisionSection(
                        title = "",
                        allTeams = teams.map { it.team },
                        onTeamClick = { viewModel.navigateToTeamDetail(it) }
                    )

                    if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                        FavoritesSectionDivider()
                    }
                }

                // Sección de luchadores favoritos
                if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                    DisplayFavoriteWrestlers(
                        wrestlers = wrestlers,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
private fun DisplayFavoriteCompetitions(
    competitions: List<Favorite.CompetitionFavorite>,
    viewModel: HomeViewModel
) {
    SectionDivider(
        title = AppStrings.Competitions.favoriteCompetitions,
        type = SectionDividerType.TITLE
    )

    Column(
        modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
        verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_12)
    ) {
        competitions.forEach { favorite ->
            org.iesharia.features.competitions.ui.components.CompetitionItem(
                competition = favorite.competition,
                onClick = { viewModel.navigateToCompetitionDetail(favorite.competition.id) },
                showMatchDays = true
            )
        }
    }
}

@Composable
private fun DisplayFavoriteWrestlers(
    wrestlers: List<Favorite.WrestlerFavorite>,
    viewModel: HomeViewModel
) {
    SectionDivider(
        title = AppStrings.Wrestlers.favoriteWrestlers,
        type = SectionDividerType.TITLE
    )

    // Group wrestlers by category
    val wrestlersByCategory = wrestlers.map { it.wrestler }.groupBy { it.category }

    WrestlerCategory.entries.forEach { category ->
        val wrestlersInCategory = wrestlersByCategory[category] ?: emptyList()

        if (wrestlersInCategory.isNotEmpty()) {
            SectionDivider(
                title = category.displayName(),
                type = SectionDividerType.SUBTITLE
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            val itemsPerRow = 3
            val rowHeight = 150.dp
            val numRows = (wrestlersInCategory.size + itemsPerRow - 1) / itemsPerRow
            val gridHeight = (numRows * rowHeight) + ((numRows - 1) * WrestlingTheme.dimensions.spacing_8)

            LazyVerticalGrid(
                columns = GridCells.Fixed(itemsPerRow),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(gridHeight.coerceAtMost(330.dp)),
                contentPadding = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
            ) {
                items(wrestlersInCategory) { wrestler ->
                    WrestlerItem(
                        wrestler = wrestler,
                        onClick = { viewModel.navigateToWrestlerDetail(wrestler.id) },
                        isGridItem = true
                    )
                }
            }
        }
    }
}
package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SearchBar
import org.iesharia.core.ui.theme.DarkSurface2
import org.iesharia.core.ui.theme.White90
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.auth.domain.model.Permission
import org.iesharia.features.auth.ui.viewmodel.RoleBasedUIHelper
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.competitions.ui.components.CompetitionsSection
import org.iesharia.features.home.ui.viewmodel.HomeViewModel

/**
 * Contenido principal de la pantalla de inicio
 */
@Composable
fun HomeContent(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Barra de búsqueda
        item {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) },
                placeholder = "Buscar competiciones, equipos o luchadores...",
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                backgroundColor = DarkSurface2,
                contentColor = White90
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Resultados de búsqueda (condicional)
        if (uiState.searchQuery.isNotBlank()) {
            item {
                HomeSearchResults(viewModel = viewModel)
            }
        }

        // Sección de favoritos (condicional según permisos)
        item {
            RoleBasedUIHelper.WithPermission(Permission.MANAGE_FAVORITES) {
                HomeFavoritesSection(viewModel = viewModel)

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
            }
        }

        // Sección de competiciones con filtros
        item {
            CompetitionsSection(
                competitions = emptyList(),
                currentFilters = uiState.filters,
                onFilterChanged = viewModel::updateFilters,
                onClearFilters = viewModel::clearFilters,
                onCompetitionClick = { /* No se usa aquí */ },
                showEmptyState = false
            )
        }

        // Lista de competiciones
        val filteredCompetitions = viewModel.getFilteredCompetitions()
        if (filteredCompetitions.isEmpty()) {
            item {
                EmptyStateMessage(
                    message = "No se encontraron competiciones con los filtros seleccionados"
                )
            }
        } else {
            items(filteredCompetitions) { competition ->
                CompetitionItem(
                    competition = competition,
                    onClick = { viewModel.navigateToCompetitionDetail(competition.id) },
                    showMatchDays = true,
                    onMatchClick = { matchId -> viewModel.navigateToMatchDetail(matchId) }
                )
            }
        }

        // Sección de equipos por división
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Get all teams from all competitions
            val allTeams = uiState.competitions.flatMap { it.teams }.distinctBy { it.id }

            // Use the TeamsByDivisionSection component
            TeamsByDivisionSection(
                title = org.iesharia.core.resources.AppStrings.Home.teams,
                allTeams = allTeams,
                onTeamClick = { viewModel.navigateToTeamDetail(it) }
            )
        }

        // Espaciado al final
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}
package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SearchBar
import org.iesharia.core.ui.components.common.SectionSubtitle
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.*
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.competitions.ui.components.CompetitionsSection
import org.iesharia.features.competitions.ui.components.EmptyCompetitions
import org.iesharia.features.home.ui.components.*
import org.iesharia.features.home.ui.viewmodel.HomeUiState
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.teams.ui.components.TeamGridCard
import org.iesharia.features.teams.ui.components.TeamItem
import org.iesharia.features.wrestlers.ui.components.WrestlerItem

/**
 * Pantalla principal de la aplicación, optimizada para pantallas OLED
 */
class HomeScreen : BaseContentScreen() {

    private lateinit var viewModel: HomeViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<HomeViewModel>()
    }

    @Composable
    override fun ScreenTitle(): String {
        return AppStrings.Common.appName
    }

    @Composable
    override fun OnNavigateBack(): (() -> Unit)? {
        return null // No hay navegación hacia atrás desde la pantalla principal
    }

    @Composable
    override fun TopBarActions() {
        // No hay acciones para esta pantalla
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        // Aplicamos el color de fondo negro puro para optimizar pantallas OLED
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            HomeContent(
                uiState = uiState,
                viewModel = viewModel
            )
        }
    }
}

/**
 * Contenido principal de la pantalla de inicio
 */
@Composable
private fun HomeContent(
    uiState: HomeUiState,
    viewModel: HomeViewModel
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
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

        // Resultados de búsqueda (solo se muestran si hay una búsqueda activa)
        if (uiState.searchQuery.isNotBlank()) {
            item {
                val (competitions, teams, wrestlers) = viewModel.getSearchResults()
                val hasResults = competitions.isNotEmpty() || teams.isNotEmpty() || wrestlers.isNotEmpty()

                if (hasResults) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Título principal
                        Text(
                            text = "Resultados de búsqueda",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = White90,
                            modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                        )

                        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                        // Competiciones encontradas
                        if (competitions.isNotEmpty()) {
                            Text(
                                text = "Competiciones encontradas",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                            competitions.take(3).forEach { competition ->
                                CompetitionItem(
                                    competition = competition,
                                    onClick = { viewModel.navigateToCompetitionDetail(competition.id) },
                                    modifier = Modifier.padding(
                                        horizontal = WrestlingTheme.dimensions.spacing_16,
                                        vertical = WrestlingTheme.dimensions.spacing_4
                                    ),
                                    showMatchDays = false
                                )
                            }

                            if (teams.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                            }
                        }

                        // Equipos encontrados
                        if (teams.isNotEmpty()) {
                            Text(
                                text = "Equipos encontrados",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                            // Grid de equipos
                            val rowHeight = 72.dp
                            val numRows = (teams.size + 1) / 2
                            val gridHeight = (numRows * (rowHeight + WrestlingTheme.dimensions.spacing_8)) + WrestlingTheme.dimensions.spacing_8

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(gridHeight.coerceAtMost(216.dp)),
                                contentPadding = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
                                horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                            ) {
                                items(teams) { team ->
                                    TeamGridCard(
                                        team = team,
                                        onClick = { viewModel.navigateToTeamDetail(team.id) }
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Mensaje cuando no hay resultados
                    EmptyStateMessage(
                        message = "No se encontraron resultados para '${uiState.searchQuery}'",
                        modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                    )
                }

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
            }
        }

        // Sección de favoritos
        item {
            Surface(
                color = DarkSurface3,
                shape = WrestlingTheme.shapes.medium,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
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
                        EmptyFavorites(uiState.selectedFavoriteType)
                    } else {
                        // Mostrar favoritos por tipo
                        val competitions = favorites.filterIsInstance<Favorite.CompetitionFavorite>()
                        val teams = favorites.filterIsInstance<Favorite.TeamFavorite>()
                        val wrestlers = favorites.filterIsInstance<Favorite.WrestlerFavorite>()

                        // Sección de competiciones favoritas
                        if (competitions.isNotEmpty() && shouldShowCompetitions(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Competitions.favoriteCompetitions,
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_12)
                            ) {
                                competitions.forEach { favorite ->
                                    CompetitionItem(
                                        competition = favorite.competition,
                                        onClick = { viewModel.navigateToCompetitionDetail(favorite.competition.id) },
                                        showMatchDays = true
                                    )
                                }
                            }

                            if ((teams.isNotEmpty() && shouldShowTeams(uiState.selectedFavoriteType)) ||
                                (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType))) {
                                FavoritesSectionDivider()
                            }
                        }

                        // Sección de equipos favoritos
                        if (teams.isNotEmpty() && shouldShowTeams(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Teams.favoriteTeams,
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_12)
                            ) {
                                teams.forEach { favorite ->
                                    val teamId = favorite.team.id
                                    val lastMatches = viewModel.getTeamLastMatches(teamId)
                                    val nextMatches = viewModel.getTeamNextMatches(teamId)

                                    TeamItem(
                                        team = favorite.team,
                                        onClick = { viewModel.navigateToTeamDetail(favorite.team.id) },
                                        lastMatches = lastMatches.take(2),
                                        nextMatches = nextMatches.take(2)
                                    )
                                }
                            }

                            if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                                FavoritesSectionDivider()
                            }
                        }

                        // Sección de luchadores favoritos
                        if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Wrestlers.favoriteWrestlers,
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_12)
                            ) {
                                wrestlers.forEach { favorite ->
                                    val wrestlerId = favorite.wrestler.id
                                    val results = viewModel.getWrestlerResults(wrestlerId)

                                    WrestlerItem(
                                        wrestler = favorite.wrestler,
                                        onClick = { viewModel.navigateToWrestlerDetail(favorite.wrestler.id) },
                                        matchResults = results
                                    )
                                }
                            }
                        }
                    }

                    // Espaciado inferior
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Divisor de sección
            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = WrestlingTheme.dimensions.spacing_16,
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
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
                EmptyCompetitions()
            }
        } else {
            items(filteredCompetitions) { competition ->
                CompetitionItem(
                    competition = competition,
                    onClick = { viewModel.navigateToCompetitionDetail(competition.id) },
                    modifier = Modifier.padding(
                        horizontal = WrestlingTheme.dimensions.spacing_16,
                        vertical = WrestlingTheme.dimensions.spacing_8
                    )
                )
            }
        }

        // Nueva sección de equipos en grid
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Título de la sección
            Text(
                text = "Equipos Participantes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = White90,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Obtener todos los equipos de todas las competiciones
            val allTeams = uiState.competitions.flatMap { it.teams }.distinctBy { it.id }

            if (allTeams.isEmpty()) {
                EmptyStateMessage(
                    message = "No hay equipos disponibles"
                )
            } else {
                // Calcular altura aproximada del grid
                val rowHeight = 72.dp
                val numRows = (allTeams.size + 1) / 2 // +1 y división entera para redondear hacia arriba
                val gridHeight = (numRows * (rowHeight + WrestlingTheme.dimensions.spacing_8)) + WrestlingTheme.dimensions.spacing_8

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(gridHeight.coerceAtMost(280.dp)), // altura máxima
                    contentPadding = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
                    horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                    verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                ) {
                    items(allTeams) { team ->
                        TeamGridCard(
                            team = team,
                            onClick = { viewModel.navigateToTeamDetail(team.id) }
                        )
                    }
                }
            }
        }

        // Espaciado al final
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}
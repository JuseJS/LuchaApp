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
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.competitions.ui.components.CompetitionsSection
import org.iesharia.features.competitions.ui.components.EmptyCompetitions
import org.iesharia.features.home.ui.components.*
import org.iesharia.features.home.ui.viewmodel.HomeUiState
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.ui.components.TeamGridCard
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
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

                            TeamsByDivisionSection(
                                title = "", // Empty title as we already have the section subtitle
                                allTeams = teams.map { it.team },
                                onTeamClick = { viewModel.navigateToTeamDetail(it) }
                            )

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

                            // Group wrestlers by category
                            val wrestlersByCategory = wrestlers.map { it.wrestler }.groupBy { it.category }

                            // For each category with wrestlers, display a grid
                            WrestlerCategory.entries.forEach { category ->
                                val wrestlersInCategory = wrestlersByCategory[category] ?: emptyList()

                                if (wrestlersInCategory.isNotEmpty()) {
                                    // Category header
                                    Text(
                                        text = category.displayName(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                                    )

                                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                                    // Altura ajustada para el nuevo diseño (más información requiere más espacio)
                                    val rowHeight = 180.dp
                                    val numRows = (wrestlersInCategory.size + 1) / 2
                                    val gridHeight = (numRows * rowHeight) + ((numRows - 1) * WrestlingTheme.dimensions.spacing_8)

                                    // Display wrestlers in a grid
                                    LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(gridHeight.coerceAtMost(380.dp)), // Altura máxima ajustada
                                        contentPadding = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
                                        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                                        verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                                    ) {
                                        items(wrestlersInCategory) { wrestler ->
                                            WrestlerItem(
                                                wrestler = wrestler,
                                                onClick = { viewModel.navigateToWrestlerDetail(wrestler.id) },
                                                isGridItem = true // Usar el nuevo diseño en grid
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
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

        // Nueva sección de equipos por división
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
                title = "Equipos",
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

/**
 * Component for displaying teams grouped by division
 */
@Composable
private fun TeamsByDivisionSection(
    title: String,
    allTeams: List<Team>,
    onTeamClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Group teams by division
    val teamsByDivision = allTeams.groupBy { it.divisionCategory }

    Column(modifier = modifier.fillMaxWidth()) {
        // Section title
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = White90,
            modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Check if there are teams to show
        if (teamsByDivision.isEmpty()) {
            EmptyStateMessage(message = "No hay equipos disponibles")
        } else {
            // For each division, create a subsection
            DivisionCategory.entries.forEach { division ->
                val teamsInDivision = teamsByDivision[division] ?: emptyList()

                if (teamsInDivision.isNotEmpty()) {
                    // Division title
                    SectionSubtitle(
                        subtitle = division.displayName(),
                        modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                    // Calculate height based on number of teams in this division
                    val rowHeight = 72.dp
                    val numRows = (teamsInDivision.size + 1) / 2 // +1 and integer division to round up
                    val gridHeight = (numRows * (rowHeight + WrestlingTheme.dimensions.spacing_8)) + WrestlingTheme.dimensions.spacing_8

                    // Grid of teams for this division
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(gridHeight.coerceAtMost(180.dp)), // max height per division
                        contentPadding = PaddingValues(horizontal = WrestlingTheme.dimensions.spacing_16),
                        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                        verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                    ) {
                        items(teamsInDivision) { team ->
                            TeamGridCard(
                                team = team,
                                onClick = { onTeamClick(team.id) },
                                showDivision = false // We already show division in the section title
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }
        }
    }
}
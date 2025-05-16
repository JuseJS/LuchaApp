package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.AppScreen
import org.iesharia.core.ui.components.LuchaLoadingOverlay
import org.iesharia.core.ui.components.common.SectionSubtitle
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.competitions.ui.components.CompetitionsSection
import org.iesharia.features.competitions.ui.components.EmptyCompetitions
import org.iesharia.features.home.ui.components.EmptyFavorites
import org.iesharia.features.home.ui.components.FavoritesSectionDivider
import org.iesharia.features.home.ui.components.FavoritesSectionHeader
import org.iesharia.features.home.ui.components.shouldShowCompetitions
import org.iesharia.features.home.ui.components.shouldShowTeams
import org.iesharia.features.home.ui.components.shouldShowWrestlers
import org.iesharia.features.home.ui.viewmodel.HomeUiState
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.teams.ui.components.TeamItem
import org.iesharia.features.wrestlers.ui.components.WrestlerItem
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.HandleNavigationManager
import org.koin.compose.koinInject

/**
 * Pantalla principal de la aplicación, optimizada para mostrar secciones responsivas
 */
class HomeScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = requireNavigator()
        val navigationManager = koinInject<NavigationManager>()

        // Manejar navegación
        navigator.HandleNavigationManager(navigationManager)

        Scaffold(
            topBar = {
                HomeTopBar()
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isLoading -> {
                        LuchaLoadingOverlay()
                    }
                    else -> {
                        HomeContent(
                            uiState = uiState,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = AppStrings.Common.appName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.primary
        )
    )
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
        contentPadding = PaddingValues(vertical = LuchaTheme.dimensions.spacing_16)
    ) {
        // Sección de favoritos - Header con filtros
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shape = LuchaTheme.shapes.medium,
                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Header de la sección de favoritos
                    FavoritesSectionHeader(
                        selectedType = uiState.selectedFavoriteType,
                        onTypeSelected = viewModel::setFavoriteType
                    )

                    // Contenido según el tipo seleccionado
                    val favorites = viewModel.getFilteredFavorites()

                    if (favorites.isEmpty()) {
                        EmptyFavorites(uiState.selectedFavoriteType)
                    } else {
                        // Obtención de favoritos según tipo
                        val competitions = favorites.filterIsInstance<Favorite.CompetitionFavorite>()
                        val teams = favorites.filterIsInstance<Favorite.TeamFavorite>()
                        val wrestlers = favorites.filterIsInstance<Favorite.WrestlerFavorite>()

                        // Sección de competiciones
                        if (competitions.isNotEmpty() && shouldShowCompetitions(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Competitions.favoriteCompetitions,
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_12)
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

                        // Sección de equipos
                        if (teams.isNotEmpty() && shouldShowTeams(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Teams.favoriteTeams,
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_12)
                            ) {
                                teams.forEach { favorite ->
                                    // Obtener datos de enfrentamientos para equipos
                                    val teamId = favorite.team.id
                                    val lastMatches = viewModel.getTeamLastMatches(teamId)
                                    val nextMatches = viewModel.getTeamNextMatches(teamId)

                                    TeamItem(
                                        team = favorite.team,
                                        onClick = { /* Navegar al detalle */ },
                                        lastMatches = lastMatches.take(2),
                                        nextMatches = nextMatches.take(2)
                                    )
                                }
                            }

                            if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                                FavoritesSectionDivider()
                            }
                        }

                        // Sección de luchadores
                        if (wrestlers.isNotEmpty() && shouldShowWrestlers(uiState.selectedFavoriteType)) {
                            SectionSubtitle(
                                subtitle = AppStrings.Wrestlers.favoriteWrestlers,
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16)
                            )

                            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                            Column(
                                modifier = Modifier.padding(horizontal = LuchaTheme.dimensions.spacing_16),
                                verticalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_12)
                            ) {
                                wrestlers.forEach { favorite ->
                                    // Obtener resultados para luchadores
                                    val wrestlerId = favorite.wrestler.id
                                    val results = viewModel.getWrestlerResults(wrestlerId)

                                    WrestlerItem(
                                        wrestler = favorite.wrestler,
                                        onClick = { /* Navegar al detalle */ },
                                        matchResults = results
                                    )
                                }
                            }
                        }
                    }

                    // Espaciado final
                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
                }
            }

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = LuchaTheme.dimensions.spacing_16,
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
        }

        // Sección de competiciones - Header y filtros
        item {
            CompetitionsSection(
                competitions = emptyList(),
                currentFilters = uiState.filters,
                onFilterChanged = viewModel::updateFilters,
                onClearFilters = viewModel::clearFilters,
                onCompetitionClick = { /* No se usa */ },
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
                        horizontal = LuchaTheme.dimensions.spacing_16,
                        vertical = LuchaTheme.dimensions.spacing_8
                    )
                )
            }
        }

        // Espacio al final para evitar que el último elemento quede cortado
        item {
            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
        }
    }
}
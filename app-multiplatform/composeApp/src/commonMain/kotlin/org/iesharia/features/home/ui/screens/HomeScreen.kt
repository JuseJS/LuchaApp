package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.SectionSubtitle
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.competitions.ui.components.CompetitionsSection
import org.iesharia.features.competitions.ui.components.EmptyCompetitions
import org.iesharia.features.home.ui.components.*
import org.iesharia.features.home.ui.viewmodel.HomeUiState
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.teams.ui.components.TeamItem
import org.iesharia.features.wrestlers.ui.components.WrestlerItem

/**
 * Main home screen of the application, optimized for OLED displays
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
        return null // No back navigation from main screen
    }

    @Composable
    override fun TopBarActions() {
        // No actions for this screen
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        HomeContent(
            uiState = uiState,
            viewModel = viewModel
        )
    }
}

/**
 * Main content of the home screen
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
        // Favorites section
        item {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = WrestlingTheme.shapes.medium,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Favorites section header with filters
                    FavoritesSectionHeader(
                        selectedType = uiState.selectedFavoriteType,
                        onTypeSelected = { viewModel.setFavoriteType(it) }
                    )

                    // Favorites content based on selected filter
                    val favorites = viewModel.getFilteredFavorites()

                    if (favorites.isEmpty()) {
                        EmptyFavorites(uiState.selectedFavoriteType)
                    } else {
                        // Display favorites by type
                        val competitions = favorites.filterIsInstance<Favorite.CompetitionFavorite>()
                        val teams = favorites.filterIsInstance<Favorite.TeamFavorite>()
                        val wrestlers = favorites.filterIsInstance<Favorite.WrestlerFavorite>()

                        // Competition favorites section
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

                        // Team favorites section
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

                        // Wrestler favorites section
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

                    // Bottom spacing
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Section divider
            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = WrestlingTheme.dimensions.spacing_16,
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Competitions section with filters
        item {
            CompetitionsSection(
                competitions = emptyList(),
                currentFilters = uiState.filters,
                onFilterChanged = viewModel::updateFilters,
                onClearFilters = viewModel::clearFilters,
                onCompetitionClick = { /* Not used here */ },
                showEmptyState = false
            )
        }

        // Competitions list
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

        // Bottom spacing
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}
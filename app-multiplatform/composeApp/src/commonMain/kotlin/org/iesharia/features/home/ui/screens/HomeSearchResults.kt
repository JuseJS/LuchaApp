package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.ui.components.CompetitionItem
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.teams.ui.components.TeamGridCard

/**
 * Componente que muestra los resultados de la búsqueda
 */
@Composable
fun HomeSearchResults(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val (competitions, teams, wrestlers) = viewModel.getSearchResults()
    val hasResults = competitions.isNotEmpty() || teams.isNotEmpty() || wrestlers.isNotEmpty()

    if (hasResults) {
        Column(modifier = modifier.fillMaxWidth()) {
            // Título principal
            SectionDivider(
                title = "Resultados de búsqueda",
                type = SectionDividerType.PRIMARY,
                textColor = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Competiciones encontradas
            if (competitions.isNotEmpty()) {
                SectionDivider(
                    title = "Competiciones encontradas",
                    type = SectionDividerType.TITLE
                )

                competitions.take(3).forEach { competition ->
                    CompetitionItem(
                        competition = competition,
                        onClick = { viewModel.navigateToCompetitionDetail(competition.id) },
                        showMatchDays = true,
                        onMatchClick = { matchId -> viewModel.navigateToMatchDetail(matchId) }
                    )
                }

                if (teams.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }

            // Equipos encontrados
            if (teams.isNotEmpty()) {
                SectionDivider(
                    title = "Equipos encontrados",
                    type = SectionDividerType.TITLE
                )

                // Grid de equipos
                val rowHeight = 72.dp
                val numRows = (teams.size + 1) / 2
                val gridHeight = numRows * (rowHeight + WrestlingTheme.dimensions.spacing_8) + WrestlingTheme.dimensions.spacing_8

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
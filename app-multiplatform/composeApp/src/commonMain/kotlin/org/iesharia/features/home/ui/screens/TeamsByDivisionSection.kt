package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.White90
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.ui.components.TeamGridCard

/**
 * Component for displaying teams grouped by division
 */
@Composable
fun TeamsByDivisionSection(
    title: String,
    allTeams: List<Team>,
    onTeamClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Group teams by division
    val teamsByDivision = allTeams.groupBy { it.divisionCategory }

    Column(modifier = modifier.fillMaxWidth()) {
        if (title.isNotEmpty()) {
            SectionDivider(
                title = title,
                type = SectionDividerType.PRIMARY,
                textColor = White90
            )
        }

        // Check if there are teams to show
        if (teamsByDivision.isEmpty()) {
            EmptyStateMessage(message = "No hay equipos disponibles")
        } else {
            // For each division, create a subsection
            DivisionCategory.entries.forEach { division ->
                val teamsInDivision = teamsByDivision[division] ?: emptyList()

                if (teamsInDivision.isNotEmpty()) {
                    // Division title - usando SUBTITLE en lugar de PRIMARY
                    SectionDivider(
                        title = division.displayName(),
                        type = SectionDividerType.SUBTITLE
                    )

                    // Calculate height based on number of teams in this division
                    val rowHeight = 72.dp
                    val itemsPerRow = 3 // Cambiado de 2 a 3 elementos por fila
                    val numRows = (teamsInDivision.size + itemsPerRow - 1) / itemsPerRow // Redondeo hacia arriba
                    val gridHeight = (numRows * (rowHeight + WrestlingTheme.dimensions.spacing_8)) + WrestlingTheme.dimensions.spacing_8

                    // Grid of teams for this division
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(itemsPerRow), // Cambiado a 3 columnas
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
                                showDivision = false
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }
        }
    }
}
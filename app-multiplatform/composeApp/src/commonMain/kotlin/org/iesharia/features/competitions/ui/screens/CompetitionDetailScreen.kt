package org.iesharia.features.competitions.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.ui.components.common.*
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailUiState
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailViewModel
import org.iesharia.features.teams.ui.components.MatchDaySection
import org.iesharia.features.teams.ui.components.TeamGridCard
import org.koin.core.parameter.parametersOf

class CompetitionDetailScreen(private val competitionId: String) : BaseContentScreen() {

    private lateinit var viewModel: CompetitionDetailViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<CompetitionDetailViewModel> { parametersOf(competitionId) }
    }

    @Composable
    override fun ScreenTitle(): String {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.competition?.name ?: "Detalle de Competición"
    }

    @Composable
    override fun OnNavigateBack(): () -> Unit {
        return { viewModel.navigateBack() }
    }

    @Composable
    override fun TopBarActions() {
        val uiState by viewModel.uiState.collectAsState()

        IconButton(onClick = { viewModel.toggleFavorite() }) {
            Icon(
                imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (uiState.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        if (uiState.competition == null && !uiState.isLoading) {
            EmptyStateMessage(
                message = uiState.errorMessage ?: "No se encontró la competición"
            )
        } else if (!uiState.isLoading) {
            CompetitionDetailContent(
                uiState = uiState,
                onTeamClick = { teamId -> viewModel.navigateToTeamDetail(teamId) }
            )
        }
    }
}

@Composable
private fun CompetitionDetailContent(
    uiState: CompetitionDetailUiState,
    onTeamClick: (String) -> Unit
) {
    val competition = uiState.competition ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Banner de información
        item {
            val season = try {
                val years = competition.season.split("-")
                if (years.size == 2) {
                    "${years[0]}/${years[1].takeLast(2)}"
                } else {
                    competition.season
                }
            } catch (e: Exception) {
                competition.season
            }

            EntityHeader(
                title = competition.name,
                iconVector = Icons.Default.EmojiEvents,
                iconSize = 80.dp,
                subtitleContent = {
                    Text(
                        text = "Temporada $season",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                },
                bottomContent = {
                    // Usar los componentes existentes para los chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = WrestlingTheme.dimensions.spacing_16,
                                vertical = WrestlingTheme.dimensions.spacing_8
                            ),
                        horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                    ) {
                        // Primera
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = {
                                Text(
                                    text = competition.divisionCategory.displayName(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                labelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                        // Regional
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = {
                                Text(
                                    text = competition.ageCategory.displayName(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                labelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )

                        // Tenerife
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = {
                                Text(
                                    text = competition.island.displayName(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                labelColor = MaterialTheme.colorScheme.onTertiary
                            )
                        )
                    }
                }
            )
        }

        // Sección de equipos participantes
        item {
            SectionDivider(
                title = "Equipos Participantes",
                type = SectionDividerType.PRIMARY,
                textColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = WrestlingTheme.dimensions.spacing_8)
            )
        }

        // Grid de equipos
        item {
            if (uiState.teams.isEmpty()) {
                EmptyStateMessage(message = "No hay equipos en esta competición")
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        horizontal = WrestlingTheme.dimensions.spacing_16,
                        vertical = WrestlingTheme.dimensions.spacing_8
                    ),
                    horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                    verticalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((uiState.teams.size / 2f).toInt() * 72.dp + 16.dp)
                ) {
                    items(uiState.teams) { team ->
                        // Usar directamente TeamGridCard en lugar de TeamCard
                        TeamGridCard(
                            team = team,
                            onClick = { onTeamClick(team.id) }
                        )
                    }
                }
            }
        }

        // Sección de jornadas
        item {
            SectionDivider(
                title = "Jornadas",
                type = SectionDividerType.PRIMARY,
                textColor = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = WrestlingTheme.dimensions.spacing_8)
            )
        }

        // Lista de jornadas
        if (uiState.matchDays.isEmpty()) {
            item {
                EmptyStateMessage(message = "No hay jornadas programadas")
            }
        } else {
            items(uiState.matchDays) { matchDay ->
                MatchDayCard(
                    matchDay = matchDay,
                    modifier = Modifier.padding(
                        horizontal = WrestlingTheme.dimensions.spacing_16,
                        vertical = WrestlingTheme.dimensions.spacing_8
                    )
                )
            }
        }

        // Espacio al final
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}

@Composable
private fun MatchDayCard(
    matchDay: MatchDay,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        shape = WrestlingTheme.shapes.medium,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jornada ${matchDay.number}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                // Rango de fechas
                Text(
                    text = matchDay.getDateRangeFormatted(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            MatchDaySection(
                matchDay = matchDay
            )
        }
    }
}
package org.iesharia.features.teams.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.components.common.SearchBar
import org.iesharia.core.ui.components.common.SectionSubtitle
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.ui.components.MatchDaySection
import org.iesharia.features.teams.ui.viewmodel.TeamDetailViewModel
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.ui.components.WrestlerItem
import org.koin.core.parameter.parametersOf

class TeamDetailScreen(private val teamId: String) : BaseContentScreen() {

    private lateinit var viewModel: TeamDetailViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<TeamDetailViewModel> { parametersOf(teamId) }
    }

    @Composable
    override fun ScreenTitle(): String {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.team?.name ?: "Detalle de Equipo"
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

        if (uiState.team == null && !uiState.isLoading) {
            EmptyStateMessage(
                message = uiState.errorMessage ?: "No se encontró el equipo"
            )
        } else if (!uiState.isLoading) {
            TeamDetailContent(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun TeamDetailContent(
    viewModel: TeamDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val team = uiState.team ?: return

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        // Banner mejorado del equipo
        item {
            TeamBanner(team = team)
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Sección de búsqueda de luchadores
        item {
            SearchWrestlersSection(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.updateSearchQuery(it) }
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Sección de luchadores por categoría
        item {
            Text(
                text = "Luchadores",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Sección de luchadores Regionales
        item {
            WrestlerCategorySection(
                category = WrestlerCategory.REGIONAL,
                wrestlers = viewModel.getFilteredWrestlers(WrestlerCategory.REGIONAL),
                onWrestlerClick = { viewModel.navigateToWrestlerDetail(it.id) }
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de luchadores Juveniles
        item {
            WrestlerCategorySection(
                category = WrestlerCategory.JUVENIL,
                wrestlers = viewModel.getFilteredWrestlers(WrestlerCategory.JUVENIL),
                onWrestlerClick = { viewModel.navigateToWrestlerDetail(it.id) }
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de luchadores Cadetes
        item {
            WrestlerCategorySection(
                category = WrestlerCategory.CADETE,
                wrestlers = viewModel.getFilteredWrestlers(WrestlerCategory.CADETE),
                onWrestlerClick = { viewModel.navigateToWrestlerDetail(it.id) }
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de competiciones mejorada
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = WrestlingTheme.dimensions.spacing_8)
            ) {
                Text(
                    text = "Competiciones",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
        }

        // Listado de competiciones con sus jornadas
        items(uiState.competitions) { competition ->
            CompetitionWithMatchDays(
                competition = competition,
                lastMatchDay = uiState.competitionMatches[competition.id]?.first,
                nextMatchDay = uiState.competitionMatches[competition.id]?.second,
                onCompetitionClick = { viewModel.navigateToCompetitionDetail(competition.id) },
                modifier = Modifier.padding(
                    horizontal = WrestlingTheme.dimensions.spacing_16,
                    vertical = WrestlingTheme.dimensions.spacing_8
                )
            )
        }

        // Espacio al final para evitar que el último elemento quede cortado
        item {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}

@Composable
private fun TeamBanner(
    team: Team
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = WrestlingTheme.dimensions.spacing_16,
                    vertical = WrestlingTheme.dimensions.spacing_16
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de equipo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )
            }

            Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_16))

            // Información del equipo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = AppStrings.Teams.islandLabel.format(team.island.displayName()),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_4)
                )

                Text(
                    text = "Terrero: ${team.venue}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                // Chips de divisiones
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = WrestlingTheme.dimensions.spacing_8),
                    horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
                ) {
                    // División
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = {
                            Text(
                                text = team.divisionCategory.displayName(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            labelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    // Isla
                    FilterChip(
                        selected = false,
                        onClick = { },
                        label = {
                            Text(
                                text = team.island.displayName(),
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
        }
    }
}

@Composable
private fun SearchWrestlersSection(
    query: String,
    onQueryChange: (String) -> Unit
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        placeholder = "Buscar luchador...",
        shape = WrestlingTheme.shapes.medium,
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun WrestlerCategorySection(
    category: WrestlerCategory,
    wrestlers: List<Wrestler>,
    onWrestlerClick: (Wrestler) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Título de la categoría
        Text(
            text = category.displayName(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

        // Dividir con una línea
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        if (wrestlers.isEmpty()) {
            EmptyStateMessage(message = "No hay luchadores en esta categoría")
        } else {
            // Lista de luchadores
            wrestlers.forEach { wrestler ->
                // Usar el componente WrestlerItem reutilizable en modo compacto
                WrestlerItem(
                    wrestler = wrestler,
                    onClick = { onWrestlerClick(wrestler) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
            }
        }
    }
}

@Composable
private fun CompetitionWithMatchDays(
    competition: Competition,
    lastMatchDay: MatchDay?,
    nextMatchDay: MatchDay?,
    onCompetitionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ItemCard(
        onClick = onCompetitionClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        title = {
            Text(
                text = competition.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        subtitle = {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))

            Text(
                text = "${competition.ageCategory.displayName()} - ${competition.divisionCategory.displayName()} - ${competition.island.displayName()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Text(
                text = AppStrings.Competitions.season.format(competition.season),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
        }
    ) {
        if (lastMatchDay != null || nextMatchDay != null) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Última jornada
            lastMatchDay?.let { matchDay ->
                SectionSubtitle(
                    subtitle = AppStrings.Competitions.lastMatchDay.format(matchDay.number),
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                MatchDaySection(matchDay = matchDay)

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
            }

            // Próxima jornada
            nextMatchDay?.let { matchDay ->
                SectionSubtitle(
                    subtitle = AppStrings.Competitions.nextMatchDay.format(matchDay.number),
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

                MatchDaySection(matchDay = matchDay)
            }
        } else {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            EmptyStateMessage(message = "No hay jornadas programadas")
        }
    }
}
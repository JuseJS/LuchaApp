package org.iesharia.features.competitions.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import org.iesharia.core.navigation.AppScreen
import org.iesharia.core.navigation.HandleNavigationManager
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.ui.components.WrestlingLoadingOverlay
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailUiState
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailViewModel
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.ui.components.MatchDaySection
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

class CompetitionDetailScreen(private val competitionId: String) : AppScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun ScreenContent() {
        val viewModel = rememberViewModel<CompetitionDetailViewModel> { parametersOf(competitionId) }
        val uiState by viewModel.uiState.collectAsState()
        val navigator = requireNavigator()
        val navigationManager = koinInject<NavigationManager>()

        // Manejar navegación
        navigator.HandleNavigationManager(navigationManager)

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = uiState.competition?.name ?: "Detalle de Competición",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.navigateBack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { viewModel.toggleFavorite() }) {
                            Icon(
                                imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (uiState.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                                tint = if (uiState.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when {
                    uiState.isLoading -> {
                        WrestlingLoadingOverlay()
                    }
                    uiState.competition == null -> {
                        EmptyStateMessage(
                            message = uiState.errorMessage ?: "No se encontró la competición"
                        )
                    }
                    else -> {
                        CompetitionDetailContent(
                            uiState = uiState,
                            onTeamClick = { /* Navegación a detalles del equipo (placeholder) */ }
                        )
                    }
                }
            }
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
            CompetitionBanner(competition = competition)
        }

        // Sección de equipos participantes
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = WrestlingTheme.dimensions.spacing_8)
            ) {
                Text(
                    text = "Equipos Participantes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                )
            }
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
                        TeamCard(
                            team = team,
                            onClick = { onTeamClick(team.id) }
                        )
                    }
                }
            }
        }

        // Sección de jornadas
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = WrestlingTheme.dimensions.spacing_8)
            ) {
                Text(
                    text = "Jornadas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_16)
                )
            }
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
private fun CompetitionBanner(competition: Competition) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = WrestlingTheme.dimensions.spacing_16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Logo/Trofeo
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0x20FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Logo competición",
                    modifier = Modifier.size(36.dp),
                    colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.9f))
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_12))

            // Nombre de la competición
            Text(
                text = competition.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Temporada
            // Convertir 2024-2025 a formato 2024/25
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

            Text(
                text = "Temporada $season",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_4)
            )

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Valores mostrados en forma de chips en la parte inferior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x33000000))
                    .padding(horizontal = WrestlingTheme.dimensions.spacing_8, vertical = WrestlingTheme.dimensions.spacing_12),
                horizontalArrangement = Arrangement.SpaceEvenly
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
                        labelColor = Color.White
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
                        labelColor = Color.White
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
                        labelColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
private fun TeamCard(
    team: Team,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = WrestlingTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = WrestlingTheme.dimensions.spacing_8, vertical = WrestlingTheme.dimensions.spacing_12),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar o logo por defecto
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Nombre del equipo
            Text(
                text = team.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
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
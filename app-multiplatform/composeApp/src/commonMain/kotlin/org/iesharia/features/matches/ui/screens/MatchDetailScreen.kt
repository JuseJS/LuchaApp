package org.iesharia.features.matches.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.matches.ui.components.MatchHeader
import org.iesharia.features.matches.ui.components.MatchStatsSection
import org.iesharia.features.matches.ui.components.RefereeSection
import org.iesharia.features.matches.ui.components.TeamLineupSection
import org.iesharia.features.matches.ui.viewmodel.MatchDetailViewModel
import org.koin.core.parameter.parametersOf

class MatchDetailScreen(private val matchId: String) : BaseContentScreen() {

    private lateinit var viewModel: MatchDetailViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<MatchDetailViewModel> { parametersOf(matchId) }
    }

    @Composable
    override fun ScreenTitle(): String {
        val uiState by viewModel.uiState.collectAsState()
        return if (uiState.match != null) {
            "${uiState.match!!.localTeam.name} vs ${uiState.match!!.visitorTeam.name}"
        } else {
            "Detalle de Enfrentamiento"
        }
    }

    @Composable
    override fun OnNavigateBack(): () -> Unit {
        return { viewModel.navigateBack() }
    }

    @Composable
    override fun TopBarActions() {
        // No actions needed for this screen
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        if (uiState.match == null && !uiState.isLoading) {
            EmptyStateMessage(
                message = uiState.errorMessage ?: "No se encontró el enfrentamiento"
            )

            if (uiState.errorMessage != null) {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    WrestlingButton(
                        text = "Reintentar",
                        onClick = { viewModel.refreshData() },
                        type = WrestlingButtonType.SECONDARY
                    )
                }
            }
        } else if (!uiState.isLoading) {
            MatchDetailContent(
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun MatchDetailContent(
    viewModel: MatchDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val match = uiState.match ?: return

    // Usamos animaciones para una mejor experiencia
    var showHeader by remember { mutableStateOf(false) }
    var showLocalTeam by remember { mutableStateOf(false) }
    var showVisitorTeam by remember { mutableStateOf(false) }
    var showStats by remember { mutableStateOf(false) }
    var showReferees by remember { mutableStateOf(false) }

    // Disparar animaciones secuencialmente
    LaunchedEffect(Unit) {
        showHeader = true
        kotlinx.coroutines.delay(200)
        showLocalTeam = true
        kotlinx.coroutines.delay(200)
        showVisitorTeam = true
        kotlinx.coroutines.delay(200)
        showStats = true
        kotlinx.coroutines.delay(200)
        showReferees = true
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Cabecera del enfrentamiento con equipos y marcador
        item {
            AnimatedVisibility(
                visible = showHeader,
                enter = fadeIn(tween(500)) + expandVertically(tween(500))
            ) {
                MatchHeader(
                    match = match,
                    onLocalTeamClick = { viewModel.navigateToLocalTeamDetail() },
                    onVisitorTeamClick = { viewModel.navigateToVisitorTeamDetail() }
                )
            }
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Alineación del equipo LOCAL
        item {
            AnimatedVisibility(
                visible = showLocalTeam,
                enter = fadeIn(tween(500)) + expandVertically(tween(500))
            ) {
                TeamLineupSection(
                    teamName = match.localTeam.name,
                    wrestlers = uiState.localTeamWrestlers,
                    isHome = true,
                    onWrestlerClick = { viewModel.navigateToWrestlerDetail(it) }
                )
            }
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Alineación del equipo VISITANTE
        item {
            AnimatedVisibility(
                visible = showVisitorTeam,
                enter = fadeIn(tween(500)) + expandVertically(tween(500))
            ) {
                TeamLineupSection(
                    teamName = match.visitorTeam.name,
                    wrestlers = uiState.visitorTeamWrestlers,
                    isHome = false,
                    onWrestlerClick = { viewModel.navigateToWrestlerDetail(it) }
                )
            }
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Estadísticas del enfrentamiento (solo si está completado)
        if (match.completed && uiState.matchStats != null) {
            item {
                AnimatedVisibility(
                    visible = showStats,
                    enter = fadeIn(tween(500)) + expandVertically(tween(500))
                ) {
                    MatchStatsSection(
                        stats = uiState.matchStats!!,
                        localTeamName = match.localTeam.name,
                        visitorTeamName = match.visitorTeam.name
                    )
                }
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
            }
        }

        // Información de árbitros
        item {
            AnimatedVisibility(
                visible = showReferees,
                enter = fadeIn(tween(500)) + expandVertically(tween(500))
            ) {
                if (uiState.referee != null) {
                    RefereeSection(
                        mainReferee = uiState.referee!!,
                        assistantReferees = uiState.assistantReferees
                    )
                }
            }
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }
    }
}
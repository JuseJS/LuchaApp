package org.iesharia.features.wrestlers.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.EntityHeader
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.wrestlers.ui.components.WrestlerMatchHistorySection
import org.iesharia.features.wrestlers.ui.components.WrestlerPersonalInfoSection
import org.iesharia.features.wrestlers.ui.components.WrestlerStatisticsSection
import org.iesharia.features.wrestlers.ui.viewmodel.WrestlerDetailViewModel
import org.koin.core.parameter.parametersOf

/**
 * Pantalla de detalle de luchador
 */
class WrestlerDetailScreen(private val wrestlerId: String) : BaseContentScreen() {

    private lateinit var viewModel: WrestlerDetailViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<WrestlerDetailViewModel> { parametersOf(wrestlerId) }
    }

    @Composable
    override fun ScreenTitle(): String {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.wrestler?.fullName ?: "Detalle de Luchador"
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

        if (uiState.wrestler == null && !uiState.isLoading) {
            EmptyStateMessage(
                message = uiState.errorMessage ?: "No se encontró el luchador"
            )
        } else if (!uiState.isLoading) {
            WrestlerDetailContent(
                viewModel = viewModel
            )
        }

        // Añadir un botón de refresco cuando hay un error
        if (uiState.errorMessage != null) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                WrestlingButton(
                    text = "Reintentar",
                    onClick = { viewModel.refreshData() },
                    type = WrestlingButtonType.SECONDARY
                )
            }
        }
    }
}

/**
 * Contenido principal de la pantalla de detalle de luchador
 */
@Composable
private fun WrestlerDetailContent(
    viewModel: WrestlerDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val wrestler = uiState.wrestler ?: return

    // Buscar el equipo actual para mostrar su nombre
    // En una implementación real, esto vendría del ViewModel
    val teamName = "C.L. Tegueste" // Simulado - Idealmente, obtener del repositorio

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Banner del luchador
        item {
            EntityHeader(
                title = wrestler.fullName,
                iconVector = Icons.Default.Person,
                iconSize = 80.dp,
                subtitleContent = {
                    Column {
                        if (wrestler.nickname != null) {
                            Text(
                                text = "\"${wrestler.nickname}\"",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }

                        // Podemos añadir más información en el subtítulo ahora que tenemos espacio horizontal
                        Text(
                            text = wrestler.classification.displayName(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }

        // Sección de información personal mejorada
        item {
            WrestlerPersonalInfoSection(
                wrestler = wrestler,
                teamName = teamName,
                onTeamClick = { viewModel.navigateToTeamDetail(wrestler.teamId) }
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de estadísticas
        item {
            WrestlerStatisticsSection(
                statisticsByClassification = uiState.statisticsByClassification
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_24))
        }

        // Sección de historial de enfrentamientos
        item {
            WrestlerMatchHistorySection(
                matchResults = uiState.matchResults
            )
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}
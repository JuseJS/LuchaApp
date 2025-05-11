package org.iesharia.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.iesharia.navigation.AppScreen
import org.iesharia.ui.components.LuchaLoadingOverlay
import org.iesharia.ui.components.home.CompetitionItem
import org.iesharia.ui.components.home.CompetitionsSection
import org.iesharia.ui.components.home.EmptyCompetitions
import org.iesharia.ui.components.home.FavoritesSection
import org.iesharia.ui.theme.LuchaTheme

/**
 * Pantalla principal de la aplicación
 */
class HomeScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel = remember { HomeViewModel() }
        val uiState by viewModel.uiState.collectAsState()

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
                // Contenido principal
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

/**
 * Barra superior de la pantalla de inicio
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Lucha Canaria",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* Abrir menú */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menú"
                )
            }
        }
    )
}

/**
 * Contenido principal de la pantalla de inicio
 * Usamos LazyColumn para toda la pantalla para evitar anidamiento de scrolls
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
        // Sección de favoritos
        item {
            FavoritesSection(
                favorites = viewModel.getFilteredFavorites(),
                selectedType = uiState.selectedFavoriteType,
                onTypeSelected = viewModel::setFavoriteType,
                onFavoriteClick = { /* Navegar al detalle del favorito */ }
            )

            HorizontalDivider(
                modifier = Modifier.padding(
                    horizontal = LuchaTheme.dimensions.spacing_16,
                    vertical = LuchaTheme.dimensions.spacing_16
                ),
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }

        // Sección de competiciones - Header y filtros
        item {
            CompetitionsSection(
                competitions = emptyList(), // No usamos esta lista aquí, solo para mostrar el header
                currentFilters = uiState.filters,
                onFilterChanged = viewModel::updateFilters,
                onClearFilters = viewModel::clearFilters,
                onCompetitionClick = { /* No se usa */ }
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
                    onClick = { /* Navegar al detalle de la competición */ },
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
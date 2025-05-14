package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.domain.model.Island
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.features.home.ui.viewmodel.CompetitionFilters
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory

/**
 * Sección de competiciones activas en la pantalla de inicio
 * Adaptada para funcionar dentro de un LazyColumn
 */
@Composable
fun CompetitionsSection(
    competitions: List<Competition>,
    currentFilters: CompetitionFilters,
    onFilterChanged: (ageCategory: AgeCategory?, divisionCategory: DivisionCategory?, island: Island?) -> Unit,
    onClearFilters: () -> Unit,
    onCompetitionClick: (Competition) -> Unit,
    modifier: Modifier = Modifier,
    showEmptyState: Boolean = true // Nuevo parámetro para controlar si se muestra el estado vacío
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = LuchaTheme.dimensions.spacing_16)
    ) {
        // Título y botón de filtro
        CompetitionSectionHeader(
            hasActiveFilters = currentFilters.ageCategory != null ||
                    currentFilters.divisionCategory != null ||
                    currentFilters.island != null,
            onFilterClick = { showFiltersDialog ->
                showFiltersDialog.value = true
            }
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

        // Diálogo de filtros
        val showFiltersDialog = remember { mutableStateOf(false) }
        if (showFiltersDialog.value) {
            FiltersDialog(
                currentFilters = currentFilters,
                onFilterChanged = onFilterChanged,
                onClearFilters = onClearFilters,
                onDismiss = { showFiltersDialog.value = false }
            )
        }

        // Solo mostrar el estado vacío si showEmptyState es true
        if (competitions.isEmpty() && showEmptyState) {
            EmptyCompetitions()
        }
    }
}

/**
 * Cabecera de la sección de competiciones
 */
@Composable
private fun CompetitionSectionHeader(
    hasActiveFilters: Boolean,
    onFilterClick: (MutableState<Boolean>) -> Unit
) {
    val showFiltersDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LuchaTheme.dimensions.spacing_16),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Competiciones Activas",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        IconButton(onClick = { onFilterClick(showFiltersDialog) }) {
            if (hasActiveFilters) {
                BadgedBox(
                    badge = {
                        Badge {
                            Text("!")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filtrar competiciones"
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar competiciones"
                )
            }
        }
    }
}

/**
 * Diálogo de filtros para competiciones
 */
@Composable
fun FiltersDialog(
    currentFilters: CompetitionFilters,
    onFilterChanged: (ageCategory: AgeCategory?, divisionCategory: DivisionCategory?, island: Island?) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedAgeCategory by remember { mutableStateOf(currentFilters.ageCategory) }
    var selectedDivisionCategory by remember { mutableStateOf(currentFilters.divisionCategory) }
    var selectedIsland by remember { mutableStateOf(currentFilters.island) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Filtrar competiciones") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Filtro de categoría por edad
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_8)
                ) {
                    AgeCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedAgeCategory == category,
                            onClick = {
                                selectedAgeCategory = if (selectedAgeCategory == category) null else category
                            },
                            label = { Text(category.displayName()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                // Filtro de división
                Text(
                    text = "División",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_8)
                ) {
                    DivisionCategory.entries.forEach { division ->
                        FilterChip(
                            selected = selectedDivisionCategory == division,
                            onClick = {
                                selectedDivisionCategory = if (selectedDivisionCategory == division) null else division
                            },
                            label = { Text(division.displayName()) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                // Filtro de isla
                Text(
                    text = "Isla",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(LuchaTheme.dimensions.spacing_8)
                ) {
                    Island.entries.forEach { island ->
                        FilterChip(
                            selected = selectedIsland == island,
                            onClick = {
                                selectedIsland = if (selectedIsland == island) null else island
                            },
                            label = { Text(island.displayName()) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onFilterChanged(selectedAgeCategory, selectedDivisionCategory, selectedIsland)
                    onDismiss()
                }
            ) {
                Text("Aplicar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClearFilters()
                    onDismiss()
                }
            ) {
                Text("Limpiar filtros")
            }
        }
    )
}

/**
 * Mensaje para cuando no hay competiciones
 */
@Composable
fun EmptyCompetitions() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        EmptyStateMessage("No hay competiciones activas con los filtros seleccionados")
    }
}
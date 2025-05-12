package org.iesharia.ui.screens.home

import org.iesharia.domain.model.*

/**
 * Opciones para el tipo de favorito seleccionado
 */
enum class FavoriteType {
    ALL,
    COMPETITIONS,
    TEAMS,
    WRESTLERS;

    fun displayName(): String = when(this) {
        ALL -> "Todos"
        COMPETITIONS -> "Competiciones"
        TEAMS -> "Equipos"
        WRESTLERS -> "Luchadores"
    }
}

/**
 * Filtros para la sección de competiciones
 */
data class CompetitionFilters(
    val ageCategory: AgeCategory? = null,
    val divisionCategory: DivisionCategory? = null,
    val island: Island? = null
)

/**
 * Estado de la pantalla de inicio
 */
data class HomeUiState(
    val isLoading: Boolean = true,

    // Favoritos
    val favorites: List<Favorite> = emptyList(),
    val selectedFavoriteType: FavoriteType = FavoriteType.ALL,

    // Competiciones
    val competitions: List<Competition> = emptyList(),
    val filters: CompetitionFilters = CompetitionFilters(),

    // Datos adicionales para equipos y luchadores
    val teamMatches: Map<String, Pair<List<Match>, List<Match>>> = emptyMap(),
    val wrestlerResults: Map<String, List<WrestlerMatchResult>> = emptyMap()
)
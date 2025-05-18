package org.iesharia.features.home.ui.viewmodel

import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.core.resources.AppStrings

/**
 * Opciones para el tipo de favorito seleccionado
 */
enum class FavoriteType {
    ALL,
    COMPETITIONS,
    TEAMS,
    WRESTLERS;

    fun displayName(): String = when(this) {
        ALL -> AppStrings.Home.all
        COMPETITIONS -> AppStrings.Home.competitions
        TEAMS -> AppStrings.Home.teams
        WRESTLERS -> AppStrings.Home.wrestlers
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
    val errorMessage: String? = null,
    val favorites: List<Favorite> = emptyList(),
    val selectedFavoriteType: FavoriteType = FavoriteType.ALL,
    val competitions: List<Competition> = emptyList(),
    val filters: CompetitionFilters = CompetitionFilters(),
    val teamMatches: Map<String, Pair<List<Match>, List<Match>>> = emptyMap(),
    val wrestlerResults: Map<String, List<WrestlerMatchResult>> = emptyMap(),
    val searchQuery: String = "" // Nuevo campo para la búsqueda
)
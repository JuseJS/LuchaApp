
package org.iesharia.features.home.ui.viewmodel

import org.iesharia.core.domain.model.Favorite
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.common.domain.model.FavoriteType
import org.iesharia.features.common.domain.model.CompetitionFilters

/**
 * Estado de la pantalla de inicio
 */
data class HomeUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val favorites: List<Favorite> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
    val selectedFavoriteType: FavoriteType = FavoriteType.ALL,
    val competitions: List<Competition> = emptyList(),
    val teams: List<Team> = emptyList(),
    val filters: CompetitionFilters = CompetitionFilters(),
    val teamMatches: Map<String, Pair<List<Match>, List<Match>>> = emptyMap(),
    val wrestlerResults: Map<String, List<WrestlerMatchResult>> = emptyMap(),
    val searchQuery: String = "" // Nuevo campo para la búsqueda
)
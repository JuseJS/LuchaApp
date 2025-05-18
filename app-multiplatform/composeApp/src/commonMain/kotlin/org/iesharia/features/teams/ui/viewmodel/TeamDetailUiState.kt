package org.iesharia.features.teams.ui.viewmodel

import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory

/**
 * Estado de la pantalla de detalle de equipo
 */
data class TeamDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val team: Team? = null,
    val wrestlers: Map<WrestlerCategory, List<Wrestler>> = emptyMap(),
    val competitions: List<Competition> = emptyList(),
    val competitionMatches: Map<String, Pair<MatchDay?, MatchDay?>> = emptyMap(),
    val isFavorite: Boolean = false,
    val searchQuery: String = ""
)
package org.iesharia.features.competitions.ui.viewmodel

import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Team

/**
 * Estado de la pantalla de detalle de competición
 */
data class CompetitionDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val competition: Competition? = null,
    val isFavorite: Boolean = false,
    val matchDays: List<MatchDay> = emptyList(),
    val teams: List<Team> = emptyList()
)
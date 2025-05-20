package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.Wrestler

/**
 * Estado de la pantalla de detalle de enfrentamiento
 */
data class MatchDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val match: Match? = null,
    val localTeamWrestlers: List<Wrestler> = emptyList(),
    val visitorTeamWrestlers: List<Wrestler> = emptyList(),
    val referee: String? = null,
    val assistantReferees: List<String> = emptyList(),
    val matchStats: MatchStats? = null
)

/**
 * Estadísticas del enfrentamiento
 */
data class MatchStats(
    val localTeamWins: Int = 0,     // Victorias del equipo local
    val visitorTeamWins: Int = 0,   // Victorias del equipo visitante
    val draws: Int = 0,             // Empates
    val separations: Int = 0,       // Separadas
    val expulsions: Int = 0,        // Expulsiones
    val totalGrips: Int = 0,        // Total de agarradas
    val duration: String = "",      // Duración del enfrentamiento (formato HH:mm)
    val spectators: Int? = null     // Número de espectadores (opcional)
)
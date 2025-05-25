package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.matches.domain.model.AssistantReferee
import org.iesharia.features.matches.domain.model.MatchActWrestler
import org.iesharia.features.matches.domain.model.MatchActBout

/**
 * Estado UI para la pantalla de acta de enfrentamiento
 */
data class MatchActUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,
    val isCompleted: Boolean = false,

    // Datos del enfrentamiento
    val match: Match? = null,
    val matchDay: MatchDay? = null,
    val actId: String? = null,

    // Encabezado del acta
    val isInsular: Boolean = false,
    val isRegional: Boolean = false,
    val season: String = "",
    val category: String = "",
    val competitionId: String = "",
    val competitionName: String = "",
    val venue: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val referee: String = "",
    val refereeLicense: String = "",
    val assistantReferees: List<AssistantReferee> = emptyList(),
    val fieldDelegate: String = "",
    val fieldDelegateDni: String = "",

    // Datos de los equipos
    val localClubName: String = "",
    val visitorClubName: String = "",
    val localWrestlers: List<MatchActWrestler> = listOf(MatchActWrestler()),
    val visitorWrestlers: List<MatchActWrestler> = listOf(MatchActWrestler()),
    val localCaptain: String = "",
    val visitorCaptain: String = "",
    val localCoach: String = "",
    val visitorCoach: String = "",

    // Desarrollo de la luchada
    val bouts: List<MatchActBout> = listOf(MatchActBout()),
    val localTeamScore: String = "",
    val visitorTeamScore: String = "",

    // Comentarios - NUEVOS CAMPOS
    val localTeamComments: String = "",
    val visitorTeamComments: String = "",
    val refereeComments: String = ""
)
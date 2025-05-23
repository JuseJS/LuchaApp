package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match

/**
 * Estado UI para la pantalla de acta de enfrentamiento
 */
data class MatchActUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isSaved: Boolean = false,

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

/**
 * Datos de un árbitro auxiliar en el acta
 */
data class AssistantReferee(
    val name: String = "",
    val licenseNumber: String = ""
)

/**
 * Datos de un luchador en el acta
 */
data class MatchActWrestler(
    val id: String = "",
    val licenseNumber: String = ""
)

/**
 * Datos de una lucha en el acta
 */
data class MatchActBout(
    // Datos del luchador local
    val localWrestlerNumber: String = "",
    val localCheck1: Boolean = false,
    val localCheck2: Boolean = false,
    val localCheck3: Boolean = false, // NUEVA: tercera agarrada
    val localPenalty: String = "",

    // Datos del luchador visitante
    val visitorWrestlerNumber: String = "",
    val visitorCheck1: Boolean = false,
    val visitorCheck2: Boolean = false,
    val visitorCheck3: Boolean = false, // NUEVA: tercera agarrada
    val visitorPenalty: String = "",

    // Resultado
    val localScore: String = "0",
    val visitorScore: String = "0",
    val localWin: Boolean = false,
    val visitorWin: Boolean = false
)
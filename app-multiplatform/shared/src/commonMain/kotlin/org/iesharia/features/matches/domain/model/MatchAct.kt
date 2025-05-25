package org.iesharia.features.matches.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.iesharia.features.competitions.domain.model.AgeCategory

/**
 * Modelo que representa un acta de enfrentamiento de lucha canaria
 */
data class MatchAct(
    val id: String,
    val matchId: String,
    val competitionId: String,
    val competitionName: String,
    val season: String,
    val category: AgeCategory,
    val isRegional: Boolean,
    val isInsular: Boolean,
    val venue: String,
    val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime?,
    val mainReferee: Referee,
    val assistantReferees: List<Referee>,
    val fieldDelegate: FieldDelegate?,
    val localTeam: ActTeam,
    val visitorTeam: ActTeam,
    val bouts: List<MatchBout>,
    val localTeamScore: Int,
    val visitorTeamScore: Int,
    val isDraft: Boolean,
    val isCompleted: Boolean,
    val isSigned: Boolean,

    // NUEVOS CAMPOS DE COMENTARIOS - AÑADIDO
    val localTeamComments: String? = null,
    val visitorTeamComments: String? = null,
    val refereeComments: String? = null
)

/**
 * Modelo que representa un delegado de terrero
 */
data class FieldDelegate(
    val name: String,
    val dni: String
)

/**
 * Modelo que representa un equipo en el acta
 */
data class ActTeam(
    val teamId: String,
    val clubName: String,
    val wrestlers: List<ActWrestler> = emptyList(),
    val captain: String = "",
    val coach: String = ""
)

/**
 * Modelo que representa un luchador en el acta
 */
data class ActWrestler(
    val wrestlerId: String,
    val licenseNumber: String,
    val number: Int? = null
)

/**
 * Modelo que representa una lucha en el acta
 */
data class MatchBout(
    val id: String,
    val localWrestler: ActWrestler?,
    val visitorWrestler: ActWrestler?,
    val localFalls: List<Fall> = emptyList(),
    val visitorFalls: List<Fall> = emptyList(),
    val localPenalties: Int = 0,
    val visitorPenalties: Int = 0,
    val winner: WinnerType = WinnerType.NONE,
    val order: Int
)

/**
 * Enumerado para representar el ganador de una lucha
 */
enum class WinnerType {
    LOCAL, VISITOR, DRAW, NONE
}

/**
 * Modelo que representa una caída en una lucha
 */
data class Fall(
    val id: String,
    val type: FallType,
    val inSeparation: Boolean = false
)

/**
 * Enumerado para representar el tipo de caída
 */
enum class FallType {
    REGULAR, REVUELTA, FORFEITED;

    fun displayName(): String = when(this) {
        REGULAR -> "Regular"
        REVUELTA -> "Revuelta"
        FORFEITED -> "Lucha Desistida"
    }
}
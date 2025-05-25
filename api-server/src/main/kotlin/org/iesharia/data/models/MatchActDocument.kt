package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.iesharia.domain.models.AgeCategory

@Serializable
data class MatchActDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val matchId: String,
    val competitionId: String,
    val competitionName: String,
    val season: String,
    val category: AgeCategory,
    val isRegional: Boolean,
    val isInsular: Boolean,
    val venue: String,
    @Contextual
    val date: LocalDate,
    @Contextual
    val startTime: LocalTime,
    @Contextual
    val endTime: LocalTime? = null,
    val mainReferee: RefereeInfo,
    val assistantReferees: List<RefereeInfo> = emptyList(),
    val fieldDelegate: FieldDelegateInfo? = null,
    val localTeam: ActTeamInfo,
    val visitorTeam: ActTeamInfo,
    val bouts: List<MatchBoutInfo> = emptyList(),
    val localTeamScore: Int,
    val visitorTeamScore: Int,
    val isDraft: Boolean = true,
    val isCompleted: Boolean = false,
    val isSigned: Boolean = false,
    val localTeamComments: String? = null,
    val visitorTeamComments: String? = null,
    val refereeComments: String? = null,
    @Contextual
    val createdAt: Instant,
    @Contextual
    val updatedAt: Instant
) {
    val id: String get() = _id.toHexString()
}

@Serializable
data class RefereeInfo(
    val id: String,
    val name: String,
    val licenseNumber: String,
    val isMain: Boolean = false,
    val isActive: Boolean = true
)

@Serializable
data class FieldDelegateInfo(
    val name: String,
    val dni: String
)

@Serializable
data class ActTeamInfo(
    val teamId: String,
    val clubName: String,
    val wrestlers: List<ActWrestlerInfo> = emptyList(),
    val captain: String = "",
    val coach: String = ""
)

@Serializable
data class ActWrestlerInfo(
    val wrestlerId: String,
    val licenseNumber: String,
    val number: Int? = null
)

@Serializable
data class MatchBoutInfo(
    val id: String,
    val localWrestler: ActWrestlerInfo? = null,
    val visitorWrestler: ActWrestlerInfo? = null,
    val localFalls: List<FallInfo> = emptyList(),
    val visitorFalls: List<FallInfo> = emptyList(),
    val localPenalties: Int = 0,
    val visitorPenalties: Int = 0,
    val winner: String = "NONE", // LOCAL, VISITOR, DRAW, NONE
    val order: Int
)

@Serializable
data class FallInfo(
    val id: String,
    val type: String, // REGULAR, REVUELTA, FORFEITED
    val inSeparation: Boolean = false
)
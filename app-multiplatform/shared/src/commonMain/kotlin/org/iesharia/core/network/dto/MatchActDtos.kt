package org.iesharia.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class MatchActDto(
    val id: String,
    val matchId: String,
    val competitionId: String,
    val competitionName: String,
    val season: String,
    val category: String,
    val isRegional: Boolean,
    val isInsular: Boolean,
    val venue: String,
    val date: String,
    val startTime: String,
    val endTime: String? = null,
    val mainReferee: RefereeDto,
    val assistantReferees: List<RefereeDto> = emptyList(),
    val fieldDelegate: FieldDelegateDto? = null,
    val localTeam: ActTeamDto,
    val visitorTeam: ActTeamDto,
    val bouts: List<BoutDto> = emptyList(),
    val localTeamScore: Int,
    val visitorTeamScore: Int,
    val isDraft: Boolean,
    val isCompleted: Boolean,
    val isSigned: Boolean,
    val observations: String? = null,
    val protests: String? = null,
    val localTeamComments: String? = null,
    val visitorTeamComments: String? = null,
    val refereeComments: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

@Serializable
data class RefereeDto(
    val id: String,
    val name: String,
    val licenseNumber: String,
    val isMain: Boolean = false
)

@Serializable
data class FieldDelegateDto(
    val name: String,
    val dni: String
)

@Serializable
data class ActTeamDto(
    val teamId: String,
    val clubName: String,
    val wrestlers: List<ActWrestlerDto>,
    val captain: String,
    val coach: String
)

@Serializable
data class ActWrestlerDto(
    val wrestlerId: String,
    val order: Int,
    val name: String,
    val licenseNumber: String,
    val classification: String,
    val weight: Double? = null
)

@Serializable
data class BoutDto(
    val boutNumber: Int,
    val localWrestlerId: String,
    val visitorWrestlerId: String,
    val localWrestlerName: String,
    val visitorWrestlerName: String,
    val result: String,
    val score: String? = null,
    val time: String? = null,
    val localPoints: Int,
    val visitorPoints: Int
)
package org.iesharia.domain.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagedResponse<T>(
    val data: List<T>,
    val page: Int,
    val limit: Int,
    val total: Long,
    val totalPages: Int
)

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errors: List<String>? = null
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String,
    val statusCode: Int,
    val timestamp: String
)

@Serializable
data class HealthCheckResponse(
    val status: String,
    val version: String,
    val timestamp: String,
    val database: DatabaseStatus
)

@Serializable
data class DatabaseStatus(
    val connected: Boolean,
    val name: String? = null,
    val error: String? = null
)

object CommonDtos {
    @Serializable
    data class CompetitionDto(
        val id: String,
        val name: String,
        val ageCategory: String,
        val divisionCategory: String,
        val island: String,
        val season: String,
        val matchDays: List<MatchDayDto> = emptyList(),
        val teams: List<TeamDto> = emptyList()
    )
    
    @Serializable
    data class MatchDayDto(
        val id: String,
        val number: Int,
        val date: String,
        val matches: List<MatchSummaryDto>
    )
    
    @Serializable
    data class MatchSummaryDto(
        val id: String,
        val localTeamId: String,
        val localTeamName: String,
        val visitorTeamId: String,
        val visitorTeamName: String,
        val localScore: Int?,
        val visitorScore: Int?,
        val date: String,
        val venue: String,
        val completed: Boolean
    )
    
    @Serializable
    data class TeamSummaryDto(
        val id: String,
        val name: String,
        val imageUrl: String
    )
    
    @Serializable
    data class MatchDto(
        val id: String,
        val localTeamId: String,
        val visitorTeamId: String,
        val localTeamName: String,
        val visitorTeamName: String,
        val localScore: Int?,
        val visitorScore: Int?,
        val date: String,
        val venue: String,
        val completed: Boolean,
        val hasAct: Boolean,
        val competitionId: String? = null,
        val competitionName: String? = null,
        val round: Int? = null
    )
}
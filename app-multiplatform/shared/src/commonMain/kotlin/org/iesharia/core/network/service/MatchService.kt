package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.iesharia.core.network.ApiConfig

class MatchService(
    private val httpClient: HttpClient
) {
    suspend fun getMatchById(id: String): MatchDto {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/$id").body()
    }
    
    suspend fun getMatchesByTeamId(teamId: String): List<MatchDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/team/$teamId").body()
    }
    
    suspend fun getUpcomingMatches(limit: Int = 10): List<MatchDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/upcoming") {
            parameter("limit", limit)
        }.body()
    }
    
    suspend fun getRecentMatches(limit: Int = 10): List<MatchDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/recent") {
            parameter("limit", limit)
        }.body()
    }
    
    suspend fun getMatchesByCompetitionId(competitionId: String): List<MatchDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/competition/$competitionId").body()
    }
    
    suspend fun getMatchesByDateRange(startDate: String, endDate: String): List<MatchDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/matches/daterange") {
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        }.body()
    }
}

@kotlinx.serialization.Serializable
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
package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.iesharia.core.network.ApiConfig

class CompetitionService(
    private val httpClient: HttpClient
) {
    suspend fun getAllCompetitions(): List<CompetitionDto> {
        return try {
            httpClient.get("${ApiConfig.API_BASE_URL}/competitions").body()
        } catch (e: Exception) {
            println("Error fetching competitions: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun getCompetitionById(id: String): CompetitionDto {
        println("🔍 Fetching competition with ID: $id")
        val result: CompetitionDto = httpClient.get("${ApiConfig.API_BASE_URL}/competitions/$id").body()
        println("✅ Competition loaded: ${result.name}")
        return result
    }
    
    suspend fun getCompetitionsByFilters(
        ageCategory: String? = null,
        divisionCategory: String? = null,
        island: String? = null,
        season: String? = null
    ): List<CompetitionDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/competitions/filter") {
            ageCategory?.let { parameter("ageCategory", it) }
            divisionCategory?.let { parameter("divisionCategory", it) }
            island?.let { parameter("island", it) }
            season?.let { parameter("season", it) }
        }.body()
    }
    
    suspend fun getMatchDaysByCompetitionId(competitionId: String): List<MatchDayDto> {
        println("🔍 Fetching match days for competition: $competitionId")
        val result: List<MatchDayDto> = httpClient.get("${ApiConfig.API_BASE_URL}/competitions/$competitionId/matchdays").body()
        println("✅ Match days loaded: ${result.size} days")
        return result
    }
    
    suspend fun getTeamsByCompetitionId(competitionId: String): List<TeamSummaryDto> {
        println("🔍 Fetching teams for competition: $competitionId")
        val result: List<TeamSummaryDto> = httpClient.get("${ApiConfig.API_BASE_URL}/competitions/$competitionId/teams").body()
        println("✅ Teams loaded: ${result.size} teams")
        return result
    }
}

// DTOs
@kotlinx.serialization.Serializable
data class CompetitionDto(
    val id: String,
    val name: String,
    val ageCategory: String,
    val divisionCategory: String,
    val island: String,
    val season: String,
    val matchDays: List<MatchDayDto> = emptyList(),
    val teams: List<org.iesharia.core.network.dto.TeamDto> = emptyList()
)

@kotlinx.serialization.Serializable
data class MatchDayDto(
    val id: String,
    val number: Int,
    val date: String,
    val matches: List<MatchSummaryDto>
)

@kotlinx.serialization.Serializable
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

@kotlinx.serialization.Serializable
data class TeamSummaryDto(
    val id: String,
    val name: String,
    val imageUrl: String
)
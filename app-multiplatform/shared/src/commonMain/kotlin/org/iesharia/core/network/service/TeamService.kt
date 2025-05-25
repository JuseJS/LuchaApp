package org.iesharia.core.network.service

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.iesharia.core.network.ApiConfig
import org.iesharia.core.network.dto.TeamDto
import org.iesharia.core.network.dto.WrestlerDto

class TeamService(private val httpClient: HttpClient) {
    
    suspend fun getAllTeams(): List<TeamDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/teams").body()
    }
    
    suspend fun getTeamsByDivision(division: String): List<TeamDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/teams/division/$division").body()
    }
    
    suspend fun getTeamById(id: String): TeamDto {
        return httpClient.get("${ApiConfig.API_BASE_URL}/teams/$id").body()
    }
    
    suspend fun getWrestlersByTeamId(teamId: String): List<WrestlerDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/teams/$teamId/wrestlers").body()
    }
    
    suspend fun searchTeams(query: String): List<TeamDto> {
        return httpClient.get("${ApiConfig.API_BASE_URL}/teams/search") {
            parameter("q", query)
        }.body()
    }
}
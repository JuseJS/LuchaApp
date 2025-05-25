
package org.iesharia.features.teams.data.repository

import org.iesharia.core.domain.model.AppError
import org.iesharia.core.network.mapper.TeamMappers.toDomain
import org.iesharia.core.network.service.TeamService
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.domain.repository.TeamRepository

class HttpTeamRepository(
    private val teamService: TeamService
) : TeamRepository {
    
    override suspend fun getAllTeams(): Result<List<Team>> {
        return try {
            val teams = teamService.getAllTeams()
            Result.success(teams.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(AppError.NetworkError("Failed to fetch teams: ${e.message}"))
        }
    }
    
    override suspend fun getTeamsByDivision(divisionCategory: String): Result<List<Team>> {
        return try {
            val teams = teamService.getTeamsByDivision(divisionCategory)
            Result.success(teams.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(AppError.NetworkError("Failed to fetch teams by division: ${e.message}"))
        }
    }
    
    override suspend fun getTeamById(id: String): Result<Team> {
        return try {
            val team = teamService.getTeamById(id)
            Result.success(team.toDomain())
        } catch (e: Exception) {
            Result.failure(AppError.NetworkError("Failed to fetch team: ${e.message}"))
        }
    }
    
    override suspend fun getTeam(id: String): Team? {
        return try {
            teamService.getTeamById(id).toDomain()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun searchTeams(query: String): Result<List<Team>> {
        return try {
            val teams = teamService.searchTeams(query)
            Result.success(teams.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(AppError.NetworkError("Failed to search teams: ${e.message}"))
        }
    }
}
package org.iesharia.features.teams.domain.repository

import org.iesharia.features.teams.domain.model.Team

interface TeamRepository {
    suspend fun getAllTeams(): Result<List<Team>>
    suspend fun getTeamsByDivision(divisionCategory: String): Result<List<Team>>
    suspend fun getTeamById(id: String): Result<Team>
    suspend fun getTeam(id: String): Team?
    suspend fun searchTeams(query: String): Result<List<Team>>
}
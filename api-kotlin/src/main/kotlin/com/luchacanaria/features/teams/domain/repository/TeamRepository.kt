package com.luchacanaria.features.teams.domain.repository

import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory

interface TeamRepository {
    suspend fun getAllTeams(
        island: Island? = null,
        divisionCategory: DivisionCategory? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<Team>

    suspend fun getTeamById(id: String): Team?
    suspend fun createTeam(team: Team): Team
    suspend fun updateTeam(team: Team): Team?
    suspend fun deleteTeam(id: String): Boolean
    suspend fun getTeamsByIds(ids: List<String>): List<Team>
    suspend fun countTeams(): Long
    suspend fun getTeamsByIsland(island: Island): List<Team>
    suspend fun getTeamsByDivision(divisionCategory: DivisionCategory): List<Team>
}
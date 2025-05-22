package com.luchacanaria.features.teams.domain.usecase

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory
import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.features.teams.domain.repository.TeamRepository

data class GetTeamsRequest(
    val island: Island? = null,
    val divisionCategory: DivisionCategory? = null,
    val search: String? = null,
    val page: Int = 0,
    val size: Int = 20
)

class GetTeamsUseCase(private val teamRepository: TeamRepository) {
    suspend operator fun invoke(request: GetTeamsRequest): List<Team> {
        return teamRepository.getAllTeams(
            island = request.island,
            divisionCategory = request.divisionCategory,
            search = request.search,
            page = request.page,
            size = request.size
        )
    }
}
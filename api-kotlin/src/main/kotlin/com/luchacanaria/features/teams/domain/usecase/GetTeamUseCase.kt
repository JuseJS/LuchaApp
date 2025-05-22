package com.luchacanaria.features.teams.domain.usecase

import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.features.teams.domain.repository.TeamRepository

class GetTeamUseCase(private val teamRepository: TeamRepository) {
    suspend operator fun invoke(id: String): Team? {
        return teamRepository.getTeamById(id)
    }
}
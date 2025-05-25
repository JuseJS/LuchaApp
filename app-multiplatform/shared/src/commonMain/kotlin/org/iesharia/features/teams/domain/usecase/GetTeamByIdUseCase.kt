package org.iesharia.features.teams.domain.usecase

import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.domain.repository.TeamRepository

class GetTeamByIdUseCase(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(teamId: String): Team? {
        return teamRepository.getTeamById(teamId).getOrNull()
    }
}
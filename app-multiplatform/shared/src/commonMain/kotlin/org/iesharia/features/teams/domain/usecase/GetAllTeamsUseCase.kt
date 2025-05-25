package org.iesharia.features.teams.domain.usecase

import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.domain.repository.TeamRepository

/**
 * Caso de uso para obtener todos los equipos
 */
class GetAllTeamsUseCase(
    private val teamRepository: TeamRepository
) {
    suspend operator fun invoke(): List<Team> {
        return try {
            teamRepository.getAllTeams().getOrThrow()
        } catch (e: Exception) {
            println("Error loading teams: ${e.message}")
            emptyList()
        }
    }
}
package org.iesharia.features.teams.domain.usecase

import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class GetTeamMatchesUseCase(private val repository: CompetitionRepository) {
    /**
     * Obtiene las listas de enfrentamientos pasados y futuros de un equipo
     * @param teamId ID del equipo
     * @return Par de listas: (enfrentamientos pasados, enfrentamientos futuros)
     */
    suspend operator fun invoke(teamId: String): Pair<List<Match>, List<Match>> =
        repository.getTeamMatches(teamId)
}
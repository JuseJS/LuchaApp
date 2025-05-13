package org.iesharia.domain.usecase

import org.iesharia.domain.model.Match
import org.iesharia.domain.repository.CompetitionRepository

class GetTeamMatchesUseCase(private val repository: CompetitionRepository) {
    /**
     * Obtiene las listas de enfrentamientos pasados y futuros de un equipo
     * @param teamId ID del equipo
     * @return Par de listas: (enfrentamientos pasados, enfrentamientos futuros)
     */
    suspend operator fun invoke(teamId: String): Pair<List<Match>, List<Match>> =
        repository.getTeamMatches(teamId)
}
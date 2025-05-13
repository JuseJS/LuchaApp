package org.iesharia.domain.usecase

import org.iesharia.domain.model.Competition
import org.iesharia.domain.repository.CompetitionRepository

class GetCompetitionsUseCase(private val repository: CompetitionRepository) {
    suspend operator fun invoke(): List<Competition> = repository.getCompetitions()
}
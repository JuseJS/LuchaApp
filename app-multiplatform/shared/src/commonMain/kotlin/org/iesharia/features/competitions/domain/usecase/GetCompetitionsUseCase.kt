package org.iesharia.features.competitions.domain.usecase

import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class GetCompetitionsUseCase(private val repository: CompetitionRepository) {
    suspend operator fun invoke(): List<Competition> = repository.getCompetitions()
}
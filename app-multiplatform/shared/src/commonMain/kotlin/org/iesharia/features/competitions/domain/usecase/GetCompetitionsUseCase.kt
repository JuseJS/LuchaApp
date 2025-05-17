package org.iesharia.features.competitions.domain.usecase

import org.iesharia.core.domain.model.AppError
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class GetCompetitionsUseCase(private val repository: CompetitionRepository) {
    suspend operator fun invoke(): List<Competition> {
        try {
            return repository.getCompetitions()
        } catch (e: Exception) {
            if (e is AppError) throw e

            throw AppError.UnknownError(e, "Error al obtener competiciones")
        }
    }
}
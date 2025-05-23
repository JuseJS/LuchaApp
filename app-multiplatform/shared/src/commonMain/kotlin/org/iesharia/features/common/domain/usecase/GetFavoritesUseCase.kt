package org.iesharia.features.common.domain.usecase

import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class GetFavoritesUseCase(private val repository: CompetitionRepository) {
    suspend operator fun invoke(): List<Favorite> {
        try {
            return repository.getFavorites()
        } catch (e: Exception) {
            if (e is AppError) throw e

            throw AppError.UnknownError(e, "Error al obtener favoritos")
        }
    }
}
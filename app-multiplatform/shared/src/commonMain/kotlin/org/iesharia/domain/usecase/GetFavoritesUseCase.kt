package org.iesharia.domain.usecase

import org.iesharia.domain.model.Favorite
import org.iesharia.domain.repository.CompetitionRepository

class GetFavoritesUseCase(private val repository: CompetitionRepository) {
    suspend operator fun invoke(): List<Favorite> = repository.getFavorites()
}
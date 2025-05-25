package org.iesharia.features.common.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.iesharia.features.common.domain.repository.FavoriteRepository

class ObserveFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    operator fun invoke(): Flow<Set<String>> {
        return favoriteRepository.observeFavorites()
    }
}
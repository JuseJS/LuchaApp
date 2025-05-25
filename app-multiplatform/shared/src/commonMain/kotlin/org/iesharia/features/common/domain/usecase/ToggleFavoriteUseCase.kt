package org.iesharia.features.common.domain.usecase

import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.features.common.domain.repository.FavoriteRepository

class ToggleFavoriteUseCase(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(entityId: String, entityType: EntityTypeDto, currentState: Boolean): Result<Unit> {
        return if (currentState) {
            favoriteRepository.removeFavorite(entityId, entityType)
        } else {
            favoriteRepository.addFavorite(entityId, entityType)
        }
    }
}
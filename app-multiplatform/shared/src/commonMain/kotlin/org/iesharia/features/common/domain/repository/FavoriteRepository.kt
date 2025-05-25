package org.iesharia.features.common.domain.repository

import kotlinx.coroutines.flow.Flow
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.core.network.mapper.FavoriteInfo

interface FavoriteRepository {
    suspend fun getFavorites(type: EntityTypeDto? = null): Result<List<FavoriteInfo>>
    suspend fun getFavoriteIds(type: EntityTypeDto? = null): Result<List<String>>
    suspend fun addFavorite(entityId: String, entityType: EntityTypeDto): Result<Unit>
    suspend fun removeFavorite(entityId: String, entityType: EntityTypeDto): Result<Unit>
    suspend fun isFavorite(entityId: String, entityType: EntityTypeDto): Result<Boolean>
    fun observeFavorites(): Flow<Set<String>>
}
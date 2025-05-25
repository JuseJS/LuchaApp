package org.iesharia.features.common.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.core.network.mapper.FavoriteInfo
import org.iesharia.core.network.mapper.toDomain
import org.iesharia.core.network.service.FavoriteService
import org.iesharia.features.common.domain.repository.FavoriteRepository

class HttpFavoriteRepository(
    private val favoriteService: FavoriteService
) : FavoriteRepository {
    
    private val _favoritesFlow = MutableStateFlow<Set<String>>(emptySet())
    
    override suspend fun getFavorites(type: EntityTypeDto?): Result<List<FavoriteInfo>> {
        return try {
            val favorites = favoriteService.getFavorites(type)
            Result.success(favorites.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getFavoriteIds(type: EntityTypeDto?): Result<List<String>> {
        return try {
            val favorites = favoriteService.getFavorites(type)
            val ids = favorites.map { it.entityId }
            // Update our local cache with all favorites
            if (type == null) {
                _favoritesFlow.value = ids.toSet()
            }
            Result.success(ids)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun addFavorite(entityId: String, entityType: EntityTypeDto): Result<Unit> {
        return try {
            favoriteService.addFavorite(entityId, entityType)
            // Update local cache
            _favoritesFlow.value = _favoritesFlow.value + entityId
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFavorite(entityId: String, entityType: EntityTypeDto): Result<Unit> {
        return try {
            favoriteService.removeFavorite(entityId, entityType)
            // Update local cache
            _favoritesFlow.value = _favoritesFlow.value - entityId
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isFavorite(entityId: String, entityType: EntityTypeDto): Result<Boolean> {
        return try {
            val result = favoriteService.checkFavorite(entityId, entityType)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun observeFavorites(): Flow<Set<String>> {
        return _favoritesFlow.asStateFlow()
    }
}
package org.iesharia.domain.services

import kotlinx.datetime.Clock
import org.iesharia.data.models.EntityType
import org.iesharia.data.models.FavoriteDocument
import org.iesharia.data.repositories.FavoriteRepository
import org.iesharia.domain.models.dto.CreateFavoriteRequest
import org.iesharia.domain.models.dto.FavoriteDto

class FavoriteService(
    private val favoriteRepository: FavoriteRepository
) {
    
    suspend fun getUserFavorites(userId: String): List<FavoriteDto> {
        return favoriteRepository.findByUserId(userId).map { it.toDto() }
    }
    
    suspend fun getUserFavoritesByType(userId: String, type: String): List<FavoriteDto> {
        val entityType = try {
            EntityType.valueOf(type.uppercase())
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid entity type: $type")
        }
        
        return favoriteRepository.findByUserIdAndType(userId, entityType).map { it.toDto() }
    }
    
    suspend fun isFavorite(userId: String, entityId: String, entityType: String): Boolean {
        val type = try {
            EntityType.valueOf(entityType.uppercase())
        } catch (e: Exception) {
            return false
        }
        
        return favoriteRepository.findByUserIdAndEntity(userId, entityId, type) != null
    }
    
    suspend fun addFavorite(userId: String, request: CreateFavoriteRequest): FavoriteDto {
        val entityType = try {
            EntityType.valueOf(request.entityType.uppercase())
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid entity type: ${request.entityType}")
        }
        
        // Verificar si ya existe
        val existing = favoriteRepository.findByUserIdAndEntity(userId, request.entityId, entityType)
        if (existing != null) {
            return existing.toDto()
        }
        
        val favorite = FavoriteDocument(
            userId = userId,
            entityId = request.entityId,
            entityType = entityType,
            createdAt = Clock.System.now()
        )
        
        val saved = favoriteRepository.create(favorite)
        return saved.toDto()
    }
    
    suspend fun removeFavorite(userId: String, entityId: String, entityType: String): Boolean {
        val type = try {
            EntityType.valueOf(entityType.uppercase())
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid entity type: $entityType")
        }
        
        return favoriteRepository.delete(userId, entityId, type)
    }
    
    suspend fun removeAllUserFavorites(userId: String): Boolean {
        return favoriteRepository.deleteAllByUserId(userId)
    }
}

private fun FavoriteDocument.toDto(): FavoriteDto {
    return FavoriteDto(
        id = this.id,
        userId = this.userId,
        entityId = this.entityId,
        entityType = this.entityType.name,
        createdAt = this.createdAt.toString()
    )
}
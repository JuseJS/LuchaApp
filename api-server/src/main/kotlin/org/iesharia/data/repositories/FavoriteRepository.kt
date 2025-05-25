package org.iesharia.data.repositories

import org.iesharia.data.models.FavoriteDocument
import org.iesharia.data.models.EntityType

interface FavoriteRepository {
    suspend fun findByUserId(userId: String): List<FavoriteDocument>
    suspend fun findByUserIdAndType(userId: String, type: EntityType): List<FavoriteDocument>
    suspend fun findByUserIdAndEntity(userId: String, entityId: String, entityType: EntityType): FavoriteDocument?
    suspend fun create(favorite: FavoriteDocument): FavoriteDocument
    suspend fun delete(userId: String, entityId: String, entityType: EntityType): Boolean
    suspend fun deleteAllByUserId(userId: String): Boolean
}
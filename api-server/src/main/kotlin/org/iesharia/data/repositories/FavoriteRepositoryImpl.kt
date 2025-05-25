package org.iesharia.data.repositories

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.iesharia.data.models.EntityType
import org.iesharia.data.models.FavoriteDocument

class FavoriteRepositoryImpl(
    private val database: MongoDatabase
) : FavoriteRepository {
    
    private val collection = database.getCollection<FavoriteDocument>("favorites")
    
    override suspend fun findByUserId(userId: String): List<FavoriteDocument> {
        return collection.find(Filters.eq("userId", userId)).toList()
    }
    
    override suspend fun findByUserIdAndType(userId: String, type: EntityType): List<FavoriteDocument> {
        return collection.find(
            Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("entityType", type.name)
            )
        ).toList()
    }
    
    override suspend fun findByUserIdAndEntity(
        userId: String, 
        entityId: String, 
        entityType: EntityType
    ): FavoriteDocument? {
        return collection.find(
            Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("entityId", entityId),
                Filters.eq("entityType", entityType.name)
            )
        ).toList().firstOrNull()
    }
    
    override suspend fun create(favorite: FavoriteDocument): FavoriteDocument {
        collection.insertOne(favorite)
        return favorite
    }
    
    override suspend fun delete(userId: String, entityId: String, entityType: EntityType): Boolean {
        val result = collection.deleteOne(
            Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("entityId", entityId),
                Filters.eq("entityType", entityType.name)
            )
        )
        return result.deletedCount > 0
    }
    
    override suspend fun deleteAllByUserId(userId: String): Boolean {
        val result = collection.deleteMany(Filters.eq("userId", userId))
        return result.deletedCount > 0
    }
}
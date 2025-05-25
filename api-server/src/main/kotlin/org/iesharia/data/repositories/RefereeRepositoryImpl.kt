package org.iesharia.data.repositories

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.types.ObjectId
import org.iesharia.data.models.RefereeDocument

class RefereeRepositoryImpl(
    private val database: MongoDatabase
) : RefereeRepository {
    
    private val collection = database.getCollection<RefereeDocument>("referees")
    
    override suspend fun findAll(): List<RefereeDocument> {
        return collection.find().toList()
    }
    
    override suspend fun findById(id: String): RefereeDocument? {
        return try {
            collection.find(Document("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findActive(): List<RefereeDocument> {
        return collection.find(Document("isActive", true)).toList()
    }
    
    override suspend fun create(referee: RefereeDocument): RefereeDocument {
        collection.insertOne(referee)
        return referee
    }
    
    override suspend fun update(id: String, referee: RefereeDocument): RefereeDocument? {
        val result = collection.replaceOne(
            Document("_id", ObjectId(id)),
            referee
        )
        return if (result.modifiedCount > 0) referee else null
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(Document("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
}
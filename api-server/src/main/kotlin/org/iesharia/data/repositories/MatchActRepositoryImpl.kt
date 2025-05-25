package org.iesharia.data.repositories

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import org.bson.types.ObjectId
import org.iesharia.data.models.MatchActDocument

class MatchActRepositoryImpl(
    private val database: MongoDatabase
) : MatchActRepository {
    
    private val collection = database.getCollection<MatchActDocument>("matchActs")
    
    override suspend fun findById(id: String): MatchActDocument? {
        return try {
            collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByMatchId(matchId: String): MatchActDocument? {
        return collection.find(Filters.eq("matchId", matchId)).firstOrNull()
    }
    
    override suspend fun create(matchAct: MatchActDocument): MatchActDocument {
        collection.insertOne(matchAct)
        return matchAct
    }
    
    override suspend fun update(id: String, matchAct: MatchActDocument): MatchActDocument? {
        val result = collection.replaceOne(
            Filters.eq("_id", ObjectId(id)),
            matchAct
        )
        return if (result.modifiedCount > 0) matchAct else null
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
}
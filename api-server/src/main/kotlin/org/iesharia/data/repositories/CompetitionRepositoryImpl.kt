package org.iesharia.data.repositories

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId
import org.iesharia.data.models.CompetitionDocument

class CompetitionRepositoryImpl(
    private val database: MongoDatabase
) : CompetitionRepository {
    
    private val collection = database.getCollection<CompetitionDocument>("competitions")
    
    override suspend fun findAll(): List<CompetitionDocument> {
        return collection.find().toList()
    }
    
    override suspend fun findById(id: String): CompetitionDocument? {
        return try {
            collection.find(org.bson.Document("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByFilters(
        ageCategory: String?,
        divisionCategory: String?,
        island: String?,
        season: String?
    ): List<CompetitionDocument> {
        val filters = mutableListOf<org.bson.Document>()
        
        ageCategory?.let { filters.add(org.bson.Document("ageCategory", it)) }
        divisionCategory?.let { filters.add(org.bson.Document("divisionCategory", it)) }
        island?.let { filters.add(org.bson.Document("island", it)) }
        season?.let { filters.add(org.bson.Document("season", it)) }
        
        val query = if (filters.isEmpty()) {
            org.bson.Document()
        } else {
            org.bson.Document("\$and", filters)
        }
        
        return collection.find(query).toList()
    }
    
    override suspend fun create(competition: CompetitionDocument): CompetitionDocument {
        collection.insertOne(competition)
        return competition
    }
    
    override suspend fun update(id: String, competition: CompetitionDocument): CompetitionDocument? {
        val result = collection.replaceOne(
            org.bson.Document("_id", ObjectId(id)),
            competition
        )
        return if (result.modifiedCount > 0) competition else null
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(org.bson.Document("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
}
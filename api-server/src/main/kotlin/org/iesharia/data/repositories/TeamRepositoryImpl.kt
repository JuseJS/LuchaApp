package org.iesharia.data.repositories

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.bson.types.ObjectId
import org.iesharia.data.models.TeamDocument

class TeamRepositoryImpl(
    private val database: MongoDatabase
) : TeamRepository {
    
    private val collection = database.getCollection<TeamDocument>("teams")
    
    override suspend fun findAll(): List<TeamDocument> {
        return collection.find().toList()
    }
    
    override suspend fun findById(id: String): TeamDocument? {
        return try {
            collection.find(Document("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByName(name: String): TeamDocument? {
        return collection.find(Document("name", name)).firstOrNull()
    }
    
    override suspend fun findByDivision(division: String): List<TeamDocument> {
        return collection.find(Document("divisionCategory", division)).toList()
    }
    
    override suspend fun search(query: String): List<TeamDocument> {
        val searchDoc = Document("\$text", Document("\$search", query))
        return collection.find(searchDoc).toList()
    }
    
    override suspend fun create(team: TeamDocument): TeamDocument {
        collection.insertOne(team)
        return team
    }
    
    override suspend fun update(id: String, team: TeamDocument): TeamDocument? {
        val result = collection.replaceOne(
            Document("_id", ObjectId(id)),
            team
        )
        return if (result.modifiedCount > 0) team else null
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(Document("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
    
    override suspend fun existsByName(name: String): Boolean {
        return collection.countDocuments(Document("name", name)) > 0
    }
}
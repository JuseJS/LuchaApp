package org.iesharia.data.repositories

import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import org.bson.Document
import org.bson.types.ObjectId
import org.iesharia.data.models.MatchDocument
import java.time.ZoneId
import java.util.Date

class MatchRepositoryImpl(
    private val database: MongoDatabase
) : MatchRepository {
    
    private val collection = database.getCollection<MatchDocument>("matches")
    
    override suspend fun findAll(): List<MatchDocument> {
        return collection.find().toList()
    }
    
    override suspend fun findById(id: String): MatchDocument? {
        return try {
            collection.find(Document("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun findByTeamId(teamId: String): List<MatchDocument> {
        val query = Document("\$or", listOf(
            Document("localTeamId", teamId),
            Document("visitorTeamId", teamId)
        ))
        return collection.find(query).toList()
    }
    
    override suspend fun findByCompetitionId(competitionId: String): List<MatchDocument> {
        return collection.find(Document("competitionId", competitionId)).toList()
    }
    
    override suspend fun findUpcoming(from: LocalDate, limit: Int): List<MatchDocument> {
        val fromDate = Date.from(from.toJavaLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val query = Document("date", Document("\$gte", fromDate))
            .append("completed", false)
        
        return collection.find(query)
            .sort(Document("date", 1))
            .limit(limit)
            .toList()
    }
    
    override suspend fun findRecent(until: LocalDate, limit: Int): List<MatchDocument> {
        val untilDate = Date.from(until.toJavaLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val query = Document("date", Document("\$lte", untilDate))
            .append("completed", true)
        
        return collection.find(query)
            .sort(Document("date", -1))
            .limit(limit)
            .toList()
    }
    
    override suspend fun findByDateRange(start: LocalDate, end: LocalDate): List<MatchDocument> {
        val startDate = Date.from(start.toJavaLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        val endDate = Date.from(end.toJavaLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
        
        val query = Document("date", Document("\$gte", startDate).append("\$lte", endDate))
        
        return collection.find(query)
            .sort(Document("date", 1))
            .toList()
    }
    
    override suspend fun create(match: MatchDocument): MatchDocument {
        collection.insertOne(match)
        return match
    }
    
    override suspend fun update(id: String, match: MatchDocument): MatchDocument? {
        val result = collection.replaceOne(
            Document("_id", ObjectId(id)),
            match
        )
        return if (result.modifiedCount > 0) match else null
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(Document("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
    
    override suspend fun updateHasAct(matchId: String, hasAct: Boolean): Boolean {
        val result = collection.updateOne(
            Document("_id", ObjectId(matchId)),
            Document("\$set", Document("hasAct", hasAct))
        )
        return result.modifiedCount > 0
    }
    
    override suspend fun updateScore(matchId: String, localScore: Int, visitorScore: Int, completed: Boolean): Boolean {
        val result = collection.updateOne(
            Document("_id", ObjectId(matchId)),
            Document("\$set", Document()
                .append("localScore", localScore)
                .append("visitorScore", visitorScore)
                .append("completed", completed)
            )
        )
        return result.modifiedCount > 0
    }
}
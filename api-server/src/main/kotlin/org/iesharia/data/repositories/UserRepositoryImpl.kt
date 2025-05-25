package org.iesharia.data.repositories

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import kotlinx.datetime.toJavaInstant
import org.bson.types.ObjectId
import org.iesharia.data.models.UserDocument

class UserRepositoryImpl(
    private val database: MongoDatabase
) : UserRepository {
    
    private val collection = database.getCollection<UserDocument>("users")
    
    override suspend fun findById(id: String): UserDocument? {
        return try {
            collection.find(Filters.eq("_id", ObjectId(id))).firstOrNull()
        } catch (e: Exception) {
            null
        }
    }
    
    // Username is no longer used, only email for authentication
    
    override suspend fun findByEmail(email: String): UserDocument? {
        return collection.find(Filters.eq("email", email)).firstOrNull()
    }
    
    override suspend fun create(user: UserDocument): UserDocument {
        val result = collection.insertOne(user)
        return user.copy(_id = result.insertedId?.asObjectId()?.value ?: user._id)
    }
    
    override suspend fun update(id: String, user: UserDocument): UserDocument? {
        val updates = listOf(
            Updates.set("name", user.name),
            Updates.set("surname", user.surname),
            Updates.set("phone", user.phone),
            Updates.set("updatedAt", java.util.Date.from(Clock.System.now().toJavaInstant()))
        )
        
        val result = collection.updateOne(
            Filters.eq("_id", ObjectId(id)),
            Updates.combine(updates)
        )
        
        return if (result.modifiedCount > 0) {
            findById(id)
        } else {
            null
        }
    }
    
    override suspend fun updateRole(id: String, role: String): Boolean {
        val result = collection.updateOne(
            Filters.eq("_id", ObjectId(id)),
            Updates.combine(
                Updates.set("role", role),
                Updates.set("updatedAt", java.util.Date.from(Clock.System.now().toJavaInstant()))
            )
        )
        return result.modifiedCount > 0
    }
    
    override suspend fun updateTeamAssociation(id: String, teamId: String?): Boolean {
        val result = collection.updateOne(
            Filters.eq("_id", ObjectId(id)),
            Updates.combine(
                Updates.set("associatedTeamId", teamId),
                Updates.set("updatedAt", java.util.Date.from(Clock.System.now().toJavaInstant()))
            )
        )
        return result.modifiedCount > 0
    }
    
    override suspend fun updateLastLogin(id: String): Boolean {
        val now = Clock.System.now()
        val result = collection.updateOne(
            Filters.eq("_id", ObjectId(id)),
            Updates.set("lastLogin", java.util.Date.from(now.toJavaInstant()))
        )
        return result.modifiedCount > 0
    }
    
    override suspend fun delete(id: String): Boolean {
        val result = collection.deleteOne(Filters.eq("_id", ObjectId(id)))
        return result.deletedCount > 0
    }
    
    // Username is no longer used
    
    override suspend fun existsByEmail(email: String): Boolean {
        return collection.countDocuments(Filters.eq("email", email)) > 0
    }
}
package com.luchacanaria.features.auth.data.repository

import com.luchacanaria.core.database.DatabaseConfig
import com.luchacanaria.core.security.PasswordHasher
import com.luchacanaria.features.auth.data.document.UserDocument
import com.luchacanaria.features.auth.data.mapper.UserMapper
import com.luchacanaria.features.auth.domain.model.Role
import com.luchacanaria.features.auth.domain.model.User
import com.luchacanaria.features.auth.domain.repository.UserRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MongoUserRepository : UserRepository {
    private val collection = DatabaseConfig.usersCollection

    override suspend fun getUserById(id: String): User? {
        return try {
            val objectId = ObjectId(id)
            val document = collection.find(Filters.eq("_id", objectId)).firstOrNull()
            document?.let { UserMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUserByEmail(email: String): User? {
        val document = collection.find(Filters.eq(UserDocument::email.name, email)).firstOrNull()
        return document?.let { UserMapper.toDomain(it) }
    }

    override suspend fun createUser(user: User, password: String): User {
        val passwordHash = PasswordHasher.hashPassword(password)
        val document = UserMapper.toDocument(user, passwordHash)
        val result = collection.insertOne(document)
        return user.copy(id = result.insertedId!!.asObjectId().value.toHexString())
    }

    override suspend fun updateUser(user: User): User? {
        return try {
            val objectId = ObjectId(user.id)
            val existingDocument = collection.find(Filters.eq("_id", objectId)).firstOrNull() ?: return null
            
            val updatedDocument = existingDocument.copy(
                email = user.email,
                name = user.name,
                surname = user.surname,
                role = user.role.name,
                associatedTeamId = user.associatedTeamId,
                updatedAt = System.currentTimeMillis()
            )
            
            collection.replaceOne(Filters.eq("_id", objectId), updatedDocument)
            user
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteUser(id: String): Boolean {
        return try {
            val objectId = ObjectId(id)
            val result = collection.deleteOne(Filters.eq("_id", objectId))
            result.deletedCount > 0
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun verifyPassword(email: String, password: String): User? {
        val document = collection.find(Filters.eq(UserDocument::email.name, email)).firstOrNull()
        return if (document != null && PasswordHasher.verifyPassword(password, document.passwordHash)) {
            UserMapper.toDomain(document)
        } else {
            null
        }
    }

    override suspend fun getAllUsers(
        role: Role?,
        search: String?,
        page: Int,
        size: Int
    ): List<User> {
        val filters = mutableListOf<org.bson.conversions.Bson>()
        
        role?.let {
            filters.add(Filters.eq(UserDocument::role.name, it.name))
        }
        
        search?.let {
            val searchFilter = Filters.or(
                Filters.regex(UserDocument::name.name, it, "i"),
                Filters.regex(UserDocument::surname.name, it, "i"),
                Filters.regex(UserDocument::email.name, it, "i")
            )
            filters.add(searchFilter)
        }

        val filter = if (filters.isNotEmpty()) Filters.and(filters) else Filters.empty()
        
        return collection.find(filter)
            .sort(Sorts.descending(UserDocument::createdAt.name))
            .skip(page * size)
            .limit(size)
            .toList()
            .map { UserMapper.toDomain(it) }
    }

    override suspend fun updatePassword(userId: String, newPasswordHash: String): Boolean {
        return try {
            val objectId = ObjectId(userId)
            val result = collection.updateOne(
                Filters.eq("_id", objectId),
                Updates.set("passwordHash", newPasswordHash)
            )
            result.modifiedCount > 0
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getUsersByTeamId(teamId: String): List<User> {
        return collection.find(Filters.eq(UserDocument::associatedTeamId.name, teamId))
            .toList()
            .map { UserMapper.toDomain(it) }
    }
}
package org.iesharia.data.repositories

import org.iesharia.data.models.UserDocument

interface UserRepository {
    suspend fun findById(id: String): UserDocument?
    suspend fun findByEmail(email: String): UserDocument?
    suspend fun create(user: UserDocument): UserDocument
    suspend fun update(id: String, user: UserDocument): UserDocument?
    suspend fun updateRole(id: String, role: String): Boolean
    suspend fun updateTeamAssociation(id: String, teamId: String?): Boolean
    suspend fun updateLastLogin(id: String): Boolean
    suspend fun delete(id: String): Boolean
    suspend fun existsByEmail(email: String): Boolean
}
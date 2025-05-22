package com.luchacanaria.features.auth.domain.repository

import com.luchacanaria.features.auth.domain.model.User
import com.luchacanaria.features.auth.domain.model.Role

interface UserRepository {
    suspend fun getUserById(id: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun createUser(user: User, password: String): User
    suspend fun updateUser(user: User): User?
    suspend fun deleteUser(id: String): Boolean
    suspend fun verifyPassword(email: String, password: String): User?
    suspend fun getAllUsers(
        role: Role? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<User>
    suspend fun updatePassword(userId: String, newPasswordHash: String): Boolean
    suspend fun getUsersByTeamId(teamId: String): List<User>
}
package org.iesharia.features.auth.domain.repository

import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.model.Role

interface UserRepository {
    suspend fun login(email: String, password: String): User
    suspend fun register(name: String, surname: String, email: String, password: String): User
    suspend fun isUserLoggedIn(): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun logout()
    suspend fun getUserRole(userId: String): Role
    suspend fun updateUserRole(userId: String, role: Role): Boolean
    suspend fun assignTeamToCoach(userId: String, teamId: String): Boolean
}
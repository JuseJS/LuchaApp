package org.iesharia.domain.repository

interface UserRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun register(name: String, surname: String, email: String, password: String): Boolean
    suspend fun isUserLoggedIn(): Boolean
}
package org.iesharia.data.repository

import org.iesharia.domain.repository.UserRepository
import kotlinx.coroutines.delay

class MockUserRepository : UserRepository {
    private var isLoggedIn = false

    override suspend fun login(email: String, password: String): Boolean {
        // Simular latencia de red
        delay(1000)
        isLoggedIn = true
        return true
    }

    override suspend fun register(name: String, surname: String, email: String, password: String): Boolean {
        // Simular latencia de red
        delay(1000)
        isLoggedIn = true
        return true
    }

    override suspend fun isUserLoggedIn(): Boolean = isLoggedIn
}
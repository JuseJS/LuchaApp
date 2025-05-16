package org.iesharia.features.auth.data.repository

import org.iesharia.features.auth.domain.repository.UserRepository
import kotlinx.coroutines.delay
import org.iesharia.core.data.mock.MockDataGenerator

class MockUserRepository(
    private val mockDataGenerator: MockDataGenerator
) : UserRepository {
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
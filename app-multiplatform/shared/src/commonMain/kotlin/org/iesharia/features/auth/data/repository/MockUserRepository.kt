package org.iesharia.features.auth.data.repository

import org.iesharia.features.auth.domain.repository.UserRepository
import kotlinx.coroutines.delay
import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.AppError

class MockUserRepository(
    private val mockDataGenerator: MockDataGenerator
) : UserRepository {
    private var isLoggedIn = false

    override suspend fun login(email: String, password: String): Boolean {
        try {
            // Simular latencia de red
            delay(1000)

            // Simular validación
            if (email == "error@test.com") {
                throw AppError.AuthError(message = "Credenciales incorrectas")
            }

            isLoggedIn = true
            return true
        } catch (e: Exception) {
            // Si ya es un AppError, propagarlo
            if (e is AppError) throw e

            // Si no, convertirlo
            throw AppError.AuthError(message = "Error al iniciar sesión: ${e.message}")
        }
    }

    override suspend fun register(name: String, surname: String, email: String, password: String): Boolean {
        try {
            // Simular latencia de red
            delay(1000)

            // Simular validación
            if (email == "error@test.com") {
                throw AppError.ValidationError(message = "El correo ya está registrado")
            }

            isLoggedIn = true
            return true
        } catch (e: Exception) {
            // Si ya es un AppError, propagarlo
            if (e is AppError) throw e

            // Si no, convertirlo
            throw AppError.ServerError(message = "Error al registrar: ${e.message}")
        }
    }

    override suspend fun isUserLoggedIn(): Boolean = isLoggedIn
}
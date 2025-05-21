package org.iesharia.features.auth.data.repository

import org.iesharia.features.auth.domain.repository.UserRepository
import kotlinx.coroutines.delay
import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.AppError
import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.model.Role

class MockUserRepository(
    private val mockDataGenerator: MockDataGenerator
) : UserRepository {
    private var currentUser: User? = null

    // Base de datos mock de usuarios
    private val users = mutableMapOf(
        "admin@test.com" to User(
            id = "admin1",
            email = "admin@test.com",
            name = "Admin",
            surname = "User",
            role = Role.FEDERATIVE_DELEGATE
        ),
        "coach@test.com" to User(
            id = "coach1",
            email = "coach@test.com",
            name = "Coach",
            surname = "User",
            role = Role.COACH,
            associatedTeamId = "team1" // Asociado con el equipo Tegueste
        ),
        "user@test.com" to User(
            id = "user1",
            email = "user@test.com",
            name = "Regular",
            surname = "User",
            role = Role.REGISTERED_USER
        )
    )

    override suspend fun login(email: String, password: String): User {
        try {
            // Simular latencia de red
            delay(1000)

            // Simular validación
            if (email == "error@test.com") {
                throw AppError.AuthError(message = "Credenciales incorrectas")
            }

            // Obtener usuario o crear uno nuevo con rol predeterminado
            val user = users[email] ?: User(
                id = "user_${users.size + 1}",
                email = email,
                name = "New",
                surname = "User",
                role = Role.REGISTERED_USER
            )

            // Almacenar en la base de datos mock si no existe
            if (!users.containsKey(email)) {
                users[email] = user
            }

            // Establecer como usuario actual
            currentUser = user
            return user
        } catch (e: Exception) {
            // Si ya es un AppError, propagarlo
            if (e is AppError) throw e

            // Si no, convertirlo
            throw AppError.AuthError(message = "Error al iniciar sesión: ${e.message}")
        }
    }

    override suspend fun register(name: String, surname: String, email: String, password: String): User {
        try {
            // Simular latencia de red
            delay(1000)

            // Simular validación
            if (email == "error@test.com") {
                throw AppError.ValidationError(message = "El correo ya está registrado")
            }

            // Crear nuevo usuario con rol predeterminado
            val user = User(
                id = "user_${users.size + 1}",
                email = email,
                name = name,
                surname = surname,
                role = Role.REGISTERED_USER
            )

            // Almacenar en la base de datos mock
            users[email] = user

            // Establecer como usuario actual
            currentUser = user
            return user
        } catch (e: Exception) {
            // Si ya es un AppError, propagarlo
            if (e is AppError) throw e

            // Si no, convertirlo
            throw AppError.ServerError(message = "Error al registrar: ${e.message}")
        }
    }

    override suspend fun isUserLoggedIn(): Boolean = currentUser != null

    override suspend fun getCurrentUser(): User? = currentUser

    override suspend fun logout() {
        currentUser = null
    }

    override suspend fun getUserRole(userId: String): Role {
        return users.values.find { it.id == userId }?.role ?: Role.GUEST
    }

    override suspend fun updateUserRole(userId: String, role: Role): Boolean {
        val user = users.values.find { it.id == userId } ?: return false

        // Actualizar rol de usuario
        val updatedUser = user.copy(role = role)
        users[updatedUser.email] = updatedUser

        // Si es el usuario actual, actualizar también
        if (currentUser?.id == userId) {
            currentUser = updatedUser
        }

        return true
    }

    override suspend fun assignTeamToCoach(userId: String, teamId: String): Boolean {
        val user = users.values.find { it.id == userId } ?: return false

        // Asegurar que el usuario sea entrenador
        if (user.role != Role.COACH) {
            return false
        }

        // Actualizar usuario con asociación de equipo
        val updatedUser = user.copy(associatedTeamId = teamId)
        users[updatedUser.email] = updatedUser

        // Si es el usuario actual, actualizar también
        if (currentUser?.id == userId) {
            currentUser = updatedUser
        }

        return true
    }
}
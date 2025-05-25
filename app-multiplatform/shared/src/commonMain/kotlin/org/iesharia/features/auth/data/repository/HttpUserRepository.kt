package org.iesharia.features.auth.data.repository

import org.iesharia.core.auth.AuthenticationManager
import org.iesharia.core.domain.model.AppError
import org.iesharia.features.auth.domain.model.Role
import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.repository.UserRepository

class HttpUserRepository(
    private val authManager: AuthenticationManager
) : UserRepository {
    
    override suspend fun login(email: String, password: String): User {
        println("🔐 HttpUserRepository.login - Iniciando login para: $email")
        val result = authManager.login(email, password)
        return result.getOrElse { error ->
            println("❌ HttpUserRepository.login - Error: ${error.message}")
            println("❌ Tipo de error: ${error::class.simpleName}")
            error.printStackTrace()
            throw AppError.AuthError(message = error.message ?: "Login failed")
        }
    }
    
    override suspend fun register(
        name: String, 
        surname: String, 
        email: String, 
        password: String
    ): User {
        val result = authManager.register(name, surname, email, password)
        return result.getOrElse { error ->
            // Check if it's a password validation error
            val message = error.message ?: "Registration failed"
            if (message.contains("contraseña", ignoreCase = true) || 
                message.contains("password", ignoreCase = true) ||
                message.contains("caracteres", ignoreCase = true) ||
                message.contains("mayúscula", ignoreCase = true) ||
                message.contains("especial", ignoreCase = true)) {
                throw AppError.ValidationError(field = "password", message = message)
            }
            throw AppError.ServerError(message = message)
        }
    }
    
    override suspend fun isUserLoggedIn(): Boolean {
        return authManager.isAuthenticated()
    }
    
    override suspend fun getCurrentUser(): User? {
        return authManager.getCurrentUser()
    }
    
    override suspend fun logout() {
        authManager.logout()
    }
    
    override suspend fun getUserRole(userId: String): Role {
        // If it's the current user, return their role
        val currentUser = authManager.getCurrentUser()
        if (currentUser?.id == userId) {
            return currentUser.role
        }
        
        // For other users, this would require an API call to get user details
        // For now, return GUEST as default
        return Role.GUEST
    }
    
    override suspend fun updateUserRole(userId: String, role: Role): Boolean {
        // This would require an admin API endpoint
        // For now, only allow updating current user role in memory
        val currentUser = authManager.getCurrentUser()
        if (currentUser?.id == userId) {
            // Update would need to be reflected in token storage and auth manager
            return true
        }
        return false
    }
    
    override suspend fun assignTeamToCoach(userId: String, teamId: String): Boolean {
        // This would require an admin API endpoint
        // For now, return false as not implemented
        return false
    }
}
package com.luchacanaria.features.auth.domain.usecase

import com.luchacanaria.features.auth.domain.model.Role
import com.luchacanaria.features.auth.domain.model.User
import com.luchacanaria.features.auth.domain.repository.UserRepository
import java.util.UUID

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val role: Role = Role.GUEST,
    val associatedTeamId: String? = null
)

class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(request: RegisterRequest): User? {
        // Check if user already exists
        val existingUser = userRepository.getUserByEmail(request.email)
        if (existingUser != null) {
            return null // User already exists
        }
        
        val user = User(
            id = UUID.randomUUID().toString(),
            email = request.email,
            name = request.name,
            surname = request.surname,
            role = request.role,
            associatedTeamId = request.associatedTeamId
        )
        
        return userRepository.createUser(user, request.password)
    }
}
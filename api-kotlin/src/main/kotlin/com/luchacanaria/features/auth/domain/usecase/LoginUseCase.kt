package com.luchacanaria.features.auth.domain.usecase

import com.luchacanaria.core.security.JWTService
import com.luchacanaria.features.auth.domain.repository.UserRepository

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: com.luchacanaria.features.auth.domain.model.User
)

class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(request: LoginRequest): LoginResponse? {
        val user = userRepository.verifyPassword(request.email, request.password)
            ?: return null
        
        val token = JWTService.generateToken(
            userId = user.id,
            email = user.email,
            role = user.role.name
        )
        
        return LoginResponse(token = token, user = user)
    }
}
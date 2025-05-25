package org.iesharia.domain.models.dto

import kotlinx.serialization.Serializable
import org.iesharia.domain.models.UserRole

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val surname: String,
    val phone: String? = null,
    val role: UserRole = UserRole.REGISTERED_USER
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserDto,
    val expiresIn: Long
)

@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val phone: String? = null,
    val role: String,
    val permissions: List<String> = emptyList(),
    val associatedTeamId: String? = null,
    val isActive: Boolean = true
)
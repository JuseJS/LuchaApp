package org.iesharia.core.network.dto

import kotlinx.serialization.Serializable

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
    val surname: String
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
    val role: String,  // Keep as String for now, will map to enum in domain
    val permissions: List<String> = emptyList(),
    val associatedTeamId: String? = null,
    val isActive: Boolean = true
)
package org.iesharia.core.network.mapper

import org.iesharia.core.network.dto.UserDto
import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.model.Role

object AuthMappers {
    
    fun UserDto.toDomain(): User {
        return User(
            id = this.id,
            email = this.email,
            name = this.name,
            surname = this.surname,
            role = when (this.role) {
                "FEDERATIVE_DELEGATE" -> Role.FEDERATIVE_DELEGATE
                "COACH" -> Role.COACH
                "REGISTERED_USER" -> Role.REGISTERED_USER
                "GUEST" -> Role.GUEST
                else -> Role.REGISTERED_USER
            },
            associatedTeamId = this.associatedTeamId
        )
    }
    
    fun User.toDto(): UserDto {
        return UserDto(
            id = this.id,
            email = this.email,
            name = this.name,
            surname = this.surname,
            phone = null,
            role = when (this.role) {
                Role.FEDERATIVE_DELEGATE -> "FEDERATIVE_DELEGATE"
                Role.COACH -> "COACH"
                Role.REGISTERED_USER -> "REGISTERED_USER"
                Role.GUEST -> "GUEST"
            },
            permissions = emptyList(), // Will be managed by the server
            associatedTeamId = this.associatedTeamId,
            isActive = true
        )
    }
}
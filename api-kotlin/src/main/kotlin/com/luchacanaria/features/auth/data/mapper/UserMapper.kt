package com.luchacanaria.features.auth.data.mapper

import com.luchacanaria.features.auth.data.document.UserDocument
import com.luchacanaria.features.auth.domain.model.User
import com.luchacanaria.features.auth.domain.model.Role

object UserMapper {
    fun toDomain(document: UserDocument): User {
        return User(
            id = document.id,
            email = document.email,
            name = document.name,
            surname = document.surname,
            role = Role.valueOf(document.role),
            associatedTeamId = document.associatedTeamId
        )
    }

    fun toDocument(user: User, passwordHash: String): UserDocument {
        return UserDocument(
            email = user.email,
            passwordHash = passwordHash,
            name = user.name,
            surname = user.surname,
            role = user.role.name,
            associatedTeamId = user.associatedTeamId
        )
    }
}
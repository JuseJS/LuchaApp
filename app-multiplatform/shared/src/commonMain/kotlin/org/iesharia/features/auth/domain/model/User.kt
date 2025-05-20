package org.iesharia.features.auth.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val surname: String,
    val role: Role = Role.INVITADO,
    val associatedTeamId: String? = null // Solo para rol ENTRENADOR
) {
    val fullName: String get() = "$name $surname"

    fun hasPermission(permission: Permission): Boolean {
        return role.hasPermission(permission)
    }

    fun isTeamCoach(teamId: String): Boolean {
        return role == Role.ENTRENADOR && associatedTeamId == teamId
    }
}
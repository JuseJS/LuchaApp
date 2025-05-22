package com.luchacanaria.features.auth.domain.model

enum class Role {
    ADMIN,
    ORGANIZER,
    COACH,
    GUEST;

    fun hasPermission(permission: Permission): Boolean {
        return when (this) {
            ADMIN -> true // Admin tiene todos los permisos
            ORGANIZER -> permission in organizerPermissions
            COACH -> permission in coachPermissions
            GUEST -> permission in guestPermissions
        }
    }

    companion object {
        private val guestPermissions = setOf(
            Permission.READ_WRESTLERS,
            Permission.READ_TEAMS,
            Permission.READ_COMPETITIONS,
            Permission.READ_MATCHES
        )

        private val coachPermissions = guestPermissions + setOf(
            Permission.MANAGE_TEAM_WRESTLERS,
            Permission.MANAGE_MATCH_ACTS
        )

        private val organizerPermissions = coachPermissions + setOf(
            Permission.CREATE_COMPETITIONS,
            Permission.MANAGE_COMPETITIONS,
            Permission.CREATE_MATCHES,
            Permission.MANAGE_MATCHES,
            Permission.CREATE_WRESTLERS,
            Permission.MANAGE_WRESTLERS,
            Permission.CREATE_TEAMS,
            Permission.MANAGE_TEAMS
        )
    }
}
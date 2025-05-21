package org.iesharia.features.auth.domain.model

enum class Role {
    GUEST,
    REGISTERED_USER,
    COACH,
    FEDERATIVE_DELEGATE;

    /**
     * Obtiene los permisos para este rol, incluyendo permisos heredados
     */
    fun getPermissions(): Set<Permission> {
        return when (this) {
            GUEST -> setOf(
                Permission.VIEW_PUBLIC_CONTENT
            )
            REGISTERED_USER -> GUEST.getPermissions() + setOf(
                Permission.MANAGE_FAVORITES
            )
            COACH -> REGISTERED_USER.getPermissions() + setOf(
                Permission.MANAGE_TEAM_WRESTLERS
            )
            FEDERATIVE_DELEGATE -> REGISTERED_USER.getPermissions() + setOf(
                Permission.MODIFY_MATCH_SCHEDULE,
                Permission.MANAGE_MATCH_RECORDS
            )
        }
    }

    /**
     * Verifica si este rol tiene un permiso específico
     */
    fun hasPermission(permission: Permission): Boolean {
        return permission in getPermissions()
    }
}
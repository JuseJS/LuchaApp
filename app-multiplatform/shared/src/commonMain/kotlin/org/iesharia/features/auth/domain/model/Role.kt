package org.iesharia.features.auth.domain.model

enum class Role {
    INVITADO,
    USUARIO_REGISTRADO,
    ENTRENADOR,
    DELEGADO_FEDERATIVO;

    /**
     * Obtiene los permisos para este rol, incluyendo permisos heredados
     */
    fun getPermissions(): Set<Permission> {
        return when (this) {
            INVITADO -> setOf(
                Permission.VIEW_PUBLIC_CONTENT
            )
            USUARIO_REGISTRADO -> INVITADO.getPermissions() + setOf(
                Permission.MANAGE_FAVORITES
            )
            ENTRENADOR -> USUARIO_REGISTRADO.getPermissions() + setOf(
                Permission.MANAGE_TEAM_WRESTLERS
            )
            DELEGADO_FEDERATIVO -> USUARIO_REGISTRADO.getPermissions() + setOf(
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
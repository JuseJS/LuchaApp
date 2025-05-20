package org.iesharia.features.auth.domain.security

import org.iesharia.features.auth.domain.model.Permission

class RolePermissionManager(private val sessionManager: SessionManager) {

    /**
     * Verifica si el usuario actual tiene el permiso especificado
     */
    fun hasPermission(permission: Permission): Boolean {
        val currentUser = sessionManager.getCurrentUser() ?: return false
        return currentUser.hasPermission(permission)
    }

    /**
     * Verifica si el usuario actual es entrenador del equipo especificado
     */
    fun isTeamCoach(teamId: String): Boolean {
        val currentUser = sessionManager.getCurrentUser() ?: return false
        return currentUser.isTeamCoach(teamId)
    }

    /**
     * Obtiene el ID del equipo asociado al usuario actual (si es entrenador)
     */
    fun getAssociatedTeamId(): String? {
        val currentUser = sessionManager.getCurrentUser() ?: return null
        return currentUser.associatedTeamId
    }
}
package org.iesharia.features.auth.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import org.iesharia.features.auth.domain.model.Permission
import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.security.RolePermissionManager
import org.iesharia.features.auth.domain.security.SessionManager
import org.koin.compose.koinInject

/**
 * Helper para manejar visibilidad y comportamiento de UI basado en roles
 */
object RoleBasedUIHelper {

    /**
     * Verifica si el usuario actual tiene un permiso específico
     */
    @Composable
    fun hasPermission(
        permission: Permission,
        roleManager: RolePermissionManager = koinInject()
    ): Boolean {
        return roleManager.hasPermission(permission)
    }

    /**
     * Verifica si el usuario actual es entrenador de un equipo específico
     */
    @Composable
    fun isTeamCoach(
        teamId: String,
        roleManager: RolePermissionManager = koinInject()
    ): Boolean {
        return roleManager.isTeamCoach(teamId)
    }

    /**
     * Obtiene el usuario actual
     */
    @Composable
    fun getCurrentUser(
        sessionManager: SessionManager = koinInject()
    ): State<User?> {
        return sessionManager.currentUser.collectAsState()
    }

    /**
     * Verifica si hay un usuario con sesión iniciada
     */
    @Composable
    fun isLoggedIn(
        sessionManager: SessionManager = koinInject()
    ): State<Boolean> {
        return sessionManager.isLoggedIn.collectAsState()
    }

    /**
     * Composable helper que renderiza contenido solo si el usuario tiene el permiso especificado
     */
    @Composable
    fun WithPermission(
        permission: Permission,
        content: @Composable () -> Unit
    ) {
        if (hasPermission(permission)) {
            content()
        }
    }

    /**
     * Composable helper que renderiza diferente contenido basado en si el usuario tiene un permiso
     */
    @Composable
    fun WithPermissionElse(
        permission: Permission,
        content: @Composable () -> Unit,
        elseContent: @Composable () -> Unit = {}
    ) {
        if (hasPermission(permission)) {
            content()
        } else {
            elseContent()
        }
    }
}
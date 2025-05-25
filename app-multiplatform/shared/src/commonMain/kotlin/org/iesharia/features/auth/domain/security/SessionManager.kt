package org.iesharia.features.auth.domain.security

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.iesharia.features.auth.domain.model.User
import org.iesharia.core.network.TokenStorage

class SessionManager(
    private val tokenStorage: TokenStorage
) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    /**
     * Establece el usuario autenticado actual
     */
    fun setCurrentUser(user: User) {
        _currentUser.value = user
        _isLoggedIn.value = true

        // Aquí también se podría persistir en almacenamiento local
    }

    /**
     * Obtiene el usuario autenticado actual
     */
    fun getCurrentUser(): User? {
        return _currentUser.value
    }

    /**
     * Verifica si hay un usuario con sesión iniciada
     */
    fun isUserLoggedIn(): Boolean {
        return _isLoggedIn.value
    }

    /**
     * Cierra la sesión del usuario actual
     */
    fun logout() {
        println("🔓 SessionManager: Iniciando logout")
        _currentUser.value = null
        _isLoggedIn.value = false
        
        // Limpiar el token almacenado
        tokenStorage.clearAll()
        println("✅ SessionManager: Logout completado - usuario: ${_currentUser.value}, isLoggedIn: ${_isLoggedIn.value}")
    }
}
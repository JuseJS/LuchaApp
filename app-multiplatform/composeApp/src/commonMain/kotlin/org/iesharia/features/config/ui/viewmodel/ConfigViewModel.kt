package org.iesharia.features.config.ui.viewmodel

import kotlinx.coroutines.delay
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
import org.iesharia.features.auth.domain.security.SessionManager
import org.iesharia.features.auth.domain.usecase.LogoutUseCase

data class ConfigUiState(
    val isLoading: Boolean = false,
    val userName: String? = null,
    val userEmail: String? = null
)

class ConfigViewModel(
    private val sessionManager: SessionManager,
    private val logoutUseCase: LogoutUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<ConfigUiState>(ConfigUiState(), errorHandler, navigationManager) {

    init {
        loadUserInfo()
        observeUserChanges()
    }

    private fun loadUserInfo() {
        val currentUser = sessionManager.currentUser.value
        updateState { state ->
            state.copy(
                userName = currentUser?.name,
                userEmail = currentUser?.email
            )
        }
    }
    
    private fun observeUserChanges() {
        launchSafe {
            sessionManager.currentUser.collect { user ->
                updateState { state ->
                    state.copy(
                        userName = user?.name,
                        userEmail = user?.email
                    )
                }
            }
        }
    }

    /**
     * Cierra la sesión del usuario
     */
    fun logout() {
        launchSafe {
            updateState { it.copy(isLoading = true) }
            
            // Cerrar sesión usando el caso de uso
            logoutUseCase()
            
            // Pequeña demora para asegurar que los cambios se propaguen
            delay(100)
            
            // Navegar al login y limpiar el back stack
            navigationManager?.navigate(Routes.Auth.Login, clearBackStack = true)
        }
    }
}
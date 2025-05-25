package org.iesharia.features.auth.domain.usecase

import org.iesharia.features.auth.domain.repository.UserRepository
import org.iesharia.features.auth.domain.security.SessionManager

class LogoutUseCase(
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) {
    suspend operator fun invoke() {
        // Logout from repository first
        userRepository.logout()
        
        // Then clear session manager
        sessionManager.logout()
    }
}
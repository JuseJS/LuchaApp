package org.iesharia.features.auth.domain.usecase

import org.iesharia.features.auth.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean =
        repository.login(email, password)
}
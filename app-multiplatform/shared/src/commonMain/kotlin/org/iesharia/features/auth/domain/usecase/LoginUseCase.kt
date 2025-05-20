package org.iesharia.features.auth.domain.usecase

import org.iesharia.features.auth.domain.model.User
import org.iesharia.features.auth.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): User =
        repository.login(email, password)
}
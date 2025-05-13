package org.iesharia.domain.usecase

import org.iesharia.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Boolean =
        repository.login(email, password)
}
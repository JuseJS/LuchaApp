package org.iesharia.features.auth.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.resources.AppStrings
import org.iesharia.features.auth.domain.repository.UserRepository
import org.iesharia.features.auth.domain.security.SessionManager

class LoginViewModel(
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<LoginUiState>(LoginUiState(), errorHandler, navigationManager) {

    init {
        // Verificar si el usuario ya tiene sesión iniciada
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        launchSafe {
            val isLoggedIn = userRepository.isUserLoggedIn()
            val currentUser = userRepository.getCurrentUser()

            updateState {
                it.copy(
                    isLoggedIn = isLoggedIn,
                    currentUser = currentUser
                )
            }

            // Si ya tiene sesión iniciada, ir a home
            if (isLoggedIn && currentUser != null) {
                sessionManager.setCurrentUser(currentUser)
                navigateToHome()
            }
        }
    }

    // Cambiar entre modos login/registro
    fun toggleAuthMode() {
        updateState { it.copy(isLoginMode = !it.isLoginMode) }
    }

    // Métodos para actualizar campos del login
    fun updateLoginEmail(email: String) {
        updateState { it.copy(loginEmail = email, loginEmailError = "") }
    }

    fun updateLoginPassword(password: String) {
        updateState { it.copy(loginPassword = password, loginPasswordError = "") }
    }

    // Métodos para actualizar campos del registro
    fun updateRegisterName(name: String) {
        updateState { it.copy(registerName = name, registerNameError = "") }
    }

    fun updateRegisterSurname(surname: String) {
        updateState { it.copy(registerSurname = surname, registerSurnameError = "") }
    }

    fun updateRegisterEmail(email: String) {
        updateState { it.copy(registerEmail = email, registerEmailError = "") }
    }

    fun updateRegisterPassword(password: String) {
        updateState {
            it.copy(
                registerPassword = password,
                registerPasswordError = "",
                // Validar confirmación de contraseña si ya existe
                registerConfirmPasswordError = if (it.registerConfirmPassword.isNotEmpty() &&
                    it.registerConfirmPassword != password) {
                    "Las contraseñas no coinciden"
                } else {
                    ""
                }
            )
        }
    }

    fun updateRegisterConfirmPassword(confirmPassword: String) {
        updateState {
            it.copy(
                registerConfirmPassword = confirmPassword,
                registerConfirmPasswordError = if (confirmPassword != it.registerPassword) {
                    "Las contraseñas no coinciden"
                } else {
                    ""
                }
            )
        }
    }

    // Validación y envío de login
    fun submitLogin() {
        launchSafe(
            errorHandler = { error ->
                when (error) {
                    is AppError.ValidationError -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                loginEmailError = if (error.field == "email") error.message else it.loginEmailError,
                                loginPasswordError = if (error.field == "password") error.message else it.loginPasswordError
                            )
                        }
                    }
                    is AppError.AuthError -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                loginPasswordError = error.message
                            )
                        }
                    }
                    else -> {
                        updateState { it.copy(isLoading = false) }
                    }
                }
            }
        ) {
            val currentState = uiState.value

            // Validar campos
            if (currentState.loginEmail.isBlank()) {
                throw AppError.ValidationError(field = "email", message = AppStrings.Auth.emailRequired)
            } else if (!isValidEmail(currentState.loginEmail)) {
                throw AppError.ValidationError(field = "email", message = AppStrings.Auth.invalidEmail)
            }

            if (currentState.loginPassword.isBlank()) {
                throw AppError.ValidationError(field = "password", message = AppStrings.Auth.passwordRequired)
            }

            // Iniciar carga
            updateState { it.copy(isLoading = true) }

            // Realizar login y obtener usuario
            val user = userRepository.login(currentState.loginEmail, currentState.loginPassword)

            // Almacenar usuario en sesión
            sessionManager.setCurrentUser(user)

            // Actualizar estado
            updateState {
                it.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentUser = user
                )
            }

            // Navegación a home
            navigateToHome()
        }
    }

    // Validación y envío de registro
    fun submitRegister() {
        launchSafe(
            errorHandler = { error ->
                when (error) {
                    is AppError.ValidationError -> {
                        val field = error.field
                        val errorMessage = error.message

                        updateState {
                            when (field) {
                                "email" -> it.copy(isLoading = false, registerEmailError = errorMessage)
                                "password" -> it.copy(isLoading = false, registerPasswordError = errorMessage)
                                "name" -> it.copy(isLoading = false, registerNameError = errorMessage)
                                "surname" -> it.copy(isLoading = false, registerSurnameError = errorMessage)
                                "confirmPassword" -> it.copy(isLoading = false, registerConfirmPasswordError = errorMessage)
                                else -> it.copy(isLoading = false, registerPasswordError = errorMessage)
                            }
                        }
                    }
                    is AppError.ServerError -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                registerEmailError = error.message
                            )
                        }
                    }
                    else -> {
                        updateState { it.copy(isLoading = false) }
                    }
                }
            }
        ) {
            val currentState = uiState.value

            // Validación de campos
            if (currentState.registerName.isBlank()) {
                throw AppError.ValidationError(field = "name", message = AppStrings.Auth.nameRequired)
            }

            if (currentState.registerSurname.isBlank()) {
                throw AppError.ValidationError(field = "surname", message = AppStrings.Auth.surnameRequired)
            }

            if (currentState.registerEmail.isBlank()) {
                throw AppError.ValidationError(field = "email", message = AppStrings.Auth.emailRequired)
            } else if (!isValidEmail(currentState.registerEmail)) {
                throw AppError.ValidationError(field = "email", message = AppStrings.Auth.invalidEmail)
            }

            if (currentState.registerPassword.isBlank()) {
                throw AppError.ValidationError(field = "password", message = AppStrings.Auth.passwordRequired)
            } else if (currentState.registerPassword.length < 6) {
                throw AppError.ValidationError(field = "password", message = AppStrings.Auth.passwordTooShort)
            }

            if (currentState.registerConfirmPassword != currentState.registerPassword) {
                throw AppError.ValidationError(field = "confirmPassword", message = AppStrings.Auth.passwordsDontMatch)
            }

            // Iniciar carga
            updateState { it.copy(isLoading = true) }

            // Realizar registro y obtener usuario
            val user = userRepository.register(
                currentState.registerName,
                currentState.registerSurname,
                currentState.registerEmail,
                currentState.registerPassword
            )

            // Almacenar usuario en sesión
            sessionManager.setCurrentUser(user)

            // Actualizar estado
            updateState {
                it.copy(
                    isLoading = false,
                    isLoggedIn = true,
                    currentUser = user
                )
            }

            // Navegación a home
            navigateToHome()
        }
    }

    fun loginAsGuest() {
        launchSafe {
            updateState {
                it.copy(
                    isLoading = false,
                    isLoggedIn = false,
                    currentUser = null
                )
            }

            // Limpiar cualquier información existente de sesión
            userRepository.logout()
            sessionManager.logout()

            // Navegar a la pantalla principal
            navigateToHome()
        }
    }

    // Función para validar formato de email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
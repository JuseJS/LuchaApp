package org.iesharia.features.auth.ui.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes

class LoginViewModel(
    private val navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<LoginUiState>(LoginUiState(), errorHandler) {

    private fun navigateToHome() {
        launchSafe {
            navigationManager.navigate(Routes.Home.Main)
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
            handleAppError = { error ->
                // Actualizar UI según el tipo de error
                when (error) {
                    is AppError.ValidationError -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                loginEmailError = (if (error.field == "email") error.message else it.loginEmailError).toString(),
                                loginPasswordError = (if (error.field == "password") error.message else it.loginPasswordError).toString()
                            )
                        }
                    }
                    is AppError.AuthError -> {
                        updateState {
                            it.copy(
                                isLoading = false,
                                loginPasswordError = error.message.toString()
                            )
                        }
                    }
                    else -> {
                        // Para otros tipos de errores, simplemente actualizamos el estado
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

            // Procesamiento de login en hilo de IO
            withContext(Dispatchers.IO) {
                // Aquí iría la lógica real para hacer login con el backend
                delay(1000)

                // Simulamos una falla ocasional (en un entorno real, esto vendría del backend)
                if (currentState.loginEmail == "error@test.com") {
                    throw AppError.AuthError(message = AppStrings.Auth.loginError)
                }
            }

            // Login exitoso - ahora navegamos a la pantalla de inicio
            updateState { it.copy(isLoading = false) }
            navigateToHome()
        }
    }

    // Validación y envío de registro
    fun submitRegister() {
        launchSafe(
            handleAppError = { error ->
                // Actualizar UI según el tipo de error
                when (error) {
                    is AppError.ValidationError -> {
                        // Si es un error específico de un campo, actualizamos ese campo
                        val field = error.field
                        val errorMessage = error.message // Garantizado no-nulo

                        updateState {
                            when (field) {
                                "email" -> it.copy(isLoading = false, registerEmailError = errorMessage.toString())
                                "password" -> it.copy(isLoading = false, registerPasswordError = errorMessage.toString())
                                "name" -> it.copy(isLoading = false, registerNameError = errorMessage.toString())
                                "surname" -> it.copy(isLoading = false, registerSurnameError = errorMessage.toString())
                                "confirmPassword" -> it.copy(isLoading = false, registerConfirmPasswordError = errorMessage.toString())
                                else -> it.copy(
                                    isLoading = false,
                                    registerPasswordError = errorMessage.toString()
                                )
                            }
                        }
                    }
                    is AppError.ServerError -> {
                        // Aseguramos que el mensaje nunca sea nulo
                        val errorMessage = error.message
                        updateState {
                            it.copy(
                                isLoading = false,
                                registerEmailError = errorMessage.toString()
                            )
                        }
                    }
                    else -> {
                        // Para otros tipos de errores, capturamos el mensaje de manera segura
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

            // Procesamiento de registro en hilo de IO
            withContext(Dispatchers.IO) {
                // Simular el registro
                delay(1000)

                // Simulamos una falla ocasional (en un entorno real, esto vendría del backend)
                if (currentState.registerEmail == "error@test.com") {
                    throw AppError.ServerError(message = AppStrings.Auth.registerError)
                }
            }

            // Registro exitoso
            updateState { it.copy(isLoading = false) }
            navigateToHome()
        }
    }

    // Función para validar formato de email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
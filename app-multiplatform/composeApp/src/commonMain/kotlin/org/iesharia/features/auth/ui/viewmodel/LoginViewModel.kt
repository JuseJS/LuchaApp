package org.iesharia.features.auth.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes

class LoginViewModel(
    private val navigationManager: NavigationManager
) : BaseViewModel<LoginUiState>(LoginUiState()) {

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
            handleError = { e ->
                updateState {
                    it.copy(
                        isLoading = false,
                        loginPasswordError = AppStrings.Auth.loginError + e.message
                    )
                }
            }
        ) {
            val currentState = uiState.value

            // Validar campos
            var isValid = true
            var emailError = ""
            var passwordError = ""

            if (currentState.loginEmail.isBlank()) {
                emailError = AppStrings.Auth.emailRequired
                isValid = false
            } else if (!isValidEmail(currentState.loginEmail)) {
                emailError = AppStrings.Auth.invalidEmail
                isValid = false
            }

            if (currentState.loginPassword.isBlank()) {
                passwordError = AppStrings.Auth.passwordRequired
                isValid = false
            }

            // Actualizar errores si hay
            if (!isValid) {
                updateState {
                    it.copy(
                        loginEmailError = emailError,
                        loginPasswordError = passwordError
                    )
                }
                return@launchSafe
            }

            // Iniciar carga
            updateState { it.copy(isLoading = true) }

            // Procesamiento de login en hilo de IO
            withContext(Dispatchers.IO) {
                // Aquí iría la lógica real para hacer login con el backend
                delay(1000)
            }

            // Login exitoso - ahora navegamos a la pantalla de inicio
            updateState { it.copy(isLoading = false) }
            navigateToHome()
        }
    }

    // Validación y envío de registro
    fun submitRegister() {
        launchSafe(
            handleError = { e ->
                updateState {
                    it.copy(
                        isLoading = false,
                        registerPasswordError = AppStrings.Auth.registerError + e.message
                    )
                }
            }
        ) {
            val currentState = uiState.value

            // Validación de campos...
            var isValid = true
            var nameError = ""
            var surnameError = ""
            var emailError = ""
            var passwordError = ""
            var confirmPasswordError = ""

            if (currentState.registerName.isBlank()) {
                nameError = AppStrings.Auth.nameRequired
                isValid = false
            }

            if (currentState.registerSurname.isBlank()) {
                surnameError = AppStrings.Auth.surnameRequired
                isValid = false
            }

            if (currentState.registerEmail.isBlank()) {
                emailError = AppStrings.Auth.emailRequired
                isValid = false
            } else if (!isValidEmail(currentState.registerEmail)) {
                emailError = AppStrings.Auth.invalidEmail
                isValid = false
            }

            if (currentState.registerPassword.isBlank()) {
                passwordError = AppStrings.Auth.passwordRequired
                isValid = false
            } else if (currentState.registerPassword.length < 6) {
                passwordError = AppStrings.Auth.passwordTooShort
                isValid = false
            }

            if (currentState.registerConfirmPassword != currentState.registerPassword) {
                confirmPasswordError = AppStrings.Auth.passwordsDontMatch
                isValid = false
            }

            // Actualizar errores si hay
            if (!isValid) {
                updateState {
                    it.copy(
                        registerNameError = nameError,
                        registerSurnameError = surnameError,
                        registerEmailError = emailError,
                        registerPasswordError = passwordError,
                        registerConfirmPasswordError = confirmPasswordError
                    )
                }
                return@launchSafe
            }

            // Iniciar carga
            updateState { it.copy(isLoading = true) }

            // Procesamiento de registro en hilo de IO
            withContext(Dispatchers.IO) {
                // Simular el registro
                delay(1000)
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
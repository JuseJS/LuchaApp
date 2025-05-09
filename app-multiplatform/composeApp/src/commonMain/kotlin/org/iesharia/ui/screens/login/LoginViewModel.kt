package org.iesharia.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Estado de la pantalla de login
 */
data class LoginUiState(
    // Estado común
    val isLoading: Boolean = false,
    val isLoginMode: Boolean = true, // true = login, false = registro

    // Campos del formulario de login
    val loginEmail: String = "",
    val loginPassword: String = "",
    val loginEmailError: String = "",
    val loginPasswordError: String = "",

    // Campos del formulario de registro
    val registerName: String = "",
    val registerSurname: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerConfirmPassword: String = "",
    val registerNameError: String = "",
    val registerSurnameError: String = "",
    val registerEmailError: String = "",
    val registerPasswordError: String = "",
    val registerConfirmPasswordError: String = ""
)

/**
 * ViewModel para la pantalla de login
 */
class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    // Cambiar entre modos login/registro
    fun toggleAuthMode() {
        _uiState.update { it.copy(isLoginMode = !it.isLoginMode) }
    }

    // Métodos para actualizar campos del login
    fun updateLoginEmail(email: String) {
        _uiState.update { it.copy(loginEmail = email, loginEmailError = "") }
    }

    fun updateLoginPassword(password: String) {
        _uiState.update { it.copy(loginPassword = password, loginPasswordError = "") }
    }

    // Métodos para actualizar campos del registro
    fun updateRegisterName(name: String) {
        _uiState.update { it.copy(registerName = name, registerNameError = "") }
    }

    fun updateRegisterSurname(surname: String) {
        _uiState.update { it.copy(registerSurname = surname, registerSurnameError = "") }
    }

    fun updateRegisterEmail(email: String) {
        _uiState.update { it.copy(registerEmail = email, registerEmailError = "") }
    }

    fun updateRegisterPassword(password: String) {
        _uiState.update {
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
        _uiState.update {
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
        viewModelScope.launch(SupervisorJob() + Dispatchers.Main.immediate) {
            val currentState = _uiState.value

            // Validar campos
            var isValid = true
            var emailError = ""
            var passwordError = ""

            if (currentState.loginEmail.isBlank()) {
                emailError = "El correo electrónico es obligatorio"
                isValid = false
            } else if (!isValidEmail(currentState.loginEmail)) {
                emailError = "Correo electrónico inválido"
                isValid = false
            }

            if (currentState.loginPassword.isBlank()) {
                passwordError = "La contraseña es obligatoria"
                isValid = false
            }

            // Actualizar errores si hay
            if (!isValid) {
                _uiState.update {
                    it.copy(
                        loginEmailError = emailError,
                        loginPasswordError = passwordError
                    )
                }
                return@launch
            }

            try {
                // Iniciar carga
                _uiState.update { it.copy(isLoading = true) }

                // Procesamiento de login en hilo de IO
                withContext(Dispatchers.IO) {
                    // Aquí iría la lógica real para hacer login con el backend
                    // Por ahora, simulamos un delay
                    delay(1000)
                }

                // Login exitoso - aquí se manejaría la navegación a la siguiente pantalla
                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                // Manejo de error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        loginPasswordError = "Error al iniciar sesión: ${e.message}"
                    )
                }
            }
        }
    }

    // Validación y envío de registro
    fun submitRegister() {
        viewModelScope.launch(SupervisorJob() + Dispatchers.Main.immediate) {
            val currentState = _uiState.value

            // Validar campos
            var isValid = true
            var nameError = ""
            var surnameError = ""
            var emailError = ""
            var passwordError = ""
            var confirmPasswordError = ""

            if (currentState.registerName.isBlank()) {
                nameError = "El nombre es obligatorio"
                isValid = false
            }

            if (currentState.registerSurname.isBlank()) {
                surnameError = "Los apellidos son obligatorios"
                isValid = false
            }

            if (currentState.registerEmail.isBlank()) {
                emailError = "El correo electrónico es obligatorio"
                isValid = false
            } else if (!isValidEmail(currentState.registerEmail)) {
                emailError = "Correo electrónico inválido"
                isValid = false
            }

            if (currentState.registerPassword.isBlank()) {
                passwordError = "La contraseña es obligatoria"
                isValid = false
            } else if (currentState.registerPassword.length < 6) {
                passwordError = "La contraseña debe tener al menos 6 caracteres"
                isValid = false
            }

            if (currentState.registerConfirmPassword != currentState.registerPassword) {
                confirmPasswordError = "Las contraseñas no coinciden"
                isValid = false
            }

            // Actualizar errores si hay
            if (!isValid) {
                _uiState.update {
                    it.copy(
                        registerNameError = nameError,
                        registerSurnameError = surnameError,
                        registerEmailError = emailError,
                        registerPasswordError = passwordError,
                        registerConfirmPasswordError = confirmPasswordError
                    )
                }
                return@launch
            }

            try {
                // Iniciar carga
                _uiState.update { it.copy(isLoading = true) }

                // Procesamiento de registro en hilo de IO
                withContext(Dispatchers.IO) {
                    // Aquí iría la lógica real para hacer registro con el backend
                    // Por ahora, simulamos un delay
                    delay(1000)
                }

                // Registro exitoso - aquí se manejaría la navegación a la siguiente pantalla
                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                // Manejo de error
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        registerPasswordError = "Error al registrarse: ${e.message}"
                    )
                }
            }
        }
    }

    // Función mejorada para validar formato de email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }
}
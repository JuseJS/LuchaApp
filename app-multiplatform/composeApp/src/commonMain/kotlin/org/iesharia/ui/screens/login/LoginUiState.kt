package org.iesharia.ui.screens.login

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
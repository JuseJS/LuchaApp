package org.iesharia.features.auth.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.form.FormDefinition
import org.iesharia.core.ui.form.FormDefinition.ValidationResult

/**
 * Definiciones de formularios de autenticación
 */
object AuthForms {
    // Claves para campos de formulario
    const val KEY_EMAIL = "email"
    const val KEY_PASSWORD = "password"
    const val KEY_NAME = "name"
    const val KEY_SURNAME = "surname"
    const val KEY_CONFIRM_PASSWORD = "confirmPassword"

    // Validadores comunes
    private val emailValidator: (String, Map<String, String>) -> ValidationResult = { email, _ ->
        when {
            email.isBlank() -> ValidationResult.Invalid(AppStrings.Auth.emailRequired)
            !isValidEmail(email) -> ValidationResult.Invalid(AppStrings.Auth.invalidEmail)
            else -> ValidationResult.Valid
        }
    }

    private val passwordValidator: (String, Map<String, String>) -> ValidationResult = { password, _ ->
        when {
            password.isBlank() -> ValidationResult.Invalid(AppStrings.Auth.passwordRequired)
            password.length < 6 -> ValidationResult.Invalid(AppStrings.Auth.passwordTooShort)
            else -> ValidationResult.Valid
        }
    }

    private val confirmPasswordValidator: (String, Map<String, String>) -> ValidationResult = { confirmPassword, allValues ->
        val password = allValues[KEY_PASSWORD] ?: ""
        when {
            confirmPassword.isBlank() -> ValidationResult.Invalid(AppStrings.Auth.passwordRequired)
            confirmPassword != password -> ValidationResult.Invalid(AppStrings.Auth.passwordsDontMatch)
            else -> ValidationResult.Valid
        }
    }

    private val nameValidator: (String, Map<String, String>) -> ValidationResult = { name, _ ->
        if (name.isBlank()) ValidationResult.Invalid(AppStrings.Auth.nameRequired) else ValidationResult.Valid
    }

    private val surnameValidator: (String, Map<String, String>) -> ValidationResult = { surname, _ ->
        if (surname.isBlank()) ValidationResult.Invalid(AppStrings.Auth.surnameRequired) else ValidationResult.Valid
    }

    // Definición del formulario de login
    val loginForm = FormDefinition(
        id = "login_form",
        title = AppStrings.Auth.login,
        fields = listOf(
            FormDefinition.FieldDefinition(
                key = KEY_EMAIL,
                label = AppStrings.Auth.email,
                icon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                validator = emailValidator
            ),
            FormDefinition.FieldDefinition(
                key = KEY_PASSWORD,
                label = AppStrings.Auth.password,
                icon = Icons.Filled.Lock,
                isPassword = true,
                validator = passwordValidator
            )
        ),
        submitButton = FormDefinition.SubmitButtonConfig(
            text = AppStrings.Auth.login,
            type = WrestlingButtonType.PRIMARY
        )
    )

    // Definición del formulario de registro
    val registerForm = FormDefinition(
        id = "register_form",
        title = AppStrings.Auth.createAccount,
        fields = listOf(
            FormDefinition.FieldDefinition(
                key = KEY_NAME,
                label = AppStrings.Auth.name,
                icon = Icons.Filled.Person,
                validator = nameValidator
            ),
            FormDefinition.FieldDefinition(
                key = KEY_SURNAME,
                label = AppStrings.Auth.surname,
                icon = Icons.Filled.Person,
                validator = surnameValidator
            ),
            FormDefinition.FieldDefinition(
                key = KEY_EMAIL,
                label = AppStrings.Auth.email,
                icon = Icons.Filled.Email,
                keyboardType = KeyboardType.Email,
                validator = emailValidator
            ),
            FormDefinition.FieldDefinition(
                key = KEY_PASSWORD,
                label = AppStrings.Auth.password,
                icon = Icons.Filled.Lock,
                isPassword = true,
                validator = passwordValidator
            ),
            FormDefinition.FieldDefinition(
                key = KEY_CONFIRM_PASSWORD,
                label = AppStrings.Auth.confirmPassword,
                icon = Icons.Filled.Lock,
                isPassword = true,
                validator = confirmPasswordValidator
            )
        ),
        submitButton = FormDefinition.SubmitButtonConfig(
            text = AppStrings.Auth.register,
            type = WrestlingButtonType.SECONDARY
        )
    )

    // Función auxiliar para validar email
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
        return email.matches(emailRegex)
    }
}
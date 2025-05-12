package org.iesharia.ui.components.login

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.ui.components.LuchaButton
import org.iesharia.ui.components.LuchaButtonType

@Composable
fun RegisterForm(
    name: String,
    surname: String,
    email: String,
    password: String,
    confirmPassword: String,
    nameError: String,
    surnameError: String,
    emailError: String,
    passwordError: String,
    confirmPasswordError: String,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    val fields = listOf(
        FormField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre",
            error = nameError,
            leadingIcon = Icons.Filled.Person
        ),
        FormField(
            value = surname,
            onValueChange = onSurnameChange,
            label = "Apellidos",
            error = surnameError,
            leadingIcon = Icons.Filled.Person
        ),
        FormField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            error = emailError,
            leadingIcon = Icons.Filled.Email,
            keyboardType = KeyboardType.Email
        ),
        FormField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            error = passwordError,
            leadingIcon = Icons.Filled.Lock,
            isPassword = true
        ),
        FormField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña",
            error = confirmPasswordError,
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            imeAction = ImeAction.Done
        )
    )

    AuthForm(
        title = "Crear Cuenta",
        fields = fields,
        onSubmit = onSubmit,
        submitButtonText = "Registrarse",
        isLoading = isLoading,
        renderButton = { text, onClick, enabled ->
            LuchaButton(
                text = text,
                onClick = onClick,
                type = LuchaButtonType.SECONDARY,
                enabled = enabled
            )
        }
    )
}
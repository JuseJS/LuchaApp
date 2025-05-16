package org.iesharia.features.auth.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.LuchaButton
import org.iesharia.core.ui.components.LuchaButtonType

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
            label = AppStrings.Auth.name,
            error = nameError,
            leadingIcon = Icons.Filled.Person
        ),
        FormField(
            value = surname,
            onValueChange = onSurnameChange,
            label = AppStrings.Auth.surname,
            error = surnameError,
            leadingIcon = Icons.Filled.Person
        ),
        FormField(
            value = email,
            onValueChange = onEmailChange,
            label = AppStrings.Auth.email,
            error = emailError,
            leadingIcon = Icons.Filled.Email,
            keyboardType = KeyboardType.Email
        ),
        FormField(
            value = password,
            onValueChange = onPasswordChange,
            label = AppStrings.Auth.password,
            error = passwordError,
            leadingIcon = Icons.Filled.Lock,
            isPassword = true
        ),
        FormField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = AppStrings.Auth.confirmPassword,
            error = confirmPasswordError,
            leadingIcon = Icons.Filled.Lock,
            isPassword = true,
            imeAction = ImeAction.Done
        )
    )

    AuthForm(
        title = AppStrings.Auth.createAccount,
        fields = fields,
        onSubmit = onSubmit,
        submitButtonText = AppStrings.Auth.register,
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
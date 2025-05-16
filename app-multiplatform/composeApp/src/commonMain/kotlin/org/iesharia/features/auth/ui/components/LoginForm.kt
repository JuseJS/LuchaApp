package org.iesharia.features.auth.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.LuchaButton
import org.iesharia.core.ui.components.LuchaButtonType

@Composable
fun LoginForm(
    email: String,
    password: String,
    emailError: String,
    passwordError: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    val fields = listOf(
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
            isPassword = true,
            imeAction = ImeAction.Done
        )
    )

    AuthForm(
        title = AppStrings.Auth.login,
        fields = fields,
        onSubmit = onSubmit,
        submitButtonText = AppStrings.Auth.login,
        isLoading = isLoading,
        renderButton = { text, onClick, enabled ->
            LuchaButton(
                text = text,
                onClick = onClick,
                type = LuchaButtonType.PRIMARY,
                enabled = enabled
            )
        }
    )
}
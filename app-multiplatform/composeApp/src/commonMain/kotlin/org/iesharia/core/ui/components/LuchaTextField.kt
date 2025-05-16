package org.iesharia.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.core.resources.AppStrings

/**
 * Campo de texto personalizado para la aplicación de Lucha Canaria
 */
@Composable
fun LuchaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    isError: Boolean = false,
    errorMessage: String = "",
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    val focusManager = LocalFocusManager.current
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = LuchaTheme.dimensions.spacing_16),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = if (leadingIcon != null) {
            { Icon(leadingIcon, contentDescription = null) }
        } else null,
        trailingIcon = if (isPassword) {
            {
                PasswordVisibilityToggle(
                    passwordVisible = passwordVisible,
                    onTogglePasswordVisibility = { passwordVisible = !passwordVisible }
                )
            }
        } else if (trailingIcon != null && onTrailingIconClick != null) {
            {
                IconButton(onClick = onTrailingIconClick) {
                    Icon(trailingIcon, contentDescription = null)
                }
            }
        } else null,
        isError = isError,
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { focusManager.clearFocus() }
        ),
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        shape = LuchaTheme.shapes.inputFieldShape,
        supportingText = if (isError && errorMessage.isNotEmpty()) {
            { Text(errorMessage) }
        } else null
    )
}

/**
 * Toggle para mostrar/ocultar contraseña
 */
@Composable
private fun PasswordVisibilityToggle(
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit
) {
    val icon = if (passwordVisible) {
        Icons.Outlined.VisibilityOff
    } else {
        Icons.Outlined.Visibility
    }

    IconButton(onClick = onTogglePasswordVisibility) {
        Icon(
            imageVector = icon,
            contentDescription = if (passwordVisible)
                AppStrings.Auth.hidePassword
            else
                AppStrings.Auth.showPassword
        )
    }
}
package org.iesharia.features.auth.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.core.ui.components.LuchaTextField

/**
 * Estructura para representar un campo de formulario
 */
data class FormField(
    val value: String,
    val onValueChange: (String) -> Unit,
    val label: String,
    val error: String = "",
    val leadingIcon: ImageVector? = null,
    val isPassword: Boolean = false,
    val keyboardType: KeyboardType = KeyboardType.Text,
    val imeAction: ImeAction = ImeAction.Next
)

/**
 * Renderiza un campo de formulario
 */
@Composable
fun AuthFormField(field: FormField) {
    LuchaTextField(
        value = field.value,
        onValueChange = field.onValueChange,
        label = field.label,
        keyboardType = field.keyboardType,
        isError = field.error.isNotEmpty(),
        errorMessage = field.error,
        leadingIcon = field.leadingIcon,
        isPassword = field.isPassword,
        imeAction = field.imeAction
    )
}
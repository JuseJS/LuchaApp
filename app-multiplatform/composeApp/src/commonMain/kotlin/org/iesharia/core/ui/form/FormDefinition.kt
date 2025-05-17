package org.iesharia.core.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import org.iesharia.core.ui.components.LuchaButtonType

/**
 * Definición completa de un formulario
 */
@Stable
data class FormDefinition(
    val id: String,
    val title: String,
    val fields: List<FieldDefinition>,
    val submitButton: SubmitButtonConfig
) {
    @Stable
    data class FieldDefinition(
        val key: String,
        val label: String,
        val icon: ImageVector? = null,
        val keyboardType: KeyboardType = KeyboardType.Text,
        val isPassword: Boolean = false,
        val imeAction: ImeAction = ImeAction.Next,
        /**
         * Validador que recibe el valor actual y todos los valores del formulario
         * para permitir validación cruzada
         */
        val validator: (value: String, allValues: Map<String, String>) -> ValidationResult = { _, _ -> ValidationResult.Valid }
    )

    @Stable
    data class SubmitButtonConfig(
        val text: String,
        val type: LuchaButtonType
    )

    sealed class ValidationResult {
        object Valid : ValidationResult()
        data class Invalid(val errorMessage: String) : ValidationResult()
    }
}

/**
 * Estado del formulario
 */
@Stable
data class FormState(
    val formId: String,
    val values: Map<String, String>,
    val errors: Map<String, String>,
    val isSubmitting: Boolean
) {
    /**
     * Verifica si el formulario es válido
     */
    val isValid: Boolean
        get() = errors.isEmpty() && values.isNotEmpty()

    /**
     * Obtiene el valor de un campo
     */
    fun getValue(key: String): String = values[key] ?: ""

    /**
     * Obtiene el error de un campo
     */
    fun getError(key: String): String = errors[key] ?: ""

    /**
     * Verifica si un campo tiene error
     */
    fun hasError(key: String): Boolean = errors.containsKey(key) && errors[key]?.isNotEmpty() ?: false
}
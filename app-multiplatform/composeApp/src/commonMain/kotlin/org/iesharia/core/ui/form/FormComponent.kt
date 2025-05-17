package org.iesharia.core.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingTextField
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente universal de formulario que se adapta a cualquier definición
 */
@Composable
fun FormComponent(
    formDefinition: FormDefinition,
    initialValues: Map<String, String> = emptyMap(),
    externalErrors: Map<String, String> = emptyMap(),
    isSubmitting: Boolean = false,
    onValueChange: (String, String) -> Unit = { _, _ -> },
    onSubmit: (Map<String, String>) -> Unit
) {
    // Estados locales del formulario
    val values = remember(formDefinition.id) {
        mutableStateMapOf<String, String>().apply {
            putAll(initialValues)
            // Asegurar que todos los campos tengan un valor inicial
            formDefinition.fields.forEach { field ->
                if (!containsKey(field.key)) {
                    put(field.key, "")
                }
            }
        }
    }

    val errors = remember(formDefinition.id) { mutableStateMapOf<String, String>() }
    var wasValidated by remember(formDefinition.id) { mutableStateOf(false) }

    // Procesar errores externos
    LaunchedEffect(externalErrors) {
        errors.clear()
        errors.putAll(externalErrors)
    }

    // Estado derivado para el formulario completo
    val formState by remember {
        derivedStateOf {
            FormState(
                formId = formDefinition.id,
                values = values.toMap(),
                errors = errors.toMap(),
                isSubmitting = isSubmitting
            )
        }
    }

    // Validar todos los campos
    fun validateAll(): Boolean {
        errors.clear()

        formDefinition.fields.forEach { field ->
            val value = values[field.key] ?: ""
            val validationResult = field.validator(value, values)

            if (validationResult is FormDefinition.ValidationResult.Invalid) {
                errors[field.key] = validationResult.errorMessage
            }
        }

        wasValidated = true
        return errors.isEmpty()
    }

    // Validar un campo individual
    fun validateField(key: String) {
        if (!wasValidated) return // Solo validar si el formulario ya se intentó enviar

        val field = formDefinition.fields.find { it.key == key } ?: return
        val value = values[key] ?: ""
        val validationResult = field.validator(value, values)

        if (validationResult is FormDefinition.ValidationResult.Invalid) {
            errors[key] = validationResult.errorMessage
        } else {
            errors.remove(key)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título del formulario
        Text(
            text = formDefinition.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_24)
        )

        // Renderizar campos dinámicamente
        formDefinition.fields.forEachIndexed { index, field ->
            val value = values[field.key] ?: ""
            val error = errors[field.key] ?: ""
            val isLastField = index == formDefinition.fields.size - 1

            WrestlingTextField(
                value = value,
                onValueChange = { newValue ->
                    values[field.key] = newValue
                    validateField(field.key)
                    onValueChange(field.key, newValue)
                },
                label = field.label,
                leadingIcon = field.icon,
                isPassword = field.isPassword,
                keyboardType = field.keyboardType,
                imeAction = if (isLastField) ImeAction.Done else field.imeAction,
                isError = error.isNotEmpty(),
                errorMessage = error,
                enabled = !isSubmitting
            )
        }

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Botón de envío
        WrestlingButton(
            text = formDefinition.submitButton.text,
            onClick = {
                if (validateAll()) {
                    onSubmit(values)
                }
            },
            type = formDefinition.submitButton.type,
            enabled = !isSubmitting
        )
    }
}
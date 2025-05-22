package org.iesharia.core.ui.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.WrestlingTextField

/**
 * Campo dropdown reutilizable con exposición de menú
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String = "",
    error: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it && enabled && !readOnly },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = if (readOnly) { _ -> } else onValueChange,
            readOnly = readOnly,
            enabled = enabled,
            label = { Text(label) },
            placeholder = if (placeholder.isNotEmpty()) {
                { Text(placeholder) }
            } else null,
            isError = error.isNotEmpty(),
            supportingText = if (error.isNotEmpty()) {
                { Text(error) }
            } else null,
            trailingIcon = if (!readOnly && enabled) {
                { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
            } else null,
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = enabled && !readOnly)
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        if (options.isNotEmpty() && !readOnly) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        modifier = Modifier.clickable {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * Campo de tiempo con validación automática
 */
@Composable
fun TimeField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: String = ""
) {
    var internalValue by remember(value) { mutableStateOf(value) }

    fun validateTime(input: String): String {
        if (input.isEmpty()) return ""

        val cleanInput = input.replace(Regex("[^0-9:]"), "")

        if (":" in cleanInput) {
            val parts = cleanInput.split(":")
            if (parts.size == 2) {
                val hours = parts[0].toIntOrNull()?.coerceIn(0, 23)
                val minutes = parts[1].toIntOrNull()?.coerceIn(0, 59)

                if (hours != null && minutes != null) {
                    return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                }
            }
        } else if (cleanInput.all { it.isDigit() } && cleanInput.length <= 4) {
            when (cleanInput.length) {
                1, 2 -> {
                    val hours = cleanInput.toIntOrNull()?.coerceIn(0, 23)
                    if (hours != null) {
                        return "${hours.toString().padStart(2, '0')}:00"
                    }
                }
                3 -> {
                    val hours = cleanInput.substring(0, 1).toIntOrNull()?.coerceIn(0, 23)
                    val minutes = cleanInput.substring(1, 3).toIntOrNull()?.coerceIn(0, 59)
                    if (hours != null && minutes != null) {
                        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                    }
                }
                4 -> {
                    val hours = cleanInput.substring(0, 2).toIntOrNull()?.coerceIn(0, 23)
                    val minutes = cleanInput.substring(2, 4).toIntOrNull()?.coerceIn(0, 59)
                    if (hours != null && minutes != null) {
                        return "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}"
                    }
                }
            }
        }

        return cleanInput
    }

    WrestlingTextField(
        value = internalValue,
        onValueChange = { newValue ->
            val cleaned = newValue.replace(Regex("[^0-9:]"), "")
            if (cleaned.length <= 5) {
                internalValue = cleaned
            }
        },
        label = label,
        trailingIcon = Icons.Default.Schedule,
        keyboardType = KeyboardType.Number,
        singleLine = true,
        enabled = enabled,
        isError = error.isNotEmpty(),
        errorMessage = error,
        placeholder = "HH:MM",
        modifier = modifier
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    val validated = validateTime(internalValue)
                    internalValue = validated
                    onValueChange(validated)
                }
            }
    )
}

/**
 * Campo numérico con validación
 */
@Composable
fun NumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxDigits: Int = Int.MAX_VALUE,
    allowDecimals: Boolean = false,
    error: String = "",
    placeholder: String = ""
) {
    WrestlingTextField(
        value = value,
        onValueChange = { newValue ->
            val regex = if (allowDecimals) {
                Regex("[^0-9.]")
            } else {
                Regex("[^0-9]")
            }

            val cleaned = newValue.replace(regex, "")

            // Validar número de dígitos
            val digitsOnly = cleaned.replace(".", "")
            if (digitsOnly.length <= maxDigits) {
                // Validar que solo haya un punto decimal
                if (!allowDecimals || cleaned.count { it == '.' } <= 1) {
                    onValueChange(cleaned)
                }
            }
        },
        label = label,
        keyboardType = if (allowDecimals) KeyboardType.Decimal else KeyboardType.Number,
        singleLine = true,
        enabled = enabled,
        isError = error.isNotEmpty(),
        errorMessage = error,
        placeholder = placeholder,
        modifier = modifier
    )
}

/**
 * Campo de texto con validación personalizada
 */
@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    error: String = "",
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    validator: ((String) -> String)? = null
) {
    var internalError by remember { mutableStateOf(error) }

    WrestlingTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
            // Aplicar validación si existe
            validator?.let { validate ->
                internalError = validate(newValue)
            }
        },
        label = label,
        keyboardType = keyboardType,
        singleLine = singleLine,
        maxLines = maxLines,
        enabled = enabled,
        isError = internalError.isNotEmpty() || error.isNotEmpty(),
        errorMessage = internalError.ifEmpty { error },
        placeholder = placeholder,
        modifier = modifier
    )
}

/**
 * Campo de búsqueda reutilizable
 */
@Composable
fun SearchField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "Buscar",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholder: String = "Escribe para buscar...",
    onSearch: ((String) -> Unit)? = null
) {
    WrestlingTextField(
        value = value,
        onValueChange = { newValue ->
            onValueChange(newValue)
            onSearch?.invoke(newValue)
        },
        label = label,
        singleLine = true,
        enabled = enabled,
        placeholder = placeholder,
        leadingIcon = Icons.Default.Schedule, // Cambiar por icono de búsqueda cuando esté disponible
        modifier = modifier
    )
}

/**
 * Campo de comentarios multi-línea
 */
@Composable
fun CommentField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    maxLines: Int = 6,
    placeholder: String = "",
    error: String = ""
) {
    WrestlingTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        singleLine = false,
        maxLines = maxLines,
        enabled = enabled,
        isError = error.isNotEmpty(),
        errorMessage = error,
        placeholder = placeholder,
        modifier = modifier
    )
}

/**
 * Selector de opciones con chips
 */
@Composable
fun <T> ChipSelector(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T?) -> Unit,
    optionToString: (T) -> String,
    modifier: Modifier = Modifier,
    allowDeselection: Boolean = true,
    title: String? = null
) {
    Column(modifier = modifier) {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            options.forEach { option ->
                FilterChip(
                    selected = option == selectedOption,
                    onClick = {
                        if (option == selectedOption && allowDeselection) {
                            onOptionSelected(null)
                        } else {
                            onOptionSelected(option)
                        }
                    },
                    label = { Text(optionToString(option)) }
                )
            }
        }
    }
}
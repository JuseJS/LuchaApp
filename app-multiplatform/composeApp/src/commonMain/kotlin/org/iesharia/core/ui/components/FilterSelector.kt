package org.iesharia.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente genérico para selección de filtros en diálogos.
 * Este componente es reutilizable para diferentes tipos de filtros:
 * - Categorías de edad
 * - Categorías de división
 * - Islas
 * - Cualquier otro tipo de selección mediante chips
 *
 * @param T El tipo de opción para filtrar
 * @param title Título para la sección de filtro
 * @param options Lista de opciones disponibles del tipo T
 * @param selectedOption La opción actualmente seleccionada, o null si ninguna está seleccionada
 * @param optionToString Función para convertir una opción en su representación de texto
 * @param onOptionSelected Callback invocado cuando el usuario selecciona una opción
 * @param allowNullSelection Si es true, permite deseleccionar una opción
 * @param modifier Modificador para aplicar al componente
 * @param allOptionLabel Etiqueta para la opción "todos" cuando allowNullSelection es true
 */
@Composable
fun <T> FilterSelector(
    title: String,
    options: List<T>,
    selectedOption: T?,
    optionToString: (T) -> String,
    onOptionSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    allowNullSelection: Boolean = true,
    allOptionLabel: String = AppStrings.Home.all
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(WrestlingTheme.dimensions.spacing_8)
        ) {
            // Opción para seleccionar "Todos" (ningún filtro)
            if (allowNullSelection) {
                FilterChip(
                    selected = selectedOption == null,
                    onClick = { onOptionSelected(null) },
                    label = { Text(allOptionLabel) }
                )
            }
            
            // Opciones disponibles para este filtro
            options.forEach { option ->
                FilterChip(
                    selected = option == selectedOption,
                    onClick = { 
                        // Si la opción ya está seleccionada y se permite deselección,
                        // entonces deseleccionar (volver a null)
                        if (option == selectedOption && allowNullSelection) {
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
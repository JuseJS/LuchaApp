package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Barra de búsqueda reutilizable con funcionalidad de limpieza
 *
 * @param query Texto actual de búsqueda
 * @param onQueryChange Callback invocado cuando cambia el texto de búsqueda
 * @param modifier Modificador para personalizar el componente
 * @param placeholder Texto mostrado cuando el campo está vacío
 * @param textStyle Estilo del texto de búsqueda
 * @param shape Forma del campo de búsqueda
 * @param backgroundColor Color de fondo del campo
 * @param contentColor Color del texto y los iconos
 * @param clearIcon Icono para limpiar la búsqueda (por defecto Clear)
 * @param searchIcon Icono para la búsqueda (por defecto Search)
 * @param onSearch Callback opcional invocado cuando el usuario termina de escribir/presiona buscar
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar...",
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = RoundedCornerShape(WrestlingTheme.dimensions.corner_radius_medium),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    clearIcon: @Composable (() -> Unit)? = null,
    searchIcon: @Composable (() -> Unit)? = null,
    onSearch: ((String) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && onSearch != null) {
                    onSearch(query)
                }
            },
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle,
                color = contentColor.copy(alpha = 0.6f)
            )
        },
        textStyle = textStyle.copy(color = contentColor),
        singleLine = true,
        leadingIcon = searchIcon ?: {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = contentColor.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                clearIcon ?: IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = contentColor.copy(alpha = 0.7f)
                    )
                }
            }
        },
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            cursorColor = contentColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = contentColor.copy(alpha = 0.2f)
        )
    )
}

/**
 * Versión de SearchBar que acepta TextFieldValue para casos donde se necesita
 * mayor control del cursor y selección.
 */
@Composable
fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Buscar...",
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    shape: Shape = RoundedCornerShape(WrestlingTheme.dimensions.corner_radius_medium),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    clearIcon: @Composable (() -> Unit)? = null,
    searchIcon: @Composable (() -> Unit)? = null,
    onSearch: ((TextFieldValue) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused && onSearch != null) {
                    onSearch(query)
                }
            },
        placeholder = {
            Text(
                text = placeholder,
                style = textStyle,
                color = contentColor.copy(alpha = 0.6f)
            )
        },
        textStyle = textStyle.copy(color = contentColor),
        singleLine = true,
        leadingIcon = searchIcon ?: {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = contentColor.copy(alpha = 0.7f)
            )
        },
        trailingIcon = {
            if (query.text.isNotEmpty()) {
                clearIcon ?: IconButton(onClick = { onQueryChange(TextFieldValue("")) }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpiar",
                        tint = contentColor.copy(alpha = 0.7f)
                    )
                }
            }
        },
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,
            unfocusedContainerColor = backgroundColor,
            disabledContainerColor = backgroundColor,
            cursorColor = contentColor,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = contentColor.copy(alpha = 0.2f)
        )
    )
}
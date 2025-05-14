package org.iesharia.features.auth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.iesharia.core.ui.theme.LuchaTheme

/**
 * Componente base para formularios de autenticación
 */
@Composable
fun AuthForm(
    title: String,
    fields: List<FormField>,
    onSubmit: () -> Unit,
    submitButtonText: String,
    isLoading: Boolean,
    renderButton: @Composable (text: String, onClick: () -> Unit, enabled: Boolean) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = LuchaTheme.dimensions.spacing_24)
        )

        // Renderizar cada campo del formulario
        fields.forEach { field ->
            AuthFormField(field = field)
        }

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

        // Botón de acción
        renderButton(
            submitButtonText,
            onSubmit,
            !isLoading
        )
    }
}
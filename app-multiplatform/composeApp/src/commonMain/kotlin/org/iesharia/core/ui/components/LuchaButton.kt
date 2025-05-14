package org.iesharia.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.ui.theme.LuchaTheme

/**
 * Tipos de botones disponibles en la aplicación
 */
enum class LuchaButtonType {
    PRIMARY,
    SECONDARY,
    TEXT
}

/**
 * Botón universal personalizado para la aplicación de Lucha Canaria
 */
@Composable
fun LuchaButton(
    text: String,
    onClick: () -> Unit,
    type: LuchaButtonType = LuchaButtonType.PRIMARY,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    customColor: Color? = null // Solo usado para botones de tipo TEXT
) {
    when (type) {
        LuchaButtonType.PRIMARY, LuchaButtonType.SECONDARY -> {
            val colors = when (type) {
                LuchaButtonType.PRIMARY -> ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LuchaButtonType.SECONDARY -> ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> ButtonDefaults.buttonColors() // Nunca llegará aquí debido al when
            }

            Button(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .height(LuchaTheme.dimensions.button_height)
                    .padding(vertical = LuchaTheme.dimensions.spacing_8),
                enabled = enabled,
                shape = LuchaTheme.shapes.buttonShape,
                colors = colors
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
        LuchaButtonType.TEXT -> {
            val color = customColor ?: MaterialTheme.colorScheme.primary
            TextButton(
                onClick = onClick,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = LuchaTheme.dimensions.spacing_4),
                enabled = enabled
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (enabled) color else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Tipos de divisores de sección disponibles
 */
enum class SectionDividerType {
    /**
     * Divisor principal con líneas a ambos lados del texto (estilo original)
     */
    PRIMARY,

    /**
     * Título grande sin líneas
     */
    TITLE,

    /**
     * Subtítulo mediano sin líneas, incluye espaciado inferior
     */
    SUBTITLE
}

/**
 * Componente unificado para cabeceras y divisores de sección
 *
 * @param title Texto de la sección
 * @param modifier Modificador para personalizar el componente
 * @param type Tipo de divisor que determina el estilo predefinido
 * @param showDividerLines Sí se muestran las líneas decorativas (solo aplicable en ciertos tipos)
 * @param leftLineWeight Peso de la línea izquierda (solo si showDividerLines es true)
 * @param rightLineWeight Peso de la línea derecha (solo si showDividerLines es true)
 * @param lineThickness Grosor de las líneas (solo si showDividerLines es true)
 * @param leftLineColor Color de la línea izquierda (solo si showDividerLines es true)
 * @param rightLineColor Color de la línea derecha (solo si showDividerLines es true)
 * @param textStyle Estilo de texto personalizado (anula el estilo predefinido por tipo)
 * @param textColor Color de texto personalizado (anula el color predefinido por tipo)
 */
@Composable
fun SectionDivider(
    title: String,
    modifier: Modifier = Modifier,
    type: SectionDividerType = SectionDividerType.PRIMARY,
    showDividerLines: Boolean = when(type) {
        SectionDividerType.PRIMARY -> true
        else -> false
    },
    leftLineWeight: Float = 0.1f,
    rightLineWeight: Float = 1f,
    lineThickness: Dp = 3.dp,
    leftLineColor: Color = MaterialTheme.colorScheme.primary,
    rightLineColor: Color = MaterialTheme.colorScheme.outlineVariant,
    textStyle: TextStyle? = null,
    textColor: Color? = null
) {
    // Determinar valores basados en el tipo
    val effectiveTextStyle = textStyle ?: when(type) {
        SectionDividerType.PRIMARY, SectionDividerType.TITLE -> MaterialTheme.typography.titleLarge
        SectionDividerType.SUBTITLE -> MaterialTheme.typography.titleMedium
    }

    val effectiveTextColor = textColor ?: when(type) {
        SectionDividerType.PRIMARY, SectionDividerType.TITLE -> MaterialTheme.colorScheme.onBackground
        SectionDividerType.SUBTITLE -> MaterialTheme.colorScheme.secondary
    }

    val paddingHorizontal = if (!showDividerLines) {
        WrestlingTheme.dimensions.spacing_16
    } else {
        WrestlingTheme.dimensions.spacing_8
    }

    Column(modifier = modifier) {
        if (showDividerLines) {
            // Versión con líneas divisoras
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(leftLineWeight),
                    thickness = lineThickness,
                    color = leftLineColor
                )

                Text(
                    text = title,
                    style = effectiveTextStyle,
                    fontWeight = FontWeight.Bold,
                    color = effectiveTextColor,
                    modifier = Modifier.padding(horizontal = paddingHorizontal)
                )

                HorizontalDivider(
                    modifier = Modifier.weight(rightLineWeight),
                    color = rightLineColor
                )
            }
        } else {
            // Versión sin líneas divisoras (solo texto)
            Text(
                text = title,
                style = effectiveTextStyle,
                fontWeight = FontWeight.Bold,
                color = effectiveTextColor,
                modifier = Modifier.padding(horizontal = paddingHorizontal)
            )
        }

        // Añadir espaciado automático después de subtítulos
        if (type == SectionDividerType.SUBTITLE) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
        }
    }
}
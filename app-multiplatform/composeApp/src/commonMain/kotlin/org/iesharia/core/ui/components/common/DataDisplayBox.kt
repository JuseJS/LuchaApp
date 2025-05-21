package org.iesharia.core.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Componente base reutilizable para mostrar datos estadísticos o indicadores numéricos
 * con una etiqueta descriptiva. Este componente unifica la funcionalidad de StatisticBox
 * y CategoryCountBox, reduciendo la duplicación de código.
 *
 * @param label Texto descriptivo que se muestra debajo del valor principal
 * @param value Valor principal a mostrar (típicamente un número, porcentaje, etc.)
 * @param color Color principal usado para el valor
 * @param backgroundColor Color de fondo (por defecto usa una versión con transparencia del color principal)
 * @param valueStyle Estilo de texto para el valor principal
 * @param labelStyle Estilo de texto para la etiqueta
 * @param labelColor Color del texto de la etiqueta (por defecto usa onSurfaceVariant)
 * @param modifier Modificador para aplicar al componente
 * @param valueFontWeight Grosor de fuente para el valor principal
 * @param padding Espaciado interno del componente
 * @param textAlign Alineación del texto para el valor y la etiqueta
 * @param useCard Indica si se debe usar un Card para envolver el contenido
 */
@Composable
fun DataDisplayBox(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier,
    backgroundColor: Color = color.copy(alpha = 0.1f),
    valueStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    labelStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    labelColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    valueFontWeight: FontWeight = FontWeight.Bold,
    padding: PaddingValues = PaddingValues(vertical = 12.dp, horizontal = 4.dp),
    textAlign: TextAlign = TextAlign.Center,
    useCard: Boolean = false
) {
    if (useCard) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            shape = RoundedCornerShape(8.dp)
        ) {
            DataDisplayContent(
                label = label,
                value = value,
                color = color,
                valueStyle = valueStyle,
                labelStyle = labelStyle,
                labelColor = labelColor,
                valueFontWeight = valueFontWeight,
                padding = padding,
                textAlign = textAlign
            )
        }
    } else {
        DataDisplayContent(
            label = label,
            value = value,
            color = color,
            valueStyle = valueStyle,
            labelStyle = labelStyle,
            labelColor = labelColor,
            valueFontWeight = valueFontWeight,
            padding = padding,
            textAlign = textAlign,
            modifier = modifier
        )
    }
}

/**
 * Implementación interna del contenido para DataDisplayBox
 * Evita duplicación de código entre las variantes con Card y sin Card
 */
@Composable
private fun DataDisplayContent(
    label: String,
    value: String,
    color: Color,
    valueStyle: TextStyle,
    labelStyle: TextStyle,
    labelColor: Color,
    valueFontWeight: FontWeight,
    padding: PaddingValues,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = valueStyle,
            fontWeight = valueFontWeight,
            color = color,
            textAlign = textAlign
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = labelStyle,
            color = labelColor,
            textAlign = textAlign
        )
    }
}
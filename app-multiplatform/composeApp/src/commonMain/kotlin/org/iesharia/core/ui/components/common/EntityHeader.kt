package org.iesharia.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Componente reutilizable para mostrar encabezados de entidades como equipos, competiciones y luchadores.
 * Proporciona una estructura consistente con imagen, título e información adicional.
 *
 * @param title Título principal de la entidad
 * @param modifier Modificador para personalizar el componente
 * @param iconVector Icono vectorial a mostrar (se usa si painter es null)
 * @param iconPainter Painter para mostrar una imagen (prioridad sobre iconVector)
 * @param iconSize Tamaño del icono o imagen
 * @param iconBackground Color de fondo para el contenedor del icono
 * @param iconTint Color para tintar el icono (solo aplicable a iconVector)
 * @param containerColor Color de fondo para todo el componente
 * @param titleStyle Estilo para el texto del título
 * @param titleColor Color para el texto del título
 * @param subtitleContent Contenido composable para mostrar debajo del título
 * @param bottomContent Contenido composable para mostrar en la parte inferior
 */
@Composable
fun EntityHeader(
    title: String,
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    iconSize: Dp = 100.dp,
    iconBackground: Color = Color(0x20FFFFFF),
    iconTint: Color = Color.White.copy(alpha = 0.9f),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    titleColor: Color = Color.White,
    subtitleContent: @Composable (() -> Unit)? = null,
    bottomContent: @Composable (() -> Unit)? = null
) {
    Surface(
        color = containerColor,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = WrestlingTheme.dimensions.spacing_24, bottom = WrestlingTheme.dimensions.spacing_16),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Icono o imagen
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .background(iconBackground),
                contentAlignment = Alignment.Center
            ) {
                when {
                    iconPainter != null -> {
                        androidx.compose.foundation.Image(
                            painter = iconPainter,
                            contentDescription = title,
                            modifier = Modifier.size(iconSize * 0.6f)
                        )
                    }
                    iconVector != null -> {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = title,
                            modifier = Modifier.size(iconSize * 0.6f),
                            tint = iconTint
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Título
            Text(
                text = title,
                style = titleStyle,
                fontWeight = FontWeight.Bold,
                color = titleColor,
                textAlign = TextAlign.Center
            )

            // Contenido de subtítulo (si existe)
            if (subtitleContent != null) {
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
                subtitleContent()
            }

            // Contenido inferior (si existe)
            if (bottomContent != null) {
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                bottomContent()
            }
        }
    }
}

/**
 * Versión simplificada de EntityHeader para casos donde solo se necesita título y subtítulo
 * como texto simple, sin componentes personalizados
 */
@Composable
fun SimpleEntityHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    iconSize: Dp = 100.dp,
    iconBackground: Color = Color(0x20FFFFFF),
    iconTint: Color = Color.White.copy(alpha = 0.9f),
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    titleStyle: TextStyle = MaterialTheme.typography.headlineSmall,
    titleColor: Color = Color.White,
    subtitleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    subtitleColor: Color = Color.White.copy(alpha = 0.8f),
    bottomContent: @Composable (() -> Unit)? = null
) {
    EntityHeader(
        title = title,
        modifier = modifier,
        iconVector = iconVector,
        iconPainter = iconPainter,
        iconSize = iconSize,
        iconBackground = iconBackground,
        iconTint = iconTint,
        containerColor = containerColor,
        titleStyle = titleStyle,
        titleColor = titleColor,
        subtitleContent = {
            Text(
                text = subtitle,
                style = subtitleStyle,
                color = subtitleColor,
                textAlign = TextAlign.Center
            )
        },
        bottomContent = bottomContent
    )
}

/**
 * Contenedor para chips de información en la parte inferior de un EntityHeader
 */
@Composable
fun EntityHeaderChips(
    modifier: Modifier = Modifier,
    containerColor: Color = Color(0x33000000),
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor)
            .padding(
                horizontal = WrestlingTheme.dimensions.spacing_8,
                vertical = WrestlingTheme.dimensions.spacing_12
            )
    ) {
        content()
    }
}
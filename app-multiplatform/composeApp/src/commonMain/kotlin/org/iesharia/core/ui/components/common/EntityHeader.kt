package org.iesharia.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme


/**
 * Componente reutilizable para mostrar encabezados de entidades
 */
@Composable
fun EntityHeader(
    title: String,
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    iconPainter: Painter? = null,
    iconSize: Dp = 80.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = WrestlingTheme.dimensions.spacing_16,
        vertical = WrestlingTheme.dimensions.spacing_16
    ),
    subtitleContent: @Composable (() -> Unit)? = null,
    bottomContent: @Composable (() -> Unit)? = null
) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier.fillMaxWidth()
    ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(contentPadding),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Icono o imagen
                        Box(
                            modifier = Modifier
                                .size(iconSize)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)),
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
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_16))

                        // Información de texto
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            if (subtitleContent != null) {
                                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))
                                subtitleContent()
                            }
                        }
                    }

                    // Contenido inferior (si existe)
                    if (bottomContent != null) {
                        bottomContent()
                    }
                }

        }
    }
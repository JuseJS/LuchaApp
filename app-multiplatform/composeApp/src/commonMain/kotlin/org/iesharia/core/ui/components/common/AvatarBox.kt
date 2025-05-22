package org.iesharia.core.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Tipos de avatar disponibles
 */
enum class AvatarType {
    WRESTLER,
    TEAM,
    COMPETITION,
    CUSTOM
}

/**
 * Componente reutilizable para avatares/iconos circulares
 * Centraliza la lógica de mostrar avatares en toda la aplicación
 */
@Composable
fun AvatarBox(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    avatarType: AvatarType = AvatarType.CUSTOM,
    customIcon: ImageVector? = null,
    imageUrl: String? = null,
    fallbackText: String = "",
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconSize: Dp = size * 0.6f
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        when {
            !imageUrl.isNullOrEmpty() -> {
                // TODO: Implementar carga de imagen real con Coil o similar
                // Por ahora mostramos el fallback
                AvatarFallbackContent(
                    avatarType = avatarType,
                    customIcon = customIcon,
                    fallbackText = fallbackText,
                    contentColor = contentColor,
                    iconSize = iconSize
                )
            }
            // Si hay texto de fallback, mostrarlo
            fallbackText.isNotEmpty() -> {
                Text(
                    text = fallbackText.take(2).uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
            }
            // Mostrar icono según el tipo o personalizado
            else -> {
                AvatarFallbackContent(
                    avatarType = avatarType,
                    customIcon = customIcon,
                    fallbackText = "",
                    contentColor = contentColor,
                    iconSize = iconSize
                )
            }
        }
    }
}

@Composable
private fun AvatarFallbackContent(
    avatarType: AvatarType,
    customIcon: ImageVector?,
    fallbackText: String,
    contentColor: Color,
    iconSize: Dp
) {
    val icon = when {
        customIcon != null -> customIcon
        else -> when (avatarType) {
            AvatarType.WRESTLER -> Icons.Default.Person
            AvatarType.TEAM -> Icons.Default.Groups
            AvatarType.COMPETITION -> Icons.Default.EmojiEvents
            AvatarType.CUSTOM -> Icons.Default.Person
        }
    }

    if (fallbackText.isNotEmpty()) {
        Text(
            text = fallbackText.take(2).uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    } else {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(iconSize)
        )
    }
}
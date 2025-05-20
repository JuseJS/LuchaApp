package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.EntityListItem
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.features.wrestlers.domain.model.Wrestler

/**
 * Tarjeta para mostrar un luchador en la alineación del equipo
 */
@Composable
fun WrestlerLineupCard(
    wrestler: Wrestler,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isHome: Boolean = true  // Si es true, ajusta los colores para equipo local, si es false para visitante
) {
    val primaryColor = if (isHome)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.secondary

    val backgroundColor = if (isHome)
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    else
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)

    EntityListItem(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        containerColor = backgroundColor,
        leadingContent = {
            // Avatar del luchador
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(primaryColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                val hasValidImage = wrestler.imageUrl != null && wrestler.imageUrl!!.isNotEmpty()
                if (hasValidImage) {
                    // Implementación de carga de imagen (en una app real)
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        titleContent = {
            Text(
                text = wrestler.fullName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        subtitleContent = {
            if (wrestler.nickname != null) {
                Text(
                    text = "\"${wrestler.nickname}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        trailingContent = {
            InfoBadge(
                text = wrestler.classification.displayName(),
                color = primaryColor,
                backgroundColor = primaryColor.copy(alpha = 0.1f)
            )
        }
    )
}
package org.iesharia.features.teams.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.theme.DarkSurface2
import org.iesharia.core.ui.theme.White90
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.teams.domain.model.Team

/**
 * Tarjeta compacta para mostrar un equipo con icono a la izquierda del nombre
 *
 * @param team Equipo a mostrar
 * @param onClick Acción al hacer clic en la tarjeta
 * @param modifier Modificador para personalizar el componente
 * @param containerColor Color de fondo de la tarjeta
 * @param contentColor Color del contenido (texto, iconos)
 * @param iconBackgroundColor Color de fondo del ícono circular
 * @param iconTintColor Color del ícono
 * @param showCustomIcon Si se debe mostrar un ícono personalizado (por defecto usa Person)
 * @param customIcon Composable opcional para reemplazar el ícono predeterminado
 */
@Composable
fun TeamGridCard(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = DarkSurface2,
    contentColor: Color = White90,
    showDivision: Boolean = false
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        titleAlignment = Alignment.CenterHorizontally,
        contentAlignment = Alignment.CenterHorizontally,
        title = {
            // Layout horizontal con logo a la izquierda del texto
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentWidth()
            ) {
                // Logo
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_8))

                // Team name
                Text(
                    text = team.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = contentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Start
                )
            }
        }
    ) {
        // Show division if requested
        if (showDivision) {
            InfoBadge(
                text = team.divisionCategory.displayName(),
                color = when(team.divisionCategory) {
                    DivisionCategory.PRIMERA -> MaterialTheme.colorScheme.primary
                    DivisionCategory.SEGUNDA -> MaterialTheme.colorScheme.secondary
                    DivisionCategory.TERCERA -> MaterialTheme.colorScheme.tertiary
                }
            )
        }
    }
}

/**
 * Versión simplificada de TeamGridCard compatible con Surface en vez de Card
 * Para casos donde no se necesita la acción onClick
 */
@Composable
fun TeamGridItem(
    team: Team,
    modifier: Modifier = Modifier,
    containerColor: Color = DarkSurface2,
    contentColor: Color = White90
) {
    Surface(
        color = containerColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo o avatar
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(WrestlingTheme.dimensions.spacing_8))

            // Nombre del equipo
            Text(
                text = team.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
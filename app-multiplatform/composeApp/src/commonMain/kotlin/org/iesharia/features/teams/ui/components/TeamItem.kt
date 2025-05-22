package org.iesharia.features.teams.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.AvatarBox
import org.iesharia.core.ui.components.common.AvatarType
import org.iesharia.core.ui.components.common.EntityListItem
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.core.ui.theme.DarkSurface2
import org.iesharia.core.ui.theme.White90
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.teams.domain.model.Team

/**
 * Tarjeta de equipo optimizada usando componentes reutilizables
 */
@Composable
fun TeamGridCard(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = DarkSurface2,
    contentColor: Color = White90,
    showDivision: Boolean = true
) {
    EntityListItem(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        leadingContent = {
            // Usar AvatarBox reutilizable para el logo del equipo
            AvatarBox(
                size = 40.dp,
                avatarType = AvatarType.TEAM,
                fallbackText = team.name,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                contentColor = MaterialTheme.colorScheme.primary
            )
        },
        titleContent = {
            Text(
                text = team.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = contentColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = if (showDivision) {
            {
                InfoBadge(
                    text = team.divisionCategory.displayName(),
                    color = when(team.divisionCategory) {
                        DivisionCategory.PRIMERA -> MaterialTheme.colorScheme.primary
                        DivisionCategory.SEGUNDA -> MaterialTheme.colorScheme.secondary
                        DivisionCategory.TERCERA -> MaterialTheme.colorScheme.tertiary
                    }
                )
            }
        } else null
    )
}
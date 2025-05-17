package org.iesharia.features.competitions.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.components.common.SectionSubtitle
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.core.resources.AppStrings
import org.iesharia.features.teams.ui.components.MatchDaySection

@Composable
fun CompetitionItem(
    competition: Competition,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showMatchDays: Boolean = true
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        title = {
            Text(
                text = competition.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        subtitle = {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))

            Text(
                text = "${competition.ageCategory.displayName()} - ${competition.divisionCategory.displayName()} - ${competition.island.displayName()}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = AppStrings.Competitions.season.format(competition.season),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    ) {
        // Si se deben mostrar las jornadas y hay jornadas para mostrar
        if (showMatchDays && (competition.lastCompletedMatchDay != null || competition.nextMatchDay != null)) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Última jornada completada
            competition.lastCompletedMatchDay?.let { matchDay ->
                SectionSubtitle(
                    subtitle = AppStrings.Competitions.lastMatchDay.format(matchDay.number),
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
                MatchDaySection(matchDay = matchDay)
                Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
            }

            // Próxima jornada
            competition.nextMatchDay?.let { matchDay ->
                SectionSubtitle(
                    subtitle = AppStrings.Competitions.nextMatchDay.format(matchDay.number),
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
                MatchDaySection(matchDay = matchDay)
            }
        }
    }
}
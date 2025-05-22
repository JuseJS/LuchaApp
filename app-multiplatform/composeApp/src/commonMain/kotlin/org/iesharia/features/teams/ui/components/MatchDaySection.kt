package org.iesharia.features.teams.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.common.InfoBadge
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.theme.*
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match

@Composable
fun MatchDaySection(
    matchDay: MatchDay,
    modifier: Modifier = Modifier,
    onMatchClick: ((String) -> Unit)? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = matchDay.getDateRangeFormatted(),
            style = MaterialTheme.typography.bodyMedium,
            color = White80
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

        matchDay.matches.forEach { match ->
            MatchItemCard(
                match = match,
                modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_8),
                onClick = { onMatchClick?.invoke(match.id) }
            )
        }
    }
}

@Composable
fun MatchItemCard(
    match: Match,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        containerColor = DarkSurface3.copy(alpha = 0.8f),
        title = {
            MatchTeamsRow(match)
        }
    ) {
        MatchInfoRow(match)
    }
}

@Composable
private fun MatchTeamsRow(match: Match) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamScore(
            teamName = match.localTeam.name,
            score = match.localScore,
            isWinner = match.winner == match.localTeam,
            isDrawn = match.isDrawn,
            modifier = Modifier.weight(2f)
        )

        Text(
            text = AppStrings.Teams.vs,
            style = MaterialTheme.typography.bodySmall,
            color = White80,
            modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_8)
        )

        TeamScore(
            teamName = match.visitorTeam.name,
            score = match.visitorScore,
            isWinner = match.winner == match.visitorTeam,
            isDrawn = match.isDrawn,
            modifier = Modifier.weight(2f)
        )
    }
}

@Composable
private fun MatchInfoRow(match: Match) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        InfoBadge(
            text = match.venue,
            color = MaterialTheme.colorScheme.tertiary,
            backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
        )

        InfoBadge(
            text = "${match.date.dayOfMonth}/${match.date.monthNumber}/${match.date.year} ${match.date.hour}:${match.date.minute.toString().padStart(2, '0')}",
            color = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
        )
    }
}

@Composable
private fun TeamScore(
    teamName: String,
    score: Int?,
    isWinner: Boolean,
    isDrawn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            color = White90,
            textAlign = TextAlign.Center
        )

        val scoreText = score?.toString() ?: "-"
        val scoreColor = when {
            !isDrawn && isWinner -> LaurisilvaGreenLight
            isDrawn -> AccentGold
            else -> White90
        }

        Text(
            text = scoreText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
    }
}
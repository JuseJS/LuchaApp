package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.iesharia.features.teams.domain.model.Match

/**
 * Sección que muestra el marcador y los equipos enfrentados
 */
@Composable
fun MatchScoreSection(
    match: Match,
    onLocalTeamClick: () -> Unit,
    onVisitorTeamClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Equipo Local
        TeamScore(
            teamName = match.localTeam.name,
            score = match.localScore,
            isWinner = match.winner == match.localTeam,
            isLocal = true,
            onClick = onLocalTeamClick,
            modifier = Modifier.weight(2f)
        )

        // VS / Resultado
        ScoreDisplay(
            localScore = match.localScore,
            visitorScore = match.visitorScore,
            isCompleted = match.completed,
            modifier = Modifier.weight(1f)
        )

        // Equipo Visitante
        TeamScore(
            teamName = match.visitorTeam.name,
            score = match.visitorScore,
            isWinner = match.winner == match.visitorTeam,
            isLocal = false,
            onClick = onVisitorTeamClick,
            modifier = Modifier.weight(2f)
        )
    }
}
package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.teams.domain.model.Match

/**
 * Cabecera para el detalle de enfrentamiento mostrando equipos, resultado y fecha
 */
@Composable
fun MatchHeader(
    match: Match,
    modifier: Modifier = Modifier,
    onLocalTeamClick: () -> Unit,
    onVisitorTeamClick: () -> Unit
) {
    // Format date without deprecated method
    val dateTime = match.date
    val formattedDate = "${dateTime.dayOfMonth.toString().padStart(2, '0')}/${dateTime.monthNumber.toString().padStart(2, '0')}/${dateTime.year}"
    val formattedTime = "${dateTime.hour.toString().padStart(2, '0')}:${dateTime.minute.toString().padStart(2, '0')}"

    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            // Fecha y hora del enfrentamiento
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "$formattedDate - $formattedTime",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lugar del enfrentamiento
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Place,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = match.venue,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Marcador con equipos
            Row(
                modifier = Modifier.fillMaxWidth(),
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
    }
}
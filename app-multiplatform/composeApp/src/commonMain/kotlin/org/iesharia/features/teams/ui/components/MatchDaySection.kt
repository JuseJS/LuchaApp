package org.iesharia.features.teams.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.core.resources.AppStrings

@Composable
fun MatchDaySection(
    matchDay: MatchDay,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Fechas de la jornada
        Text(
            text = matchDay.getDateRangeFormatted(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

        // Lista de enfrentamientos
        matchDay.matches.forEach { match ->
            MatchItem(
                match = match,
                modifier = Modifier.padding(bottom = WrestlingTheme.dimensions.spacing_8)
            )
        }
    }
}

@Composable
fun MatchItem(
    match: Match,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = WrestlingTheme.shapes.cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_12)
        ) {
            // Equipos y resultado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Equipo local
                TeamScore(
                    teamName = match.localTeam.name,
                    score = match.localScore,
                    isWinner = match.winner == match.localTeam,
                    isDrawn = match.isDrawn,
                    modifier = Modifier.weight(2f)
                )

                // Separador
                Text(
                    text = AppStrings.Teams.vs,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = WrestlingTheme.dimensions.spacing_8)
                )

                // Equipo visitante
                TeamScore(
                    teamName = match.visitorTeam.name,
                    score = match.visitorScore,
                    isWinner = match.winner == match.visitorTeam,
                    isDrawn = match.isDrawn,
                    modifier = Modifier.weight(2f)
                )
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))

            // Información adicional
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Terrero
                Text(
                    text = match.venue,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Fecha
                Text(
                    text = "${match.date.dayOfMonth}/${match.date.month}/${match.date.year} ${match.date.hour}:${match.date.minute.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
        // Nombre del equipo
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        // Puntuación
        val scoreText = score?.toString() ?: "-"
        val scoreColor = when {
            !isDrawn && isWinner -> MaterialTheme.colorScheme.primary
            isDrawn -> MaterialTheme.colorScheme.tertiary
            else -> MaterialTheme.colorScheme.onSurface
        }

        Text(
            text = scoreText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
    }
}
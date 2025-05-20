package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

/**
 * Componente para mostrar un equipo con su puntuación
 */
@Composable
private fun TeamScore(
    teamName: String,
    score: Int?,
    isWinner: Boolean,
    isLocal: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val teamColor = if (isLocal)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.secondary

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        // Avatar/Icono del equipo
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(teamColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            // Aquí iría la imagen del equipo si está disponible
            Text(
                text = teamName.take(2).uppercase(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = teamColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del equipo
        Text(
            text = teamName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2
        )

        // Indicador de Local o Visitante
        Text(
            text = if (isLocal) "LOCAL" else "VISITANTE",
            style = MaterialTheme.typography.labelSmall,
            color = teamColor,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Componente para mostrar el resultado del enfrentamiento
 */
@Composable
private fun ScoreDisplay(
    localScore: Int?,
    visitorScore: Int?,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (isCompleted && localScore != null && visitorScore != null) {
            // Mostrar resultado
            Row(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = WrestlingTheme.shapes.medium
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Puntuación local
                Text(
                    text = localScore.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (localScore > visitorScore)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurface
                )

                // Separador
                Text(
                    text = " - ",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Puntuación visitante
                Text(
                    text = visitorScore.toString(),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (visitorScore > localScore)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.onSurface
                )
            }

            // Estado del enfrentamiento
            if (localScore == visitorScore) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "EMPATE",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Mostrar "VS" para enfrentamientos no completados
            Text(
                text = "VS",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "PENDIENTE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}
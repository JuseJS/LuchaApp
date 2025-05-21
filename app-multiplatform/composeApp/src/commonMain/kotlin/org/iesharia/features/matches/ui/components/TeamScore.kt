package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Componente para mostrar un equipo con su puntuación
 */
@Composable
fun TeamScore(
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
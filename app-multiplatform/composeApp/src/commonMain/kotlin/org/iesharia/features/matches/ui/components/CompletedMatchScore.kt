package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.theme.WrestlingTheme

/**
 * Muestra la puntuación de un enfrentamiento completado
 */
@Composable
fun CompletedMatchScore(
    localScore: Int,
    visitorScore: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
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
            ScoreText(
                score = localScore,
                isWinning = localScore > visitorScore,
                isLocal = true
            )

            // Separador
            Text(
                text = " - ",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Puntuación visitante
            ScoreText(
                score = visitorScore,
                isWinning = visitorScore > localScore,
                isLocal = false
            )
        }

        // Estado del enfrentamiento (empate)
        if (localScore == visitorScore) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "EMPATE",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Componente para mostrar la puntuación de un equipo
 */
@Composable
private fun ScoreText(
    score: Int,
    isWinning: Boolean,
    isLocal: Boolean,
    modifier: Modifier = Modifier
) {
    val textColor = when {
        isWinning && isLocal -> MaterialTheme.colorScheme.primary
        isWinning && !isLocal -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Text(
        text = score.toString(),
        style = MaterialTheme.typography.headlineLarge,
        fontWeight = FontWeight.Bold,
        color = textColor,
        modifier = modifier
    )
}
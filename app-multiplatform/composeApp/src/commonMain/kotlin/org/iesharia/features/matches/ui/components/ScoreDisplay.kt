package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Componente para mostrar el resultado del enfrentamiento
 */
@Composable
fun ScoreDisplay(
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
            CompletedMatchScore(
                localScore = localScore,
                visitorScore = visitorScore
            )
        } else {
            PendingMatchDisplay()
        }
    }
}
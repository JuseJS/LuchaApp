package org.iesharia.features.matches.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WrestlingTheme.dimensions.spacing_16)
        ) {
            // Información de fecha, hora y lugar
            MatchInfoSection(match = match)

            Spacer(modifier = Modifier.height(24.dp))

            // Marcador con equipos
            MatchScoreSection(
                match = match,
                onLocalTeamClick = onLocalTeamClick,
                onVisitorTeamClick = onVisitorTeamClick
            )
        }
    }
}
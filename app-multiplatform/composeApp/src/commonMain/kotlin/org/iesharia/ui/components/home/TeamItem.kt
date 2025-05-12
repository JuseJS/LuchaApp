package org.iesharia.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.iesharia.domain.model.Match
import org.iesharia.domain.model.Team
import org.iesharia.ui.components.common.SectionSubtitle
import org.iesharia.ui.theme.LuchaTheme

/**
 * Item para mostrar un equipo con sus últimos y próximos enfrentamientos
 */
@Composable
fun TeamItem(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    lastMatches: List<Match> = emptyList(),
    nextMatches: List<Match> = emptyList()
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = LuchaTheme.shapes.cardShape,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LuchaTheme.dimensions.spacing_16)
        ) {
            // Cabecera del equipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Logo del equipo (si hay)
                if (team.imageUrl.isNotEmpty()) {
                    // Implementar carga de imagen
                    Spacer(modifier = Modifier.width(LuchaTheme.dimensions.spacing_16))
                }

                Column(modifier = Modifier.weight(1f)) {
                    // Nombre del equipo
                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_4))

                    // Isla
                    Text(
                        text = "Isla: ${team.island.displayName()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Solo mostrar secciones si hay enfrentamientos
            if (lastMatches.isNotEmpty() || nextMatches.isNotEmpty()) {
                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                // Últimos enfrentamientos
                if (lastMatches.isNotEmpty()) {
                    SectionSubtitle(
                        subtitle = "Últimos Enfrentamientos",
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )

                    lastMatches.take(2).forEach { match ->
                        MatchItem(
                            match = match,
                            modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                        )
                    }

                    if (nextMatches.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
                    }
                }

                // Próximos enfrentamientos
                if (nextMatches.isNotEmpty()) {
                    SectionSubtitle(
                        subtitle = "Próximos Enfrentamientos",
                        modifier = Modifier.padding(horizontal = 0.dp)
                    )

                    nextMatches.take(2).forEach { match ->
                        MatchItem(
                            match = match,
                            modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                        )
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                Text(
                    text = "No hay enfrentamientos recientes",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
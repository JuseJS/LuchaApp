package org.iesharia.features.teams.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.components.common.SectionList
import org.iesharia.core.ui.components.common.SectionType
import org.iesharia.core.ui.theme.*
import org.iesharia.core.resources.AppStrings

@Composable
fun TeamItem(
    team: Team,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    lastMatches: List<Match> = emptyList(),
    nextMatches: List<Match> = emptyList()
) {
    ItemCard(
        onClick = onClick,
        modifier = modifier,
        containerColor = DarkSurface2,
        title = {
            // Logo e información del equipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Aquí iría el logo si está disponible
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = team.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = White90
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_4))

                    Text(
                        text = AppStrings.Teams.islandLabel.format(team.island.displayName()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = White80
                    )
                }
            }
        }
    ) {
        // Solo mostrar secciones si hay enfrentamientos
        if (lastMatches.isNotEmpty() || nextMatches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            // Mostrar últimos enfrentamientos
            if (lastMatches.isNotEmpty()) {
                SectionList(
                    items = lastMatches.take(2),
                    title = AppStrings.Teams.lastMatches,
                    type = SectionType.SECONDARY,
                    contentPadding = PaddingValues(0.dp)
                ) { match ->
                    MatchItem(
                        match = match,
                        modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
                    )
                }

                if (nextMatches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                }
            }

            // Mostrar próximos enfrentamientos
            if (nextMatches.isNotEmpty()) {
                SectionList(
                    items = nextMatches.take(2),
                    title = AppStrings.Teams.nextMatches,
                    type = SectionType.SECONDARY,
                    contentPadding = PaddingValues(0.dp)
                ) { match ->
                    MatchItem(
                        match = match,
                        modifier = Modifier.padding(vertical = WrestlingTheme.dimensions.spacing_8)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

            Text(
                text = AppStrings.Teams.noRecentMatches,
                style = MaterialTheme.typography.bodyMedium,
                color = White80,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
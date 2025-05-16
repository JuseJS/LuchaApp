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
import org.iesharia.core.ui.components.common.ItemCard
import org.iesharia.core.ui.components.common.SubSectionList
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
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
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_4))

                    Text(
                        text = AppStrings.Teams.islandLabel.format(team.island.displayName()),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) {
        // Solo mostrar secciones si hay enfrentamientos
        if (lastMatches.isNotEmpty() || nextMatches.isNotEmpty()) {
            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

            // Mostrar últimos enfrentamientos usando SubSectionList
            if (lastMatches.isNotEmpty()) {
                SubSectionList(
                    items = lastMatches.take(2),
                    subtitle = AppStrings.Teams.lastMatches,
                    emptyContent = {},
                    contentPadding = PaddingValues(0.dp)
                ) { match ->
                    MatchItem(
                        match = match,
                        modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                    )
                }

                if (nextMatches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
                }
            }

            // Mostrar próximos enfrentamientos usando SubSectionList
            if (nextMatches.isNotEmpty()) {
                SubSectionList(
                    items = nextMatches.take(2),
                    subtitle = AppStrings.Teams.nextMatches,
                    emptyContent = {},
                    contentPadding = PaddingValues(0.dp)
                ) { match ->
                    MatchItem(
                        match = match,
                        modifier = Modifier.padding(vertical = LuchaTheme.dimensions.spacing_8)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

            Text(
                text = AppStrings.Teams.noRecentMatches,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
package org.iesharia.ui.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import org.iesharia.domain.model.Competition
import org.iesharia.ui.theme.LuchaTheme

/**
 * Item para una competición
 */
@Composable
fun CompetitionItem(
    competition: Competition,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = LuchaTheme.shapes.cardShape,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LuchaTheme.dimensions.spacing_16)
        ) {
            // Encabezado de la competición
            CompetitionHeader(competition)

            Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

            // Última jornada completada
            competition.lastCompletedMatchDay?.let { matchDay ->
                Text(
                    text = "Última jornada (${matchDay.number})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                MatchDaySection(matchDay = matchDay)

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))
            }

            // Próxima jornada
            competition.nextMatchDay?.let { matchDay ->
                Text(
                    text = "Próxima jornada (${matchDay.number})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                MatchDaySection(matchDay = matchDay)
            }
        }
    }
}

/**
 * Encabezado de una competición
 */
@Composable
private fun CompetitionHeader(competition: Competition) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Nombre de la competición
        Text(
            text = competition.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_4))

        // Detalles de la competición
        Text(
            text = "${competition.ageCategory.displayName()} - ${competition.divisionCategory.displayName()} - ${competition.island.displayName()}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Temporada ${competition.season}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
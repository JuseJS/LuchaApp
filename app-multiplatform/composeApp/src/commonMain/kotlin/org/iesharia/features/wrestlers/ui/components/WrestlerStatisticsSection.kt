package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.ui.viewmodel.WrestlerStatistics

/**
 * Sección de estadísticas de rendimiento del luchador
 */
@Composable
fun WrestlerStatisticsSection(
    statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics>
) {
    // Calcular estadísticas agregadas del luchador
    val statistics = calculateWrestlerStatistics(statisticsByClassification)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Título de la sección
        SectionDivider(title = "Rendimiento")

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Resumen de temporada con mejor visualización
        SeasonSummaryCard(
            totalMatches = statistics.totalMatches,
            wins = statistics.wins,
            losses = statistics.losses,
            draws = statistics.draws,
            effectivenessPercentage = statistics.effectivenessPercentage
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Tabla detallada de estadísticas por clasificación
        DetailedStatisticsTable(statisticsByClassification)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Enfrentamientos por tipo de luchador
        OpponentTypeBreakdown(
            puntalesMatches = statistics.puntalesMatches,
            destacadosMatches = statistics.destacadosMatches,
            otherMatches = statistics.otherMatches
        )
    }
}
package org.iesharia.features.wrestlers.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.iesharia.core.ui.components.common.SectionDivider
import org.iesharia.core.ui.components.common.SectionDividerType
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerStatistics

/**
 * Sección de estadísticas de rendimiento del luchador
 */
/**
 * Sección de estadísticas de rendimiento del luchador
 */
@Composable
fun WrestlerStatisticsSection(
    statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics>
) {
    val statistics = calculateWrestlerStatistics(statisticsByClassification)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        SectionDivider(
            title = "Rendimiento",
            type = SectionDividerType.PRIMARY
        )

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
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
    // Calcular totales para el resumen
    val totalMatches = statisticsByClassification.values.sumOf { it.total }
    val totalWins = statisticsByClassification.values.sumOf { it.wins }
    val totalLosses = statisticsByClassification.values.sumOf { it.losses }
    val totalDraws = statisticsByClassification.values.sumOf { it.draws }
    val totalPoints = statisticsByClassification.values.sumOf { it.points }
    val overallEffectiveness = if (totalMatches > 0) {
        (totalPoints / totalMatches) * 100.0
    } else {
        0.0
    }

    // Categorizar por tipo de luchador
    val puntalesMatches = statisticsByClassification
        .filter { it.key in listOf(WrestlerClassification.PUNTAL_A, WrestlerClassification.PUNTAL_B, WrestlerClassification.PUNTAL_C) }
        .values.sumOf { it.total }

    val destacadosMatches = statisticsByClassification
        .filter { it.key in listOf(WrestlerClassification.DESTACADO_A, WrestlerClassification.DESTACADO_B, WrestlerClassification.DESTACADO_C) }
        .values.sumOf { it.total }

    val otherMatches = statisticsByClassification
        .filter { it.key == WrestlerClassification.NONE }
        .values.sumOf { it.total }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = WrestlingTheme.dimensions.spacing_16)
    ) {
        // Reemplazado el título con el componente SectionDivider
        SectionDivider(title = "Rendimiento")

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Resumen de temporada con mejor visualización
        SeasonSummaryCard(
            totalMatches = totalMatches,
            wins = totalWins,
            losses = totalLosses,
            draws = totalDraws,
            effectivenessPercentage = overallEffectiveness
        )

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Tabla detallada de estadísticas por clasificación
        DetailedStatisticsTable(statisticsByClassification)

        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

        // Enfrentamientos por tipo de luchador
        OpponentTypeBreakdown(
            puntalesMatches = puntalesMatches,
            destacadosMatches = destacadosMatches,
            otherMatches = otherMatches
        )
    }
}
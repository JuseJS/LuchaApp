package org.iesharia.features.wrestlers.ui.components

import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerStatistics
import org.iesharia.features.wrestlers.domain.model.AggregatedWrestlerStatistics

/**
 * Calcula estadísticas agregadas a partir de estadísticas por clasificación
 */
fun calculateWrestlerStatistics(
    statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics>
): AggregatedWrestlerStatistics {
    // Calcular totales para el resumen
    val totalMatches = statisticsByClassification.values.sumOf { it.total }
    val totalWins = statisticsByClassification.values.sumOf { it.wins }
    val totalLosses = statisticsByClassification.values.sumOf { it.losses }
    val totalDraws = statisticsByClassification.values.sumOf { it.draws }
    val totalPoints = statisticsByClassification.values.sumOf { it.points }
    
    // Calcular efectividad global
    val overallEffectiveness = if (totalMatches > 0) {
        (totalPoints / totalMatches) * 100.0
    } else {
        0.0
    }

    // Categorizar enfrentamientos por tipo de luchador
    val puntalesMatches = statisticsByClassification
        .filter { 
            it.key in listOf(
                WrestlerClassification.PUNTAL_A, 
                WrestlerClassification.PUNTAL_B, 
                WrestlerClassification.PUNTAL_C
            ) 
        }
        .values.sumOf { it.total }

    val destacadosMatches = statisticsByClassification
        .filter { 
            it.key in listOf(
                WrestlerClassification.DESTACADO_A, 
                WrestlerClassification.DESTACADO_B, 
                WrestlerClassification.DESTACADO_C
            ) 
        }
        .values.sumOf { it.total }

    val otherMatches = statisticsByClassification
        .filter { it.key == WrestlerClassification.NONE }
        .values.sumOf { it.total }

    return AggregatedWrestlerStatistics(
        totalMatches = totalMatches,
        wins = totalWins,
        losses = totalLosses,
        draws = totalDraws,
        points = totalPoints,
        effectivenessPercentage = overallEffectiveness,
        puntalesMatches = puntalesMatches,
        destacadosMatches = destacadosMatches,
        otherMatches = otherMatches
    )
}
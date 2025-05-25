package org.iesharia.features.wrestlers.domain.model

/**
 * Estadísticas de rendimiento del luchador por clasificación
 */
data class WrestlerStatistics(
    val total: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0,
    val points: Double = 0.0,
    val effectivenessPercentage: Double = 0.0
)

/**
 * Modelo de datos que representa las estadísticas agregadas de un luchador
 */
data class AggregatedWrestlerStatistics(
    val totalMatches: Int,
    val wins: Int,
    val losses: Int,
    val draws: Int,
    val points: Double,
    val effectivenessPercentage: Double,
    val puntalesMatches: Int,
    val destacadosMatches: Int,
    val otherMatches: Int
)
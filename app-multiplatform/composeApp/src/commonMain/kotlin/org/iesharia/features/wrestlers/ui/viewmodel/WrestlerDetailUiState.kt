package org.iesharia.features.wrestlers.ui.viewmodel

import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Estado de la pantalla de detalle de luchador
 */
data class WrestlerDetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val wrestler: Wrestler? = null,
    val matchResults: List<WrestlerMatchResult> = emptyList(),
    val isFavorite: Boolean = false,
    val statisticsByClassification: Map<WrestlerClassification, WrestlerStatistics> = emptyMap()
)

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
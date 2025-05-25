package org.iesharia.features.wrestlers.ui.viewmodel

import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.model.WrestlerStatistics

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
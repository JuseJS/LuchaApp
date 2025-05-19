package org.iesharia.features.wrestlers.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase

/**
 * ViewModel para la pantalla de detalle de luchador
 */
class WrestlerDetailViewModel(
    private val wrestlerId: String,
    private val wrestlerRepository: WrestlerRepository,
    private val competitionRepository: CompetitionRepository,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase,
    private val navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<WrestlerDetailUiState>(WrestlerDetailUiState(), errorHandler) {

    init {
        loadWrestlerDetails()
    }

    /**
     * Carga los detalles del luchador y sus estadísticas
     */
    private fun loadWrestlerDetails() {
        launchSafe(
            errorHandler = { error ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message
                    )
                }
            }
        ) {
            // Obtener detalles del luchador
            val wrestler = wrestlerRepository.getWrestler(wrestlerId)
                ?: throw AppError.UnknownError(message = "No se encontró el luchador")

            // Obtener resultados de enfrentamientos
            val matchResults = getWrestlerResultsUseCase(wrestlerId)

            // Calcular estadísticas por clasificación
            val statisticsByClassification = calculateStatistics(matchResults)

            // Comprobar si este luchador es favorito
            val favorites = getFavoritesUseCase()
            val isFavorite = favorites.any {
                it is Favorite.WrestlerFavorite && it.wrestler.id == wrestlerId
            }

            // Actualizar estado
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    wrestler = wrestler,
                    matchResults = matchResults,
                    statisticsByClassification = statisticsByClassification,
                    isFavorite = isFavorite
                )
            }
        }
    }

    /**
     * Calcula estadísticas basadas en los resultados de enfrentamientos
     */
    private fun calculateStatistics(matchResults: List<WrestlerMatchResult>): Map<WrestlerClassification, WrestlerStatistics> {
        // Mapa para almacenar estadísticas por clasificación
        val statisticsByClassification = mutableMapOf<WrestlerClassification, WrestlerStatistics>()

        // Inicializar todas las clasificaciones con valores por defecto
        WrestlerClassification.getOrderedValues().forEach { classification ->
            statisticsByClassification[classification] = WrestlerStatistics()
        }

        // Procesar cada resultado de enfrentamiento
        matchResults.forEach { result ->
            // Encontrar la clasificación según el texto (podría mejorarse con un mejor modelo de dominio)
            val classification = WrestlerClassification.entries.find {
                it.displayName() == result.classification
            } ?: WrestlerClassification.NONE

            // Obtener o crear estadísticas para esta clasificación
            val stats = statisticsByClassification[classification] ?: WrestlerStatistics()

            // Actualizar estadísticas según el resultado
            val updatedStats = when (result.result) {
                WrestlerMatchResult.Result.WIN -> stats.copy(
                    total = stats.total + 1,
                    wins = stats.wins + 1,
                    points = stats.points + 1.0
                )
                WrestlerMatchResult.Result.LOSS -> stats.copy(
                    total = stats.total + 1,
                    losses = stats.losses + 1
                )
                WrestlerMatchResult.Result.DRAW -> stats.copy(
                    total = stats.total + 1,
                    draws = stats.draws + 1,
                    points = stats.points + 0.5
                )
                else -> stats.copy(
                    total = stats.total + 1
                )
            }

            // Calcular porcentaje de efectividad
            val effectivenessPercentage = if (updatedStats.total > 0) {
                (updatedStats.points / updatedStats.total) * 100.0
            } else {
                0.0
            }

            // Almacenar estadísticas actualizadas
            statisticsByClassification[classification] = updatedStats.copy(
                effectivenessPercentage = effectivenessPercentage
            )
        }

        return statisticsByClassification
    }

    /**
     * Navega a la pantalla anterior
     */
    fun navigateBack() {
        launchSafe {
            navigationManager.navigateBack()
        }
    }

    /**
     * Navega a la pantalla de detalle de equipo
     */
    fun navigateToTeamDetail(teamId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Team.Detail(), teamId)
        }
    }

    /**
     * Alternar estado de favorito (placeholder para funcionalidad real)
     */
    fun toggleFavorite() {
        // En una implementación real, se llamaría a un usecase para gestionar favoritos
        updateState {
            it.copy(isFavorite = !it.isFavorite)
        }
    }

    /**
     * Recarga los datos
     */
    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadWrestlerDetails()
    }
}
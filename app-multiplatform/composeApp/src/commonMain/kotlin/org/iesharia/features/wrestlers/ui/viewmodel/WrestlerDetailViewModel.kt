package org.iesharia.features.wrestlers.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.common.domain.usecase.ToggleFavoriteUseCase
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.model.WrestlerStatistics
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
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<WrestlerDetailUiState>(WrestlerDetailUiState(), errorHandler, navigationManager) {

    init {
        loadWrestlerDetails()
    }

    /**
     * Carga los detalles del luchador y sus estadísticas
     */
    private fun loadWrestlerDetails() {
        loadEntity(
            entityId = wrestlerId,
            fetchEntity = { wrestlerRepository.getWrestler(wrestlerId) },
            processEntity = { wrestler ->
                // Obtener resultados de enfrentamientos
                val matchResults = getWrestlerResultsUseCase(wrestlerId)

                // Calcular estadísticas
                val statisticsByClassification = calculateStatistics(matchResults)

                // Comprobar si es favorito
                val favorites = getFavoritesUseCase().getOrElse { emptyList() }
                val isFavorite = favorites.any { favorite ->
                    favorite is Favorite.WrestlerFavorite && favorite.wrestler.id == wrestlerId
                }

                // Devolver nuevo estado
                uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    wrestler = wrestler,
                    matchResults = matchResults,
                    statisticsByClassification = statisticsByClassification,
                    isFavorite = isFavorite
                )
            }
        )
    }

    /**
     * Sobrescribir el método de actualización de estado con error
     */
    override fun updateErrorState(currentState: WrestlerDetailUiState, error: AppError): WrestlerDetailUiState {
        return currentState.copy(
            isLoading = false,
            errorMessage = error.message
        )
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
     * Navega a la pantalla de detalle de equipo
     */
    fun navigateToTeamDetail(teamId: String) {
        navigateToEntityDetail(EntityType.TEAM, teamId)
    }

    /**
     * Alternar estado de favorito
     */
    fun toggleFavorite() {
        val currentState = uiState.value
        if (currentState.wrestler == null) return
        
        launchSafe {
            // Actualizar estado UI inmediatamente
            updateState { it.copy(isFavorite = !it.isFavorite) }
            
            // Llamar al servidor
            toggleFavoriteUseCase(
                entityId = wrestlerId,
                entityType = EntityTypeDto.WRESTLER,
                currentState = currentState.isFavorite
            ).fold(
                onSuccess = {
                    // Éxito - el estado UI ya está actualizado
                },
                onFailure = { throwable ->
                    // Error - revertir el estado UI
                    updateState { it.copy(isFavorite = currentState.isFavorite) }
                    errorHandler.handleError(AppError.NetworkError(throwable.message ?: "Error al cambiar favorito"))
                }
            )
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
package org.iesharia.features.competitions.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.common.domain.usecase.ToggleFavoriteUseCase
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class CompetitionDetailViewModel(
    private val competitionId: String,
    private val competitionRepository: CompetitionRepository,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<CompetitionDetailUiState>(CompetitionDetailUiState(), errorHandler, navigationManager) {

    init {
        loadCompetition()
    }

    private fun loadCompetition() {
        loadEntity(
            entityId = competitionId,
            fetchEntity = { competitionRepository.getCompetition(competitionId) },
            processEntity = { competition ->
                // Comprobar si es favorito
                val favorites = getFavoritesUseCase().getOrElse { emptyList() }
                val isFavorite = favorites.any { favorite ->
                    favorite is Favorite.CompetitionFavorite && favorite.competition.id == competitionId
                }

                // Devolver nuevo estado
                uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    competition = competition,
                    isFavorite = isFavorite,
                    matchDays = competition.matchDays.sortedBy { it.number },
                    teams = competition.teams
                )
            }
        )
    }

    /**
     * Sobrescribir el método de actualización de estado con error
     */
    override fun updateErrorState(currentState: CompetitionDetailUiState, error: AppError): CompetitionDetailUiState {
        return currentState.copy(
            isLoading = false,
            errorMessage = error.message
        )
    }

    fun navigateToTeamDetail(teamId: String) {
        navigateToEntityDetail(EntityType.TEAM, teamId)
    }

    fun toggleFavorite() {
        val currentState = uiState.value
        if (currentState.competition == null) return
        
        launchSafe(
            errorHandler = { error ->
                errorHandler.handleError(error)
            }
        ) {
            // Actualizamos el estado UI inmediatamente para una respuesta más rápida
            updateState {
                it.copy(isFavorite = !it.isFavorite)
            }
            
            // Hacemos la llamada al servidor
            toggleFavoriteUseCase(
                entityId = competitionId,
                entityType = EntityTypeDto.COMPETITION,
                currentState = currentState.isFavorite
            ).fold(
                onSuccess = {
                    // Éxito, el estado ya está actualizado
                },
                onFailure = { error ->
                    // Si falla, revertimos el cambio
                    updateState {
                        it.copy(isFavorite = currentState.isFavorite)
                    }
                    errorHandler.handleError(error as? AppError ?: AppError.UnknownError(error, "Error al actualizar favorito"))
                }
            )
        }
    }

    /**
     * Navega al detalle de un enfrentamiento
     */
    fun navigateToMatchDetail(matchId: String) {
        navigateToEntityDetail(EntityType.MATCH, matchId)
    }

    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadCompetition()
    }
}
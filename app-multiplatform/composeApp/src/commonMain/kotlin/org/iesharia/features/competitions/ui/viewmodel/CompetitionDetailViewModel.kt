package org.iesharia.features.competitions.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class CompetitionDetailViewModel(
    private val competitionId: String,
    private val competitionRepository: CompetitionRepository,
    private val getFavoritesUseCase: GetFavoritesUseCase,
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
                val favorites = getFavoritesUseCase()
                val isFavorite = favorites.any {
                    it is Favorite.CompetitionFavorite && it.competition.id == competitionId
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

    // Esta función es un placeholder para la funcionalidad de favoritos
    fun toggleFavorite() {
        // Por ahora, solo actualizamos el estado UI
        updateState {
            it.copy(isFavorite = !it.isFavorite)
        }
    }

    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadCompetition()
    }
}
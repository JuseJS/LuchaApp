package org.iesharia.features.competitions.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class CompetitionDetailViewModel(
    private val competitionId: String,
    private val competitionRepository: CompetitionRepository,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<CompetitionDetailUiState>(CompetitionDetailUiState(), errorHandler) {

    init {
        loadCompetition()
    }

    private fun loadCompetition() {
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
            // Obtener la competición específica
            val competition = competitionRepository.getCompetition(competitionId)
                ?: throw AppError.UnknownError(message = "No se encontró la competición")

            // Comprobar si esta competición es favorita
            val favorites = getFavoritesUseCase()
            val isFavorite = favorites.any {
                it is Favorite.CompetitionFavorite && it.competition.id == competitionId
            }

            // Actualizar el estado
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    competition = competition,
                    isFavorite = isFavorite,
                    matchDays = competition.matchDays.sortedBy { it.number },
                    teams = competition.teams
                )
            }
        }
    }

    fun navigateToTeamDetail(teamId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Team.Detail(), teamId)
        }
    }

    fun navigateBack() {
        launchSafe {
            navigationManager.navigateBack()
        }
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
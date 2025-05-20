package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.matches.domain.usecase.GetMatchDetailsUseCase

class MatchDetailViewModel(
    private val matchId: String,
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val matchRepository: MatchRepository,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<MatchDetailUiState>(MatchDetailUiState(), errorHandler, navigationManager) {

    init {
        loadMatchDetails()
    }

    /**
     * Carga los detalles del enfrentamiento y actualiza el estado UI
     */
    private fun loadMatchDetails() {
        loadEntity(
            entityId = matchId,
            fetchEntity = {
                // Obtener detalles del enfrentamiento
                val (match, localTeamWrestlers, visitorTeamWrestlers) = getMatchDetailsUseCase(matchId)
                match
            },
            processEntity = { match ->
                // Obtener datos adicionales
                val (referee, assistantReferees) = matchRepository.getMatchReferees(matchId)
                val statsMap = matchRepository.getMatchStatistics(matchId)

                // Obtener luchadores de ambos equipos
                val (_, localTeamWrestlers, visitorTeamWrestlers) = getMatchDetailsUseCase(matchId)

                // Crear objeto de estadísticas
                val matchStats = MatchStats(
                    localTeamWins = statsMap["localTeamWins"] as? Int ?: 0,
                    visitorTeamWins = statsMap["visitorTeamWins"] as? Int ?: 0,
                    draws = statsMap["draws"] as? Int ?: 0,
                    separations = statsMap["separations"] as? Int ?: 0,
                    expulsions = statsMap["expulsions"] as? Int ?: 0,
                    totalGrips = statsMap["totalGrips"] as? Int ?: 0,
                    duration = statsMap["duration"] as? String ?: "",
                    spectators = statsMap["spectators"] as? Int
                )

                // Actualizar el estado UI
                uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    match = match,
                    localTeamWrestlers = localTeamWrestlers,
                    visitorTeamWrestlers = visitorTeamWrestlers,
                    referee = referee,
                    assistantReferees = assistantReferees,
                    matchStats = matchStats
                )
            }
        )
    }

    /**
     * Sobrescribir el método de actualización de estado con error
     */
    override fun updateErrorState(currentState: MatchDetailUiState, error: AppError): MatchDetailUiState {
        return currentState.copy(
            isLoading = false,
            errorMessage = error.message
        )
    }

    /**
     * Navega al detalle del equipo local
     */
    fun navigateToLocalTeamDetail() {
        uiState.value.match?.localTeam?.id?.let { teamId ->
            navigateToEntityDetail(EntityType.TEAM, teamId)
        }
    }

    /**
     * Navega al detalle del equipo visitante
     */
    fun navigateToVisitorTeamDetail() {
        uiState.value.match?.visitorTeam?.id?.let { teamId ->
            navigateToEntityDetail(EntityType.TEAM, teamId)
        }
    }

    /**
     * Navega al detalle de un luchador
     */
    fun navigateToWrestlerDetail(wrestlerId: String) {
        navigateToEntityDetail(EntityType.WRESTLER, wrestlerId)
    }

    /**
     * Método público para recargar datos
     */
    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadMatchDetails()
    }
}
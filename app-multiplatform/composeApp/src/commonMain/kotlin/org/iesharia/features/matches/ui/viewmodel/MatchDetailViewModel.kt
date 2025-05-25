package org.iesharia.features.matches.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.matches.domain.usecase.GetMatchDetailsUseCase
import org.iesharia.features.matches.domain.usecase.GetMatchActUseCase

class MatchDetailViewModel(
    private val matchId: String,
    private val getMatchDetailsUseCase: GetMatchDetailsUseCase,
    private val matchRepository: MatchRepository,
    private val getMatchActUseCase: GetMatchActUseCase,
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
                val (match, _, _) = getMatchDetailsUseCase(matchId)
                match
            },
            processEntity = { match ->
                // Intentar obtener el acta del enfrentamiento
                val matchAct = try {
                    getMatchActUseCase.byMatchId(matchId)
                } catch (e: Exception) {
                    println("No se encontró acta para el match: ${e.message}")
                    null
                }
                
                // Obtener luchadores
                val (allLocalWrestlers, allVisitorWrestlers) = try {
                    val (_, local, visitor) = getMatchDetailsUseCase(matchId)
                    Pair(local, visitor)
                } catch (e: Exception) {
                    Pair(emptyList(), emptyList())
                }
                
                // Si hay acta, filtrar solo los luchadores que participaron
                val (localTeamWrestlers, visitorTeamWrestlers) = if (matchAct != null && match.hasAct) {
                    println("Filtrando luchadores que participaron en el acta...")
                    val localActWrestlerIds = matchAct.localTeam.wrestlers.map { it.wrestlerId }.toSet()
                    val visitorActWrestlerIds = matchAct.visitorTeam.wrestlers.map { it.wrestlerId }.toSet()
                    
                    val localParticipants = allLocalWrestlers.filter { it.id in localActWrestlerIds }
                    val visitorParticipants = allVisitorWrestlers.filter { it.id in visitorActWrestlerIds }
                    
                    println("Luchadores locales que participaron: ${localParticipants.size}")
                    println("Luchadores visitantes que participaron: ${visitorParticipants.size}")
                    
                    Pair(localParticipants, visitorParticipants)
                } else {
                    println("No hay acta, mostrando todos los luchadores de los equipos...")
                    Pair(allLocalWrestlers, allVisitorWrestlers)
                }
                
                // Obtener árbitros del acta si existe
                val (referee, assistantReferees) = if (matchAct != null) {
                    val mainRefereeName = "${matchAct.mainReferee.name} (${matchAct.mainReferee.licenseNumber})"
                    val assistantRefereeNames = matchAct.assistantReferees.map { 
                        "${it.name} (${it.licenseNumber})"
                    }
                    Pair(mainRefereeName, assistantRefereeNames)
                } else {
                    matchRepository.getMatchReferees(matchId)
                }
                
                val statsMap = matchRepository.getMatchStatistics(matchId)

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
     * Navega a la pantalla de acta del enfrentamiento
     */
    fun navigateToMatchAct() {
        navigationManager?.let { manager ->
            launchSafe {
                manager.navigateWithParams(Routes.Match.Act(), matchId)
            }
        }
    }

    /**
     * Método público para recargar datos
     */
    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadMatchDetails()
    }
}
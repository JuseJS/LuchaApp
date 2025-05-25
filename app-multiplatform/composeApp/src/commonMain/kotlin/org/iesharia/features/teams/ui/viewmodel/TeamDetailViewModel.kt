package org.iesharia.features.teams.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.common.domain.usecase.ToggleFavoriteUseCase
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.core.domain.model.AppError
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.teams.domain.usecase.GetTeamByIdUseCase
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase

class TeamDetailViewModel(
    private val teamId: String,
    private val competitionRepository: CompetitionRepository,
    private val getTeamByIdUseCase: GetTeamByIdUseCase,
    private val getWrestlersByTeamIdUseCase: GetWrestlersByTeamIdUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<TeamDetailUiState>(TeamDetailUiState(), errorHandler, navigationManager) {

    init {
        loadTeamDetails()
    }

    // Método para cargar datos usando la función existente en BaseViewModel
    private fun loadTeamDetails() {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        // Usar la función loadEntity de BaseViewModel
        loadEntity(
            entityId = teamId,
            fetchEntity = { getTeamByIdUseCase(teamId) },
            processEntity = { team ->
                println("🏀 Cargando detalles del equipo: ${team.name} (ID: ${team.id})")
                
                // Obtener luchadores
                val wrestlers = getWrestlersByTeamIdUseCase(teamId)

                // Agrupar luchadores por categoría
                val groupedWrestlers = wrestlers.groupBy { it.category }
                    .mapValues { (category, categoryWrestlers) ->
                        if (category == WrestlerCategory.REGIONAL) {
                            categoryWrestlers.sortedBy { wrestler ->
                                WrestlerClassification.getOrderedValues().indexOf(wrestler.classification)
                            }
                        } else {
                            categoryWrestlers
                        }
                    }

                // Obtener competiciones
                val teamCompetitions = competitionRepository.getCompetitions().filter { competition ->
                    competition.teams.any { it.id == teamId }
                }
                
                println("🏆 Competiciones del equipo: ${teamCompetitions.size}")
                teamCompetitions.forEach { comp ->
                    println("  - ${comp.name}: ${comp.matchDays.size} jornadas")
                    println("    Equipos: ${comp.teams.size}")
                    println("    Partidos totales: ${comp.matchDays.flatMap { it.matches }.size}")
                }

                // Obtener enfrentamientos
                val matchesByCompetition = teamCompetitions.associate { competition ->
                    println("\n🔍 Buscando partidos del equipo en ${competition.name}")
                    
                    val lastMatchDay = competition.lastCompletedMatchDay?.let { matchDay ->
                        val teamMatch = matchDay.matches.find { match ->
                            match.localTeam.id == teamId || match.visitorTeam.id == teamId
                        }
                        if (teamMatch != null) {
                            println("  ✅ Último partido completado: Jornada ${matchDay.number}")
                            println("     ${teamMatch.localTeam.name} vs ${teamMatch.visitorTeam.name}")
                            matchDay
                        } else null
                    }

                    val nextMatchDay = competition.nextMatchDay?.let { matchDay ->
                        val teamMatch = matchDay.matches.find { match ->
                            match.localTeam.id == teamId || match.visitorTeam.id == teamId
                        }
                        if (teamMatch != null) {
                            println("  ⏭️ Próximo partido: Jornada ${matchDay.number}")
                            println("     ${teamMatch.localTeam.name} vs ${teamMatch.visitorTeam.name}")
                            matchDay
                        } else null
                    }

                    competition.id to Pair(lastMatchDay, nextMatchDay)
                }

                // Comprobar si es favorito
                val favorites = getFavoritesUseCase().getOrElse { emptyList() }
                val isFavorite = favorites.any { favorite ->
                    favorite is Favorite.TeamFavorite && favorite.team.id == teamId
                }

                // Devolver nuevo estado
                uiState.value.copy(
                    isLoading = false,
                    errorMessage = null,
                    team = team,
                    wrestlers = groupedWrestlers,
                    competitions = teamCompetitions,
                    competitionMatches = matchesByCompetition,
                    isFavorite = isFavorite
                )
            }
        )
    }

    /**
     * Método público para recargar datos
     */
    fun refreshData() {
        loadTeamDetails()
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    fun navigateToWrestlerDetail(wrestlerId: String) {
        navigateToEntityDetail(EntityType.WRESTLER, wrestlerId)
    }

    fun navigateToCompetitionDetail(competitionId: String) {
        navigateToEntityDetail(EntityType.COMPETITION, competitionId)
    }

    /**
     * Alternar estado de favorito
     */
    fun toggleFavorite() {
        val currentState = uiState.value
        if (currentState.team == null) return
        
        launchSafe {
            // Actualizar estado UI inmediatamente
            updateState { it.copy(isFavorite = !it.isFavorite) }
            
            // Llamar al servidor
            toggleFavoriteUseCase(
                entityId = teamId,
                entityType = EntityTypeDto.TEAM,
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
     * Navega al detalle de un enfrentamiento
     */
    fun navigateToMatchDetail(matchId: String) {
        navigateToEntityDetail(EntityType.MATCH, matchId)
    }

    /**
     * Filtra los luchadores de una categoría según el término de búsqueda
     */
    fun getFilteredWrestlers(category: WrestlerCategory): List<Wrestler> {
        val wrestlers = uiState.value.wrestlers[category] ?: emptyList()
        val query = uiState.value.searchQuery.lowercase()

        return if (query.isBlank()) {
            wrestlers
        } else {
            wrestlers.filter {
                it.name.lowercase().contains(query) ||
                        it.surname.lowercase().contains(query) ||
                        it.fullName.lowercase().contains(query)
            }
        }
    }
}
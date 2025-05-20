package org.iesharia.features.teams.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase

class TeamDetailViewModel(
    private val teamId: String,
    private val competitionRepository: CompetitionRepository,
    private val getWrestlersByTeamIdUseCase: GetWrestlersByTeamIdUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<TeamDetailUiState>(TeamDetailUiState(), errorHandler, navigationManager) {

    init {
        loadTeamDetails()
    }

    // Método para cargar datos
    private fun loadTeamDetails() {
        updateState { it.copy(isLoading = true, errorMessage = null) }

        loadEntity(
            entityId = teamId,
            fetchEntity = {
                val competitions = competitionRepository.getCompetitions()
                competitions.flatMap { it.teams }.find { it.id == teamId }
            },
            processEntity = { team ->
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

                // Obtener enfrentamientos
                val matchesByCompetition = teamCompetitions.associate { competition ->
                    val lastMatchDay = competition.lastCompletedMatchDay?.let { matchDay ->
                        if (matchDay.matches.any { match ->
                                match.localTeam.id == teamId || match.visitorTeam.id == teamId
                            }) matchDay else null
                    }

                    val nextMatchDay = competition.nextMatchDay?.let { matchDay ->
                        if (matchDay.matches.any { match ->
                                match.localTeam.id == teamId || match.visitorTeam.id == teamId
                            }) matchDay else null
                    }

                    competition.id to Pair(lastMatchDay, nextMatchDay)
                }

                // Comprobar si es favorito
                val favorites = getFavoritesUseCase()
                val isFavorite = favorites.any {
                    it is Favorite.TeamFavorite && it.team.id == teamId
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
     * Sobrescribir el método de actualización de estado con error
     */
    override fun updateErrorState(currentState: TeamDetailUiState, error: AppError): TeamDetailUiState {
        return currentState.copy(
            isLoading = false,
            errorMessage = error.message
        )
    }

    /**
     * Método público para recargar datos
     */
    fun refreshData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
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
        // Por ahora, solo actualizamos el estado UI
        updateState {
            it.copy(isFavorite = !it.isFavorite)
        }
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
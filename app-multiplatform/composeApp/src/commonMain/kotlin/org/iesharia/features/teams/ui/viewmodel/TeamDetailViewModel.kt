package org.iesharia.features.teams.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
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
    private val navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<TeamDetailUiState>(TeamDetailUiState(), errorHandler) {

    init {
        loadTeamDetails()
    }

    private fun loadTeamDetails() {
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
            // Obtener datos del equipo
            val competitions = competitionRepository.getCompetitions()
            val team = competitions.flatMap { it.teams }.find { it.id == teamId }
                ?: throw AppError.UnknownError(message = "No se encontró el equipo")

            // Obtener luchadores del equipo usando el caso de uso específico
            val wrestlers = getWrestlersByTeamIdUseCase(teamId)

            // Agrupar luchadores por categoría
            val groupedWrestlers = wrestlers.groupBy { it.category }
                .mapValues { (category, wrestlers) ->
                    // Ordenar por clasificación dentro de cada categoría
                    if (category == WrestlerCategory.REGIONAL) {
                        wrestlers.sortedBy { wrestler ->
                            WrestlerClassification.getOrderedValues().indexOf(wrestler.classification)
                        }
                    } else {
                        // Para juveniles y cadetes, el orden no importa por clasificación
                        wrestlers
                    }
                }

            // Obtener competiciones en las que participa el equipo
            val teamCompetitions = competitions.filter { competition ->
                competition.teams.any { it.id == teamId }
            }

            // Obtener último y próximo enfrentamiento por competición
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

            // Comprobar si este equipo es favorito
            val favorites = getFavoritesUseCase()
            val isFavorite = favorites.any {
                it is Favorite.TeamFavorite && it.team.id == teamId
            }

            // Actualizar el estado
            updateState {
                it.copy(
                    isLoading = false,
                    errorMessage = null,
                    team = team,
                    wrestlers = groupedWrestlers,
                    competitions = teamCompetitions,
                    competitionMatches = matchesByCompetition,
                    isFavorite = isFavorite
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    fun navigateBack() {
        launchSafe {
            navigationManager.navigateBack()
        }
    }

    fun navigateToWrestlerDetail(wrestlerId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Wrestler.Detail(), wrestlerId)
        }
    }

    fun navigateToCompetitionDetail(competitionId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Competition.Detail(), competitionId)
        }
    }

    // Esta función es un placeholder para la funcionalidad de favoritos
    fun toggleFavorite() {
        // Por ahora, solo actualizamos el estado UI
        updateState {
            it.copy(isFavorite = !it.isFavorite)
        }
    }

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
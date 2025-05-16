package org.iesharia.features.home.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.usecase.GetTeamMatchesUseCase
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes

class HomeViewModel(
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getTeamMatchesUseCase: GetTeamMatchesUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase,
    private val navigationManager: NavigationManager
) : BaseViewModel<HomeUiState>(HomeUiState(isLoading = true)) {

    // Datos cacheados para acceso rápido
    private var competitions: List<Competition> = emptyList()
    private var favorites: List<Favorite> = emptyList()
    private val teamMatchesCache = mutableMapOf<String, Pair<List<Match>, List<Match>>>()
    private val wrestlerResultsCache = mutableMapOf<String, List<WrestlerMatchResult>>()

    init {
        loadData()
    }

    /**
     * Carga los datos iniciales
     */
    private fun loadData() {
        launchSafe {
            competitions = getCompetitionsUseCase()
            favorites = getFavoritesUseCase()

            updateState {
                it.copy(
                    isLoading = false,
                    favorites = favorites,
                    competitions = competitions
                )
            }
        }
    }

    /**
     * Cambia el tipo de favorito seleccionado
     */
    fun setFavoriteType(type: FavoriteType) {
        updateState { it.copy(selectedFavoriteType = type) }
    }

    /**
     * Actualiza los filtros de competición
     */
    fun updateFilters(
        ageCategory: AgeCategory? = uiState.value.filters.ageCategory,
        divisionCategory: DivisionCategory? = uiState.value.filters.divisionCategory,
        island: Island? = uiState.value.filters.island
    ) {
        updateState {
            it.copy(
                filters = CompetitionFilters(
                    ageCategory = ageCategory,
                    divisionCategory = divisionCategory,
                    island = island
                )
            )
        }
    }

    /**
     * Limpia todos los filtros
     */
    fun clearFilters() {
        updateState {
            it.copy(filters = CompetitionFilters())
        }
    }

    /**
     * Obtiene las competiciones filtradas según los criterios seleccionados
     */
    fun getFilteredCompetitions(): List<Competition> {
        val filters = uiState.value.filters

        return uiState.value.competitions.filter { competition ->
            (filters.ageCategory == null || competition.ageCategory == filters.ageCategory) &&
                    (filters.divisionCategory == null || competition.divisionCategory == filters.divisionCategory) &&
                    (filters.island == null || competition.island == filters.island)
        }
    }

    /**
     * Obtiene los favoritos filtrados según el tipo seleccionado
     */
    fun getFilteredFavorites(): List<Favorite> {
        return when (uiState.value.selectedFavoriteType) {
            FavoriteType.ALL -> uiState.value.favorites
            FavoriteType.COMPETITIONS -> uiState.value.favorites.filterIsInstance<Favorite.CompetitionFavorite>()
            FavoriteType.TEAMS -> uiState.value.favorites.filterIsInstance<Favorite.TeamFavorite>()
            FavoriteType.WRESTLERS -> uiState.value.favorites.filterIsInstance<Favorite.WrestlerFavorite>()
        }
    }

    /**
     * Obtiene los últimos enfrentamientos de un equipo
     */
    fun getTeamLastMatches(teamId: String): List<Match> {
        if (!teamMatchesCache.containsKey(teamId)) {
            launchSafe {
                teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
            }
        }
        return teamMatchesCache[teamId]?.first ?: emptyList()
    }

    /**
     * Obtiene los próximos enfrentamientos de un equipo
     */
    fun getTeamNextMatches(teamId: String): List<Match> {
        if (!teamMatchesCache.containsKey(teamId)) {
            launchSafe {
                teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
            }
        }
        return teamMatchesCache[teamId]?.second ?: emptyList()
    }

    /**
     * Obtiene los resultados de enfrentamientos de un luchador
     */
    fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> {
        if (!wrestlerResultsCache.containsKey(wrestlerId)) {
            launchSafe {
                wrestlerResultsCache[wrestlerId] = getWrestlerResultsUseCase(wrestlerId)
            }
        }
        return wrestlerResultsCache[wrestlerId] ?: emptyList()
    }

    // Añadir métodos de navegación
    fun navigateToCompetitionDetail(competitionId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Competition.Detail(), competitionId)
        }
    }

    fun navigateToTeamDetail(teamId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Team.Detail(), teamId)
        }
    }

    fun navigateToWrestlerDetail(wrestlerId: String) {
        launchSafe {
            navigationManager.navigateWithParams(Routes.Wrestler.Detail(), wrestlerId)
        }
    }

    /**
     * Sobrescribe el manejo de errores por defecto
     */
    override fun handleDefaultError(e: Exception) {
        // Actualizar el estado para mostrar el error en la UI si es necesario
        updateState { it.copy(isLoading = false) }
        println("Error en HomeViewModel: ${e.message}")
    }
}
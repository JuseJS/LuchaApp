package org.iesharia.features.home.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.domain.usecase.GetTeamMatchesUseCase
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase

class HomeViewModel(
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getTeamMatchesUseCase: GetTeamMatchesUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<HomeUiState>(HomeUiState(isLoading = true), errorHandler, navigationManager) {

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
        // Como no cargamos una entidad específica, usamos launchSafe directamente
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
            // Cargar datos
            competitions = getCompetitionsUseCase()
            favorites = getFavoritesUseCase()

            // Actualizar estado
            updateState {
                it.copy(
                    isLoading = false,
                    favorites = favorites,
                    competitions = competitions,
                    errorMessage = null
                )
            }
        }
    }

    override fun updateErrorState(currentState: HomeUiState, error: AppError): HomeUiState {
        return currentState.copy(
            isLoading = false,
            errorMessage = error.message
        )
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
            launchIO {
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
            launchIO {
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
            launchIO {
                wrestlerResultsCache[wrestlerId] = getWrestlerResultsUseCase(wrestlerId)
            }
        }
        return wrestlerResultsCache[wrestlerId] ?: emptyList()
    }

    // Métodos de navegación
    fun navigateToCompetitionDetail(competitionId: String) {
        navigateToEntityDetail(EntityType.COMPETITION, competitionId)
    }

    fun navigateToTeamDetail(teamId: String) {
        navigateToEntityDetail(EntityType.TEAM, teamId)
    }

    fun navigateToWrestlerDetail(wrestlerId: String) {
        navigateToEntityDetail(EntityType.WRESTLER, wrestlerId)
    }

    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    // Obtener todos los equipos de todas las competiciones
    fun getAllTeams(): List<Team> {
        return competitions.flatMap { it.teams }.distinctBy { it.id }
    }

    fun getTeamsByDivision(): Map<DivisionCategory, List<Team>> {
        return getAllTeams().groupBy { it.divisionCategory }
    }

    // Obtener resultados de búsqueda
    fun getSearchResults(): Triple<List<Competition>, List<Team>, List<Wrestler>> {
        val query = uiState.value.searchQuery.lowercase()
        if (query.isBlank()) {
            return Triple(emptyList(), emptyList(), emptyList())
        }

        // Filtrar competiciones
        val competitionResults = competitions.filter {
            it.name.lowercase().contains(query) ||
                    it.ageCategory.displayName().lowercase().contains(query) ||
                    it.divisionCategory.displayName().lowercase().contains(query) ||
                    it.island.displayName().lowercase().contains(query)
        }

        // Filtrar equipos
        val teamResults = getAllTeams().filter {
            it.name.lowercase().contains(query) ||
                    it.island.displayName().lowercase().contains(query) ||
                    it.venue.lowercase().contains(query)
        }

        // Para luchadores, dejamos vacío por ahora ya que no tenemos un repositorio global
        val wrestlerResults = emptyList<Wrestler>()

        return Triple(competitionResults, teamResults, wrestlerResults)
    }

    /**
     * Recarga los datos
     */
    fun reloadData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadData()
    }
}
package org.iesharia.features.home.ui.viewmodel

import kotlinx.coroutines.flow.collect
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes
import org.iesharia.features.auth.domain.repository.UserRepository
import org.iesharia.features.auth.domain.security.SessionManager
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.common.domain.usecase.ToggleFavoriteUseCase
import org.iesharia.features.common.domain.usecase.ObserveFavoritesUseCase
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.features.common.domain.model.FavoriteType
import org.iesharia.features.common.domain.model.CompetitionFilters
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.teams.domain.usecase.GetTeamMatchesUseCase
import org.iesharia.features.teams.domain.usecase.GetAllTeamsUseCase
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase

class HomeViewModel(
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val observeFavoritesUseCase: ObserveFavoritesUseCase,
    private val getTeamMatchesUseCase: GetTeamMatchesUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase,
    private val getAllTeamsUseCase: GetAllTeamsUseCase,
    navigationManager: NavigationManager,
    errorHandler: ErrorHandler,
    private val userRepository: UserRepository,
    private val sessionManager: SessionManager
) : BaseViewModel<HomeUiState>(HomeUiState(isLoading = true), errorHandler, navigationManager) {

    // Datos cacheados para acceso rápido
    private var competitions: List<Competition> = emptyList()
    private var teams: List<Team> = emptyList()
    private var favorites: List<Favorite> = emptyList()
    private val teamMatchesCache = mutableMapOf<String, Pair<List<Match>, List<Match>>>()
    private val wrestlerResultsCache = mutableMapOf<String, List<WrestlerMatchResult>>()

    init {
        loadData()
        observeFavorites()
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
            teams = getAllTeamsUseCase()

            // Solo cargar favoritos si el usuario está autenticado
            favorites = if (sessionManager.isLoggedIn.value) {
                getFavoritesUseCase().getOrElse { emptyList() }
            } else {
                emptyList()
            }

            // Actualizar estado
            updateState {
                it.copy(
                    isLoading = false,
                    favorites = favorites,
                    competitions = competitions,
                    teams = teams,
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
     * Obtiene los equipos filtrados por división
     */
    fun getTeamsByDivision(division: DivisionCategory): List<Team> {
        return uiState.value.teams.filter { it.divisionCategory == division }
    }

    /**
     * Actualiza la búsqueda
     */
    fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    /**
     * Obtiene los resultados de búsqueda
     */
    fun getSearchResults(): Triple<List<Competition>, List<Team>, List<Wrestler>> {
        val query = uiState.value.searchQuery.lowercase()
        
        if (query.isBlank()) {
            return Triple(emptyList(), emptyList(), emptyList())
        }
        
        val competitions = uiState.value.competitions.filter { 
            it.name.lowercase().contains(query) ||
            it.island.name.lowercase().contains(query) ||
            it.season.lowercase().contains(query)
        }
        
        val teams = uiState.value.teams.filter {
            it.name.lowercase().contains(query)
        }
        
        // Por ahora no buscamos luchadores ya que no están cargados en el estado
        val wrestlers = emptyList<Wrestler>()
        
        return Triple(competitions, teams, wrestlers)
    }

    /**
     * Navega al detalle de una competición
     */
    fun navigateToCompetitionDetail(competitionId: String) {
        launchSafe {
            navigationManager?.navigate(Routes.Competition.Detail(competitionId))
        }
    }

    /**
     * Navega al detalle de un equipo
     */
    fun navigateToTeamDetail(teamId: String) {
        launchSafe {
            navigationManager?.navigate(Routes.Team.Detail(teamId))
        }
    }

    /**
     * Navega al detalle de un luchador
     */
    fun navigateToWrestlerDetail(wrestlerId: String) {
        launchSafe {
            navigationManager?.navigate(Routes.Wrestler.Detail(wrestlerId))
        }
    }

    /**
     * Navega al detalle de un partido
     */
    fun navigateToMatchDetail(matchId: String) {
        launchSafe {
            navigationManager?.navigate(Routes.Match.Detail(matchId))
        }
    }

    /**
     * Recarga los datos
     */
    fun refresh() {
        loadData()
    }

    /**
     * Recarga los datos (alias para refresh)
     */
    fun reloadData() {
        refresh()
    }

    /**
     * Realiza el logout
     */
    fun logout() {
        launchSafe {
            sessionManager.logout()
            // Navegar al login y limpiar el back stack
            navigationManager?.navigate(Routes.Auth.Login, clearBackStack = true)
        }
    }

    /**
     * Navega a la pantalla de login
     */
    fun navigateToLogin() {
        launchSafe {
            navigationManager?.navigate(Routes.Auth.Login)
        }
    }
    
    /**
     * Navega a la pantalla de configuración
     */
    fun navigateToConfig() {
        launchSafe {
            navigationManager?.navigate(Routes.Config.Settings)
        }
    }

    /**
     * Obtiene los partidos cacheados de un equipo
     */
    fun getCachedTeamMatches(teamId: String): Pair<List<Match>, List<Match>>? {
        return teamMatchesCache[teamId]
    }

    /**
     * Cachea los partidos de un equipo
     */
    fun cacheTeamMatches(teamId: String, matches: Pair<List<Match>, List<Match>>) {
        teamMatchesCache[teamId] = matches
    }

    /**
     * Obtiene los resultados cacheados de un luchador
     */
    fun getCachedWrestlerResults(wrestlerId: String): List<WrestlerMatchResult>? {
        return wrestlerResultsCache[wrestlerId]
    }

    /**
     * Cachea los resultados de un luchador
     */
    fun cacheWrestlerResults(wrestlerId: String, results: List<WrestlerMatchResult>) {
        wrestlerResultsCache[wrestlerId] = results
    }
    
    /**
     * Añade o quita un elemento de favoritos
     */
    fun toggleFavorite(entityId: String, entityType: EntityTypeDto, currentState: Boolean) {
        if (!sessionManager.isLoggedIn.value) {
            launchSafe(
                errorHandler = { /* Manejado internamente */ }
            ) {
                errorHandler.handleError(AppError.AuthError("Debes iniciar sesión para usar favoritos"))
            }
            return
        }
        
        launchSafe(
            errorHandler = { error ->
                errorHandler.handleError(error)
            }
        ) {
            toggleFavoriteUseCase(entityId, entityType, currentState).fold(
                onSuccess = {
                    // Recargar favoritos
                    loadData()
                },
                onFailure = { error ->
                    errorHandler.handleError(error as? AppError ?: AppError.UnknownError(error, "Error al actualizar favorito"))
                }
            )
        }
    }
    
    /**
     * Observa los cambios en favoritos
     */
    private fun observeFavorites() {
        launchSafe(
            errorHandler = { /* No hacer nada, es opcional */ }
        ) {
            observeFavoritesUseCase().collect { favoriteIds ->
                updateState { currentState ->
                    currentState.copy(favoriteIds = favoriteIds)
                }
            }
        }
    }
}
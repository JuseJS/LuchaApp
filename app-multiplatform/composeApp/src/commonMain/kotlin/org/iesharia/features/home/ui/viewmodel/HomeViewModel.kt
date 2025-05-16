package org.iesharia.features.home.ui.viewmodel

import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.iesharia.core.domain.model.AppError
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
    private val navigationManager: NavigationManager,
    errorHandler: ErrorHandler
) : BaseViewModel<HomeUiState>(HomeUiState(isLoading = true), errorHandler) {

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
        launchSafe(
            handleAppError = { appError ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = appError.message
                    )
                }
            }
        ) {
            try {
                competitions = getCompetitionsUseCase()
                favorites = getFavoritesUseCase()

                updateState {
                    it.copy(
                        isLoading = false,
                        favorites = favorites,
                        competitions = competitions,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                // Cualquier excepción que no sea AppError se convierte
                throw errorHandler?.convertException(e) ?: AppError.UnknownError(e)
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
            launchSafe(
                handleAppError = { appError ->
                    // Manejar error de carga de enfrentamientos
                    println("Error al cargar enfrentamientos: ${appError.message}")
                }
            ) {
                try {
                    teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
                } catch (e: Exception) {
                    throw errorHandler?.convertException(e) ?: AppError.UnknownError(e)
                }
            }
        }
        return teamMatchesCache[teamId]?.first ?: emptyList()
    }

    /**
     * Obtiene los próximos enfrentamientos de un equipo
     */
    fun getTeamNextMatches(teamId: String): List<Match> {
        if (!teamMatchesCache.containsKey(teamId)) {
            launchSafe(
                handleAppError = { appError ->
                    // Manejar error de carga de enfrentamientos
                    println("Error al cargar enfrentamientos: ${appError.message}")
                }
            ) {
                try {
                    teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
                } catch (e: Exception) {
                    throw errorHandler?.convertException(e) ?: AppError.UnknownError(e)
                }
            }
        }
        return teamMatchesCache[teamId]?.second ?: emptyList()
    }

    /**
     * Obtiene los resultados de enfrentamientos de un luchador
     */
    fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> {
        if (!wrestlerResultsCache.containsKey(wrestlerId)) {
            launchSafe(
                handleAppError = { appError ->
                    // Manejar error de carga de resultados
                    println("Error al cargar resultados: ${appError.message}")
                }
            ) {
                try {
                    wrestlerResultsCache[wrestlerId] = getWrestlerResultsUseCase(wrestlerId)
                } catch (e: Exception) {
                    throw errorHandler?.convertException(e) ?: AppError.UnknownError(e)
                }
            }
        }
        return wrestlerResultsCache[wrestlerId] ?: emptyList()
    }

    // Añadir métodos de navegación
    fun navigateToCompetitionDetail(competitionId: String) {
        launchSafe(
            handleAppError = { appError ->
                // Mostrar error de navegación si ocurre
                updateState { it.copy(errorMessage = appError.message) }
            }
        ) {
            navigationManager.navigateWithParams(Routes.Competition.Detail(), competitionId)
        }
    }

    fun navigateToTeamDetail(teamId: String) {
        launchSafe(
            handleAppError = { appError ->
                // Mostrar error de navegación si ocurre
                updateState { it.copy(errorMessage = appError.message) }
            }
        ) {
            navigationManager.navigateWithParams(Routes.Team.Detail(), teamId)
        }
    }

    fun navigateToWrestlerDetail(wrestlerId: String) {
        launchSafe(
            handleAppError = { appError ->
                // Mostrar error de navegación si ocurre
                updateState { it.copy(errorMessage = appError.message) }
            }
        ) {
            navigationManager.navigateWithParams(Routes.Wrestler.Detail(), wrestlerId)
        }
    }

    /**
     * Recarga los datos (puede ser llamado por la UI en caso de error)
     */
    fun reloadData() {
        updateState { it.copy(isLoading = true, errorMessage = null) }
        loadData()
    }
}
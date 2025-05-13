package org.iesharia.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.iesharia.domain.model.*
import org.iesharia.domain.usecase.*

class HomeViewModel(
    private val getCompetitionsUseCase: GetCompetitionsUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val getTeamMatchesUseCase: GetTeamMatchesUseCase,
    private val getWrestlerResultsUseCase: GetWrestlerResultsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
        viewModelScope.launch {
            try {
                competitions = getCompetitionsUseCase()
                favorites = getFavoritesUseCase()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        favorites = favorites,
                        competitions = competitions
                    )
                }
            } catch (e: Exception) {
                // Manejar error
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Cambia el tipo de favorito seleccionado
     */
    fun setFavoriteType(type: FavoriteType) {
        _uiState.update { it.copy(selectedFavoriteType = type) }
    }

    /**
     * Actualiza los filtros de competición
     */
    fun updateFilters(
        ageCategory: AgeCategory? = _uiState.value.filters.ageCategory,
        divisionCategory: DivisionCategory? = _uiState.value.filters.divisionCategory,
        island: Island? = _uiState.value.filters.island
    ) {
        _uiState.update {
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
        _uiState.update {
            it.copy(filters = CompetitionFilters())
        }
    }

    /**
     * Obtiene las competiciones filtradas según los criterios seleccionados
     */
    fun getFilteredCompetitions(): List<Competition> {
        val filters = _uiState.value.filters

        return _uiState.value.competitions.filter { competition ->
            (filters.ageCategory == null || competition.ageCategory == filters.ageCategory) &&
                    (filters.divisionCategory == null || competition.divisionCategory == filters.divisionCategory) &&
                    (filters.island == null || competition.island == filters.island)
        }
    }

    /**
     * Obtiene los favoritos filtrados según el tipo seleccionado
     */
    fun getFilteredFavorites(): List<Favorite> {
        return when (_uiState.value.selectedFavoriteType) {
            FavoriteType.ALL -> _uiState.value.favorites
            FavoriteType.COMPETITIONS -> _uiState.value.favorites.filterIsInstance<Favorite.CompetitionFavorite>()
            FavoriteType.TEAMS -> _uiState.value.favorites.filterIsInstance<Favorite.TeamFavorite>()
            FavoriteType.WRESTLERS -> _uiState.value.favorites.filterIsInstance<Favorite.WrestlerFavorite>()
        }
    }

    /**
     * Obtiene los últimos enfrentamientos de un equipo
     */
    fun getTeamLastMatches(teamId: String): List<Match> {
        if (!teamMatchesCache.containsKey(teamId)) {
            viewModelScope.launch {
                try {
                    teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
                } catch (e: Exception) {
                    // Manejar error
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
            viewModelScope.launch {
                try {
                    teamMatchesCache[teamId] = getTeamMatchesUseCase(teamId)
                } catch (e: Exception) {
                    // Manejar error
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
            viewModelScope.launch {
                try {
                    wrestlerResultsCache[wrestlerId] = getWrestlerResultsUseCase(wrestlerId)
                } catch (e: Exception) {
                    // Manejar error
                }
            }
        }
        return wrestlerResultsCache[wrestlerId] ?: emptyList()
    }
}
package org.iesharia.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.iesharia.domain.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * ViewModel para la pantalla de inicio con soporte para datos de enfrentamientos
 */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Mapas para almacenar datos de enfrentamientos
    private val teamMatchesMap = mutableMapOf<String, Pair<List<Match>, List<Match>>>()
    private val wrestlerResultsMap = mutableMapOf<String, List<WrestlerMatchResult>>()

    init {
        loadData()
    }

    /**
     * Carga los datos iniciales
     */
    private fun loadData() {
        viewModelScope.launch {
            // Simulamos carga de datos
            delay(1000)

            // Datos de prueba
            val mockData = createMockData()

            // Preparamos los mapas para acceso rápido
            mockData.teams.forEach { team ->
                teamMatchesMap[team.id] = getTeamMatches(team, mockData.competitions)
            }

            mockData.wrestlers.forEach { wrestler ->
                wrestlerResultsMap[wrestler.id] = generateWrestlerResults(wrestler, mockData)
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    favorites = mockData.favorites,
                    competitions = mockData.competitions
                )
            }
        }
    }

    /**
     * Obtiene los enfrentamientos de un equipo (últimos y próximos)
     */
    private fun getTeamMatches(team: Team, competitions: List<Competition>): Pair<List<Match>, List<Match>> {
        val completedMatches = mutableListOf<Match>()
        val upcomingMatches = mutableListOf<Match>()

        // Recorremos todas las competiciones y sus jornadas
        competitions.forEach { competition ->
            competition.matchDays.forEach { matchDay ->
                matchDay.matches.forEach { match ->
                    // Si el equipo participa en el enfrentamiento
                    if (match.localTeam.id == team.id || match.visitorTeam.id == team.id) {
                        if (match.completed) {
                            completedMatches.add(match)
                        } else {
                            upcomingMatches.add(match)
                        }
                    }
                }
            }
        }

        // Ordenamos los completados por fecha (más recientes primero)
        val sortedCompleted = completedMatches.sortedByDescending { it.date }

        // Ordenamos los próximos por fecha (más cercanos primero)
        val sortedUpcoming = upcomingMatches.sortedBy { it.date }

        return Pair(sortedCompleted, sortedUpcoming)
    }

    /**
     * Genera resultados ficticios para los luchadores
     */
    private fun generateWrestlerResults(
        wrestler: Wrestler,
        mockData: MockData
    ): List<WrestlerMatchResult> {
        // Aquí simularíamos un repositorio real que obtiene resultados de enfrentamientos
        // Para este ejemplo, generamos datos de prueba

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val results = mutableListOf<WrestlerMatchResult>()

        // Encontramos el equipo del luchador
        val team = mockData.teams.find { it.id == wrestler.teamId } ?: return emptyList()

        // Encontramos los enfrentamientos del equipo
        val teamMatches = teamMatchesMap[team.id]?.first ?: return emptyList()

        // Generamos resultados de enfrentamientos para el luchador
        teamMatches.take(3).forEachIndexed { index, match ->
            // Determinamos si es local o visitante
            val isLocal = match.localTeam.id == team.id
            val opponentTeam = if (isLocal) match.visitorTeam else match.localTeam

            // Buscamos un luchador oponente (o generamos uno ficticio)
            val opponent = mockData.wrestlers.find { it.teamId == opponentTeam.id }
                ?: Wrestler(
                    id = "wrestler_opp${index}",
                    name = "Luchador",
                    surname = "Oponente ${index+1}",
                    imageUrl = null,
                    teamId = opponentTeam.id,
                    position = "Central"
                )

            // Determinamos el resultado
            val result = when {
                !match.completed -> WrestlerMatchResult.Result.DRAW
                index % 3 == 0 -> WrestlerMatchResult.Result.WIN
                index % 3 == 1 -> WrestlerMatchResult.Result.LOSS
                else -> WrestlerMatchResult.Result.EXPELLED
            }

            // Añadimos el resultado
            results.add(
                WrestlerMatchResult(
                    opponentName = "${opponent.name} ${opponent.surname}",
                    result = result,
                    fouls = index % 3,
                    opponentFouls = (index + 1) % 3,
                    category = if ((wrestler.weight ?: 0) > 80) "Pesado" else "Ligero",
                    classification = "Categoría ${('A'.code + index % 3).toChar()}",
                    date = match.date.format(dateFormatter)
                )
            )
        }

        return results
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
        return teamMatchesMap[teamId]?.first ?: emptyList()
    }

    /**
     * Obtiene los próximos enfrentamientos de un equipo
     */
    fun getTeamNextMatches(teamId: String): List<Match> {
        return teamMatchesMap[teamId]?.second ?: emptyList()
    }

    /**
     * Obtiene los resultados de enfrentamientos de un luchador
     */
    fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> {
        return wrestlerResultsMap[wrestlerId] ?: emptyList()
    }

    /**
     * Crea datos de prueba para la aplicación
     */
    private fun createMockData(): MockData {
        // Equipos
        val teamTenerife1 = Team(
            id = "team1",
            name = "C.L. Santa Cruz",
            imageUrl = "",
            island = Island.TENERIFE
        )

        val teamTenerife2 = Team(
            id = "team2",
            name = "C.L. Tegueste",
            imageUrl = "",
            island = Island.TENERIFE
        )

        val teamGranCanaria1 = Team(
            id = "team3",
            name = "C.L. Las Palmas",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        )

        val teamGranCanaria2 = Team(
            id = "team4",
            name = "C.L. Vecindario",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        )

        val teamLanzarote1 = Team(
            id = "team5",
            name = "C.L. Arrecife",
            imageUrl = "",
            island = Island.LANZAROTE
        )

        val teamLanzarote2 = Team(
            id = "team6",
            name = "C.L. Teguise",
            imageUrl = "",
            island = Island.LANZAROTE
        )

        // Lista de equipos
        val teams = listOf(
            teamTenerife1, teamTenerife2, teamGranCanaria1,
            teamGranCanaria2, teamLanzarote1, teamLanzarote2
        )

        // Luchadores
        val wrestler1 = Wrestler(
            id = "wrestler1",
            name = "Pedro",
            surname = "Hernández",
            imageUrl = null,
            teamId = teamTenerife1.id,
            position = "Destacado",
            weight = 85,
            height = 182,
            birthYear = 1995,
            achievements = listOf(
                "Campeón Regional 2023",
                "Subcampeón Insular 2022"
            )
        )

        val wrestler2 = Wrestler(
            id = "wrestler2",
            name = "Juan",
            surname = "García",
            imageUrl = null,
            teamId = teamGranCanaria1.id,
            position = "Puntero",
            weight = 78,
            height = 175,
            birthYear = 1998
        )

        val wrestler3 = Wrestler(
            id = "wrestler3",
            name = "Miguel",
            surname = "Rodríguez",
            imageUrl = null,
            teamId = teamLanzarote1.id,
            position = "Central",
            weight = 92,
            height = 188,
            birthYear = 1990,
            achievements = listOf(
                "Campeón Insular 2023"
            )
        )

        // Lista de luchadores
        val wrestlers = listOf(wrestler1, wrestler2, wrestler3)

        // Enfrentamientos para jornada 1
        val match1 = Match(
            id = "match1",
            localTeam = teamTenerife1,
            visitorTeam = teamGranCanaria1,
            localScore = 12,
            visitorScore = 10,
            date = LocalDateTime.now().minusDays(14),
            venue = "Terrero de Santa Cruz",
            completed = true
        )

        val match2 = Match(
            id = "match2",
            localTeam = teamLanzarote1,
            visitorTeam = teamTenerife2,
            localScore = 8,
            visitorScore = 12,
            date = LocalDateTime.now().minusDays(13),
            venue = "Terrero de Arrecife",
            completed = true
        )

        // Enfrentamientos para jornada 2
        val match3 = Match(
            id = "match3",
            localTeam = teamGranCanaria2,
            visitorTeam = teamLanzarote2,
            localScore = 12,
            visitorScore = 12, // Empate
            date = LocalDateTime.now().minusDays(7),
            venue = "Terrero de Vecindario",
            completed = true
        )

        val match4 = Match(
            id = "match4",
            localTeam = teamTenerife2,
            visitorTeam = teamGranCanaria1,
            localScore = 8,
            visitorScore = 10,
            date = LocalDateTime.now().minusDays(6),
            venue = "Terrero de Tegueste",
            completed = true
        )

        // Enfrentamientos para próxima jornada
        val match5 = Match(
            id = "match5",
            localTeam = teamLanzarote2,
            visitorTeam = teamTenerife1,
            date = LocalDateTime.now().plusDays(3),
            venue = "Terrero de Teguise",
            completed = false
        )

        val match6 = Match(
            id = "match6",
            localTeam = teamGranCanaria1,
            visitorTeam = teamLanzarote1,
            date = LocalDateTime.now().plusDays(4),
            venue = "Terrero de Las Palmas",
            completed = false
        )

        // Jornadas
        val matchDay1 = MatchDay(
            id = "matchday1",
            number = 1,
            matches = listOf(match1, match2),
            competitionId = "comp1"
        )

        val matchDay2 = MatchDay(
            id = "matchday2",
            number = 2,
            matches = listOf(match3, match4),
            competitionId = "comp1"
        )

        val matchDay3 = MatchDay(
            id = "matchday3",
            number = 3,
            matches = listOf(match5, match6),
            competitionId = "comp1"
        )

        // Competiciones
        val competitionLigaTenerife = Competition(
            id = "comp1",
            name = "Liga Insular de Tenerife",
            ageCategory = AgeCategory.REGIONAL,
            divisionCategory = DivisionCategory.PRIMERA,
            island = Island.TENERIFE,
            season = "2024-2025",
            matchDays = listOf(matchDay1, matchDay2, matchDay3),
            teams = listOf(teamTenerife1, teamTenerife2, teamGranCanaria1, teamLanzarote1, teamGranCanaria2, teamLanzarote2)
        )

        val competitionLigaGranCanaria = Competition(
            id = "comp2",
            name = "Liga Insular de Gran Canaria",
            ageCategory = AgeCategory.REGIONAL,
            divisionCategory = DivisionCategory.PRIMERA,
            island = Island.GRAN_CANARIA,
            season = "2024-2025",
            matchDays = emptyList(),
            teams = listOf(teamGranCanaria1, teamGranCanaria2)
        )

        val competitionJuvenilTenerife = Competition(
            id = "comp3",
            name = "Liga Juvenil de Tenerife",
            ageCategory = AgeCategory.JUVENIL,
            divisionCategory = DivisionCategory.PRIMERA,
            island = Island.TENERIFE,
            season = "2024-2025",
            matchDays = emptyList(),
            teams = listOf(teamTenerife1, teamTenerife2)
        )

        // Lista de competiciones
        val competitions = listOf(
            competitionLigaTenerife,
            competitionLigaGranCanaria,
            competitionJuvenilTenerife
        )

        // Favoritos
        val favoriteTeam = Favorite.TeamFavorite(teamTenerife1)
        val favoriteCompetition = Favorite.CompetitionFavorite(competitionLigaTenerife)
        val favoriteWrestler = Favorite.WrestlerFavorite(wrestler1)

        return MockData(
            favorites = listOf(favoriteTeam, favoriteCompetition, favoriteWrestler),
            competitions = competitions,
            teams = teams,
            wrestlers = wrestlers
        )
    }

    /**
     * Clase auxiliar para los datos de prueba
     */
    private data class MockData(
        val favorites: List<Favorite>,
        val competitions: List<Competition>,
        val teams: List<Team>,
        val wrestlers: List<Wrestler>
    )
}
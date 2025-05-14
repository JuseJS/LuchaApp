package org.iesharia.features.competitions.data.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Función de extensión para añadir/restar días a un LocalDateTime
 */
private fun LocalDateTime.plusDays(days: Int): LocalDateTime {
    // Primero modificamos la fecha y mantenemos la misma hora
    val newDate = this.date.plus(DatePeriod(days = days))
    // Creamos un nuevo LocalDateTime con la fecha modificada y la misma hora
    return LocalDateTime(newDate, this.time)
}

class MockCompetitionRepository : CompetitionRepository {
    private val mockData = createMockData()
    private val teamMatchesMap = mutableMapOf<String, Pair<List<Match>, List<Match>>>()
    private val wrestlerResultsMap = mutableMapOf<String, List<WrestlerMatchResult>>()

    init {
        // Preparamos los mapas para acceso rápido
        mockData.teams.forEach { team ->
            teamMatchesMap[team.id] = getTeamMatches(team, mockData.competitions)
        }

        mockData.wrestlers.forEach { wrestler ->
            wrestlerResultsMap[wrestler.id] = generateWrestlerResults(wrestler, mockData)
        }
    }

    override suspend fun getCompetitions(): List<Competition> = mockData.competitions

    override suspend fun getCompetition(id: String): Competition? =
        mockData.competitions.find { it.id == id }

    override suspend fun getFavorites(): List<Favorite> = mockData.favorites

    override suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>> =
        teamMatchesMap[teamId] ?: Pair(emptyList(), emptyList())

    override suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> =
        wrestlerResultsMap[wrestlerId] ?: emptyList()

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
     * Genera resultados para los luchadores
     */
    private fun generateWrestlerResults(
        wrestler: Wrestler,
        mockData: MockData
    ): List<WrestlerMatchResult> {
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
                else -> if (index % 2 == 0) WrestlerMatchResult.Result.SEPARATED else WrestlerMatchResult.Result.EXPELLED
            }

            // Determinamos la categoría correcta (Regional o Juvenil)
            val categoriaEdad = if ((wrestler.birthYear ?: 2000) >= 2006) "Juvenil" else "Regional"

            // Determinamos la clasificación
            val clasificacion = wrestler.position

            // Añadimos el resultado
            results.add(
                WrestlerMatchResult(
                    opponentName = "${opponent.name} ${opponent.surname}",
                    result = result,
                    fouls = index % 3,
                    opponentFouls = (index + 1) % 3,
                    category = categoriaEdad,
                    classification = clasificacion,
                    date = match.date.toString().split("T")[0]
                )
            )
        }
        return results
    }

    /**
     * Crea datos realistas para la aplicación
     */
    private fun createMockData(): MockData {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Equipos
        val teamTenerife1 = Team(
            id = "team1",
            name = "C.L. Tegueste",
            imageUrl = "",
            island = Island.TENERIFE
        )

        val teamTenerife2 = Team(
            id = "team2",
            name = "C.L. Victoria",
            imageUrl = "",
            island = Island.TENERIFE
        )

        val teamGranCanaria1 = Team(
            id = "team3",
            name = "C.L. Unión Sardina",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        )

        val teamGranCanaria2 = Team(
            id = "team4",
            name = "C.L. Adargoma",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        )

        val teamLanzarote1 = Team(
            id = "team5",
            name = "C.L. Tao",
            imageUrl = "",
            island = Island.LANZAROTE
        )

        val teamLanzarote2 = Team(
            id = "team6",
            name = "C.L. Tinajo",
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
            name = "Eusebio",
            surname = "Ledesma",
            imageUrl = null,
            teamId = teamTenerife1.id,
            position = "Destacado A",
            weight = 85,
            height = 182,
            birthYear = 1995,
            achievements = emptyList()
        )

        val wrestler2 = Wrestler(
            id = "wrestler2",
            name = "Fabián",
            surname = "Rocha",
            imageUrl = null,
            teamId = teamGranCanaria1.id,
            position = "Puntal B",
            weight = 78,
            height = 175,
            birthYear = 1998,
            achievements = emptyList()
        )

        val wrestler3 = Wrestler(
            id = "wrestler3",
            name = "Cristian",
            surname = "Pérez",
            imageUrl = null,
            teamId = teamLanzarote1.id,
            position = "Puntal A",
            weight = 92,
            height = 188,
            birthYear = 1990,
            achievements = emptyList()
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
            date = now.plusDays(-14),
            venue = "Terrero de Tegueste",
            completed = true
        )

        val match2 = Match(
            id = "match2",
            localTeam = teamLanzarote1,
            visitorTeam = teamTenerife2,
            localScore = 8,
            visitorScore = 12,
            date = now.plusDays(-13),
            venue = "Terrero de Tao",
            completed = true
        )

        // Enfrentamientos para jornada 2
        val match3 = Match(
            id = "match3",
            localTeam = teamGranCanaria2,
            visitorTeam = teamLanzarote2,
            localScore = 12,
            visitorScore = 12,
            date = now.plusDays(-7),
            venue = "Terrero de Adargoma",
            completed = true
        )

        val match4 = Match(
            id = "match4",
            localTeam = teamTenerife2,
            visitorTeam = teamGranCanaria1,
            localScore = 8,
            visitorScore = 10,
            date = now.plusDays(-6),
            venue = "Terrero de La Victoria",
            completed = true
        )

        // Enfrentamientos para próxima jornada
        val match5 = Match(
            id = "match5",
            localTeam = teamLanzarote2,
            visitorTeam = teamTenerife1,
            date = now.plusDays(3),
            venue = "Terrero de Tinajo",
            completed = false
        )

        val match6 = Match(
            id = "match6",
            localTeam = teamGranCanaria1,
            visitorTeam = teamLanzarote1,
            date = now.plusDays(4),
            venue = "Terrero de Sardina",
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
package org.iesharia.core.data.mock

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

/**
 * Generador centralizado de datos mock para toda la aplicación
 */
class MockDataGenerator {
    // Extender LocalDateTime para añadir/restar días
    private fun LocalDateTime.plusDays(days: Int): LocalDateTime {
        val newDate = this.date.plus(DatePeriod(days = days))
        return LocalDateTime(newDate, this.time)
    }

    /**
     * Datos generados y almacenados para reutilización
     */
    private val _teams = mutableListOf<Team>()
    private val _wrestlers = mutableListOf<Wrestler>()
    private val _competitions = mutableListOf<Competition>()
    private val _favorites = mutableListOf<Favorite>()
    private val _teamMatchesMap = mutableMapOf<String, Pair<List<Match>, List<Match>>>()
    private val _wrestlerResultsMap = mutableMapOf<String, List<WrestlerMatchResult>>()

    // Propiedades de acceso público
    val teams: List<Team> get() = _teams
    val wrestlers: List<Wrestler> get() = _wrestlers
    val competitions: List<Competition> get() = _competitions
    val favorites: List<Favorite> get() = _favorites

    // Métodos para obtener datos específicos
    fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>> =
        _teamMatchesMap[teamId] ?: Pair(emptyList(), emptyList())

    fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> =
        _wrestlerResultsMap[wrestlerId] ?: emptyList()

    /**
     * Inicializa todos los datos mock
     */
    fun generateAllData() {
        // Limpiar datos anteriores
        _teams.clear()
        _wrestlers.clear()
        _competitions.clear()
        _favorites.clear()
        _teamMatchesMap.clear()
        _wrestlerResultsMap.clear()

        // Generar datos en orden adecuado
        generateTeams()
        generateWrestlers()
        generateCompetitions()
        generateFavorites()

        // Generar datos relacionados
        generateTeamMatches()
        generateWrestlerResults()
    }

    /**
     * Genera equipos mock para diferentes islas
     */
    private fun generateTeams() {
        // Equipos de Tenerife
        _teams.add(Team(
            id = "team1",
            name = "C.L. Tegueste",
            imageUrl = "",
            island = Island.TENERIFE
        ))

        _teams.add(Team(
            id = "team2",
            name = "C.L. Victoria",
            imageUrl = "",
            island = Island.TENERIFE
        ))

        // Equipos de Gran Canaria
        _teams.add(Team(
            id = "team3",
            name = "C.L. Unión Sardina",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        ))

        _teams.add(Team(
            id = "team4",
            name = "C.L. Adargoma",
            imageUrl = "",
            island = Island.GRAN_CANARIA
        ))

        // Equipos de Lanzarote
        _teams.add(Team(
            id = "team5",
            name = "C.L. Tao",
            imageUrl = "",
            island = Island.LANZAROTE
        ))

        _teams.add(Team(
            id = "team6",
            name = "C.L. Tinajo",
            imageUrl = "",
            island = Island.LANZAROTE
        ))
    }

    /**
     * Genera luchadores mock asociados a equipos
     */
    private fun generateWrestlers() {
        // Asegurar que haya equipos generados
        if (_teams.isEmpty()) {
            generateTeams()
        }

        // Luchador para el primer equipo de Tenerife
        _wrestlers.add(Wrestler(
            id = "wrestler1",
            name = "Eusebio",
            surname = "Ledesma",
            imageUrl = null,
            teamId = _teams[0].id,
            position = "Destacado A",
            weight = 85,
            height = 182,
            birthYear = 1995,
            achievements = emptyList()
        ))

        // Luchador para el primer equipo de Gran Canaria
        _wrestlers.add(Wrestler(
            id = "wrestler2",
            name = "Fabián",
            surname = "Rocha",
            imageUrl = null,
            teamId = _teams[2].id,
            position = "Puntal B",
            weight = 78,
            height = 175,
            birthYear = 1998,
            achievements = emptyList()
        ))

        // Luchador para el primer equipo de Lanzarote
        _wrestlers.add(Wrestler(
            id = "wrestler3",
            name = "Cristian",
            surname = "Pérez",
            imageUrl = null,
            teamId = _teams[4].id,
            position = "Puntal A",
            weight = 92,
            height = 188,
            birthYear = 1990,
            achievements = emptyList()
        ))
    }

    /**
     * Genera competiciones con jornadas y enfrentamientos
     */
    private fun generateCompetitions() {
        // Asegurar que haya equipos generados
        if (_teams.isEmpty()) {
            generateTeams()
        }

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Enfrentamientos para jornada 1
        val match1 = Match(
            id = "match1",
            localTeam = _teams[0], // Tegueste
            visitorTeam = _teams[2], // Unión Sardina
            localScore = 12,
            visitorScore = 10,
            date = now.plusDays(-14),
            venue = "Terrero de Tegueste",
            completed = true
        )

        val match2 = Match(
            id = "match2",
            localTeam = _teams[4], // Tao
            visitorTeam = _teams[1], // Victoria
            localScore = 8,
            visitorScore = 12,
            date = now.plusDays(-13),
            venue = "Terrero de Tao",
            completed = true
        )

        // Enfrentamientos para jornada 2
        val match3 = Match(
            id = "match3",
            localTeam = _teams[3], // Adargoma
            visitorTeam = _teams[5], // Tinajo
            localScore = 12,
            visitorScore = 12,
            date = now.plusDays(-7),
            venue = "Terrero de Adargoma",
            completed = true
        )

        val match4 = Match(
            id = "match4",
            localTeam = _teams[1], // Victoria
            visitorTeam = _teams[2], // Unión Sardina
            localScore = 8,
            visitorScore = 10,
            date = now.plusDays(-6),
            venue = "Terrero de La Victoria",
            completed = true
        )

        // Enfrentamientos para próxima jornada
        val match5 = Match(
            id = "match5",
            localTeam = _teams[5], // Tinajo
            visitorTeam = _teams[0], // Tegueste
            date = now.plusDays(3),
            venue = "Terrero de Tinajo",
            completed = false
        )

        val match6 = Match(
            id = "match6",
            localTeam = _teams[2], // Unión Sardina
            visitorTeam = _teams[4], // Tao
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
            teams = listOf(_teams[0], _teams[1], _teams[2], _teams[4], _teams[3], _teams[5])
        )

        val competitionLigaGranCanaria = Competition(
            id = "comp2",
            name = "Liga Insular de Gran Canaria",
            ageCategory = AgeCategory.REGIONAL,
            divisionCategory = DivisionCategory.PRIMERA,
            island = Island.GRAN_CANARIA,
            season = "2024-2025",
            matchDays = emptyList(),
            teams = listOf(_teams[2], _teams[3])
        )

        val competitionJuvenilTenerife = Competition(
            id = "comp3",
            name = "Liga Juvenil de Tenerife",
            ageCategory = AgeCategory.JUVENIL,
            divisionCategory = DivisionCategory.PRIMERA,
            island = Island.TENERIFE,
            season = "2024-2025",
            matchDays = emptyList(),
            teams = listOf(_teams[0], _teams[1])
        )

        _competitions.addAll(listOf(
            competitionLigaTenerife,
            competitionLigaGranCanaria,
            competitionJuvenilTenerife
        ))
    }

    /**
     * Genera favoritos para el usuario
     */
    private fun generateFavorites() {
        // Asegurar que existan los datos necesarios
        if (_teams.isEmpty() || _competitions.isEmpty() || _wrestlers.isEmpty()) {
            generateTeams()
            generateWrestlers()
            generateCompetitions()
        }

        // Crear favoritos de diferentes tipos
        val favoriteTeam = Favorite.TeamFavorite(_teams[0])
        val favoriteCompetition = Favorite.CompetitionFavorite(_competitions[0])
        val favoriteWrestler = Favorite.WrestlerFavorite(_wrestlers[0])

        _favorites.addAll(listOf(favoriteTeam, favoriteCompetition, favoriteWrestler))
    }

    /**
     * Genera los enfrentamientos para cada equipo (pasados y futuros)
     */
    private fun generateTeamMatches() {
        // Asegurar que existan competiciones
        if (_competitions.isEmpty()) {
            generateCompetitions()
        }

        // Generar mapas de enfrentamientos por equipo
        _teams.forEach { team ->
            _teamMatchesMap[team.id] = getTeamMatchesFromCompetitions(team)
        }
    }

    /**
     * Genera resultados para cada luchador
     */
    private fun generateWrestlerResults() {
        // Asegurar que existan luchadores y equipos
        if (_wrestlers.isEmpty() || _teamMatchesMap.isEmpty()) {
            generateWrestlers()
            generateTeamMatches()
        }

        // Generar resultados para cada luchador
        _wrestlers.forEach { wrestler ->
            _wrestlerResultsMap[wrestler.id] = generateWrestlerResultsForWrestler(wrestler)
        }
    }

    /**
     * Obtiene los enfrentamientos de un equipo a partir de las competiciones
     */
    private fun getTeamMatchesFromCompetitions(team: Team): Pair<List<Match>, List<Match>> {
        val completedMatches = mutableListOf<Match>()
        val upcomingMatches = mutableListOf<Match>()

        // Buscar enfrentamientos en todas las competiciones
        _competitions.forEach { competition ->
            competition.matchDays.forEach { matchDay ->
                matchDay.matches.forEach { match ->
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

        // Ordenar por fecha
        val sortedCompleted = completedMatches.sortedByDescending { it.date }
        val sortedUpcoming = upcomingMatches.sortedBy { it.date }

        return Pair(sortedCompleted, sortedUpcoming)
    }

    /**
     * Genera resultados de enfrentamientos para un luchador
     */
    private fun generateWrestlerResultsForWrestler(wrestler: Wrestler): List<WrestlerMatchResult> {
        val results = mutableListOf<WrestlerMatchResult>()

        // Encontrar el equipo del luchador
        val team = _teams.find { it.id == wrestler.teamId } ?: return emptyList()

        // Encontrar los enfrentamientos del equipo
        val teamMatches = _teamMatchesMap[team.id]?.first ?: return emptyList()

        // Generar resultados para los últimos enfrentamientos
        teamMatches.take(3).forEachIndexed { index, match ->
            // Determinar si es local o visitante
            val isLocal = match.localTeam.id == team.id
            val opponentTeam = if (isLocal) match.visitorTeam else match.localTeam

            // Buscar un luchador oponente
            val opponent = _wrestlers.find { it.teamId == opponentTeam.id }
                ?: Wrestler(
                    id = "wrestler_opp${index}",
                    name = "Luchador",
                    surname = "Oponente ${index+1}",
                    imageUrl = null,
                    teamId = opponentTeam.id,
                    position = "Central"
                )

            // Determinar el resultado basado en índice para variedad
            val result = when {
                !match.completed -> WrestlerMatchResult.Result.DRAW
                index % 3 == 0 -> WrestlerMatchResult.Result.WIN
                index % 3 == 1 -> WrestlerMatchResult.Result.LOSS
                else -> if (index % 2 == 0) WrestlerMatchResult.Result.SEPARATED else WrestlerMatchResult.Result.EXPELLED
            }

            // Determinar categoría correcta
            val categoriaEdad = if ((wrestler.birthYear ?: 2000) >= 2006) "Juvenil" else "Regional"

            // Añadir el resultado
            results.add(
                WrestlerMatchResult(
                    opponentName = "${opponent.name} ${opponent.surname}",
                    result = result,
                    fouls = index % 3,
                    opponentFouls = (index + 1) % 3,
                    category = categoriaEdad,
                    classification = wrestler.position,
                    date = match.date.toString().split("T")[0]
                )
            )
        }

        return results
    }

    /**
     * Regenera un conjunto específico de datos mock
     */
    fun regenerateSpecificData(
        regenerateTeams: Boolean = false,
        regenerateWrestlers: Boolean = false,
        regenerateCompetitions: Boolean = false,
        regenerateFavorites: Boolean = false
    ) {
        if (regenerateTeams) {
            _teams.clear()
            generateTeams()
        }

        if (regenerateWrestlers) {
            _wrestlers.clear()
            generateWrestlers()
        }

        if (regenerateCompetitions) {
            _competitions.clear()
            generateCompetitions()
        }

        if (regenerateFavorites) {
            _favorites.clear()
            generateFavorites()
        }

        // Siempre regenerar datos relacionados
        generateTeamMatches()
        generateWrestlerResults()
    }
}
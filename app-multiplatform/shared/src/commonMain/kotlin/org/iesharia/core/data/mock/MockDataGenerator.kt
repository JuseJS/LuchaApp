package org.iesharia.core.data.mock

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
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
import org.iesharia.features.wrestlers.domain.model.WrestlerCategory
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification
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
            island = Island.TENERIFE,
            venue = "Terrero de Tegueste",
            divisionCategory = DivisionCategory.PRIMERA
        ))

        _teams.add(Team(
            id = "team2",
            name = "C.L. Victoria",
            imageUrl = "",
            island = Island.TENERIFE,
            venue = "Terrero de La Victoria",
            divisionCategory = DivisionCategory.PRIMERA
        ))

        // Equipos de Gran Canaria
        _teams.add(Team(
            id = "team3",
            name = "C.L. Unión Sardina",
            imageUrl = "",
            island = Island.GRAN_CANARIA,
            venue = "Terrero de Sardina",
            divisionCategory = DivisionCategory.SEGUNDA
        ))

        _teams.add(Team(
            id = "team4",
            name = "C.L. Adargoma",
            imageUrl = "",
            island = Island.GRAN_CANARIA,
            venue = "Terrero de Adargoma",
            divisionCategory = DivisionCategory.SEGUNDA
        ))

        // Equipos de Lanzarote
        _teams.add(Team(
            id = "team5",
            name = "C.L. Tao",
            imageUrl = "",
            island = Island.LANZAROTE,
            venue = "Terrero de Tao",
            divisionCategory = DivisionCategory.TERCERA
        ))

        _teams.add(Team(
            id = "team6",
            name = "C.L. Tinajo",
            imageUrl = "",
            island = Island.LANZAROTE,
            venue = "Terrero de Tinajo",
            divisionCategory = DivisionCategory.TERCERA
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

        // Luchadores para el primer equipo de Tenerife (Team1 - C.L. Tegueste)
        // Regionales
        _wrestlers.add(Wrestler(
            id = "wrestler1",
            licenseNumber = "LCA-001",
            name = "Eusebio",
            surname = "Ledesma",
            imageUrl = null,
            teamId = _teams[0].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_A,
            height = 182,
            weight = 85,
            birthDate = LocalDate(1990, Month.MAY, 15),
            nickname = "El Peruano"
        ))

        _wrestlers.add(Wrestler(
            id = "wrestler2",
            licenseNumber = "LCA-002",
            name = "Antonio",
            surname = "Martín",
            imageUrl = null,
            teamId = _teams[0].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.DESTACADO_A,
            height = 178,
            weight = 82,
            birthDate = LocalDate(1995, Month.JUNE, 21),
            nickname = "Merienda Gallinas"
        ))

        // Juveniles
        _wrestlers.add(Wrestler(
            id = "wrestler3",
            licenseNumber = "LCA-003",
            name = "Miguel",
            surname = "Hernández",
            imageUrl = null,
            teamId = _teams[0].id,
            category = WrestlerCategory.JUVENIL,
            classification = WrestlerClassification.NONE,
            height = 175,
            weight = 68,
            birthDate = LocalDate(2006, Month.AUGUST, 12),
            nickname = null
        ))

        // Cadetes
        _wrestlers.add(Wrestler(
            id = "wrestler4",
            licenseNumber = "LCA-004",
            name = "Pedro",
            surname = "González",
            imageUrl = null,
            teamId = _teams[0].id,
            category = WrestlerCategory.CADETE,
            classification = WrestlerClassification.NONE,
            height = 160,
            weight = 52,
            birthDate = LocalDate(2008, Month.FEBRUARY, 3),
            nickname = null
        ))

        // Luchadores para el segundo equipo de Tenerife (Team2 - C.L. Victoria)
        // Regionales
        _wrestlers.add(Wrestler(
            id = "wrestler5",
            licenseNumber = "LCA-005",
            name = "Juan",
            surname = "Díaz",
            imageUrl = null,
            teamId = _teams[1].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_B,
            height = 180,
            weight = 86,
            birthDate = LocalDate(1992, Month.JANUARY, 18),
            nickname = null
        ))

        _wrestlers.add(Wrestler(
            id = "wrestler6",
            licenseNumber = "LCA-006",
            name = "Marcos",
            surname = "Pérez",
            imageUrl = null,
            teamId = _teams[1].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.DESTACADO_B,
            height = 175,
            weight = 80,
            birthDate = LocalDate(1994, Month.MARCH, 25),
            nickname = null
        ))

        // Juveniles
        _wrestlers.add(Wrestler(
            id = "wrestler7",
            licenseNumber = "LCA-007",
            name = "Carlos",
            surname = "Santana",
            imageUrl = null,
            teamId = _teams[1].id,
            category = WrestlerCategory.JUVENIL,
            classification = WrestlerClassification.NONE,
            height = 170,
            weight = 65,
            birthDate = LocalDate(2007, Month.JULY, 8),
            nickname = null
        ))

        // Luchadores para el primer equipo de Gran Canaria (Team3 - C.L. Unión Sardina)
        // Regionales
        _wrestlers.add(Wrestler(
            id = "wrestler8",
            licenseNumber = "LCA-008",
            name = "Fabián",
            surname = "Rocha",
            imageUrl = null,
            teamId = _teams[2].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_A,
            height = 190,
            weight = 95,
            birthDate = LocalDate(1988, Month.NOVEMBER, 30),
            nickname = null
        ))

        _wrestlers.add(Wrestler(
            id = "wrestler9",
            licenseNumber = "LCA-009",
            name = "Roberto",
            surname = "Alonso",
            imageUrl = null,
            teamId = _teams[2].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.DESTACADO_A,
            height = 178,
            weight = 82,
            birthDate = LocalDate(1993, Month.APRIL, 12),
            nickname = null
        ))

        // Cadetes
        _wrestlers.add(Wrestler(
            id = "wrestler10",
            licenseNumber = "LCA-010",
            name = "Daniel",
            surname = "Medina",
            imageUrl = null,
            teamId = _teams[2].id,
            category = WrestlerCategory.CADETE,
            classification = WrestlerClassification.NONE,
            height = 165,
            weight = 58,
            birthDate = LocalDate(2009, Month.SEPTEMBER, 5),
            nickname = null
        ))

        // Luchadores para el primer equipo de Lanzarote (Team5 - C.L. Tao)
        // Regionales
        _wrestlers.add(Wrestler(
            id = "wrestler11",
            licenseNumber = "LCA-011",
            name = "Cristian",
            surname = "Pérez",
            imageUrl = null,
            teamId = _teams[4].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_B,
            height = 188,
            weight = 92,
            birthDate = LocalDate(1991, Month.OCTOBER, 7),
            nickname = null
        ))

        _wrestlers.add(Wrestler(
            id = "wrestler12",
            licenseNumber = "LCA-012",
            name = "Alejandro",
            surname = "García",
            imageUrl = null,
            teamId = _teams[4].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.DESTACADO_C,
            height = 176,
            weight = 78,
            birthDate = LocalDate(1996, Month.DECEMBER, 14),
            nickname = null
        ))

        // Juveniles
        _wrestlers.add(Wrestler(
            id = "wrestler13",
            licenseNumber = "LCA-013",
            name = "Francisco",
            surname = "Ortega",
            imageUrl = null,
            teamId = _teams[4].id,
            category = WrestlerCategory.JUVENIL,
            classification = WrestlerClassification.NONE,
            height = 172,
            weight = 67,
            birthDate = LocalDate(2006, Month.MARCH, 23),
            nickname = null
        ))

        // Añadir al menos un luchador para cada equipo restante
        _wrestlers.add(Wrestler(
            id = "wrestler14",
            licenseNumber = "LCA-014",
            name = "José",
            surname = "Medina",
            imageUrl = null,
            teamId = _teams[3].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_C,
            height = 183,
            weight = 89,
            birthDate = LocalDate(1990, Month.AUGUST, 9),
            nickname = null
        ))

        _wrestlers.add(Wrestler(
            id = "wrestler15",
            licenseNumber = "LCA-015",
            name = "Manuel",
            surname = "Suárez",
            imageUrl = null,
            teamId = _teams[5].id,
            category = WrestlerCategory.REGIONAL,
            classification = WrestlerClassification.PUNTAL_C,
            height = 179,
            weight = 84,
            birthDate = LocalDate(1993, Month.JUNE, 17),
            nickname = null
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
            id = "match1",  // ID explícito
            localTeam = _teams[0], // Tegueste
            visitorTeam = _teams[2], // Unión Sardina
            localScore = 12,
            visitorScore = 10,
            date = now.plusDays(-14),
            venue = _teams[0].venue, // Usar el terrero del equipo local
            completed = true
        )

        val match2 = Match(
            id = "match2",
            localTeam = _teams[4], // Tao
            visitorTeam = _teams[1], // Victoria
            localScore = 8,
            visitorScore = 12,
            date = now.plusDays(-13),
            venue = _teams[4].venue, // Usar el terrero del equipo local
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
            venue = _teams[3].venue, // Usar el terrero del equipo local
            completed = true
        )

        val match4 = Match(
            id = "match4",
            localTeam = _teams[1], // Victoria
            visitorTeam = _teams[2], // Unión Sardina
            localScore = 8,
            visitorScore = 10,
            date = now.plusDays(-6),
            venue = _teams[1].venue, // Usar el terrero del equipo local
            completed = true
        )

        // Enfrentamientos para próxima jornada
        val match5 = Match(
            id = "match5",
            localTeam = _teams[5], // Tinajo
            visitorTeam = _teams[0], // Tegueste
            date = now.plusDays(3),
            venue = _teams[5].venue, // Usar el terrero del equipo local
            completed = false
        )

        val match6 = Match(
            id = "match6",
            localTeam = _teams[2], // Unión Sardina
            visitorTeam = _teams[4], // Tao
            date = now.plusDays(4),
            venue = _teams[2].venue, // Usar el terrero del equipo local
            completed = false
        )

        // Jornadas
        val matchDay1 = MatchDay(
            id = "matchday1",
            number = 1,
            matches = listOf(match1, match2),
            competitionId = "comp1",
            ended = true
        )

        val matchDay2 = MatchDay(
            id = "matchday2",
            number = 2,
            matches = listOf(match3, match4),
            competitionId = "comp1",
            ended = true
        )

        val matchDay3 = MatchDay(
            id = "matchday3",
            number = 3,
            matches = listOf(match5, match6),
            competitionId = "comp1",
            ended = false
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
                    licenseNumber = "LCA-X${index+1}",
                    name = "Luchador",
                    surname = "Oponente ${index+1}",
                    imageUrl = null,
                    teamId = opponentTeam.id,
                    category = wrestler.category,
                    classification = if (wrestler.category == WrestlerCategory.REGIONAL)
                        WrestlerClassification.entries.random() else WrestlerClassification.NONE,
                    height = 175,
                    weight = 80,
                    birthDate = LocalDate(1995, Month.JANUARY, 1),
                    nickname = "Cosivo"
                )

            // Determinar el resultado basado en índice para variedad
            val result = when {
                !match.completed -> WrestlerMatchResult.Result.DRAW
                index % 3 == 0 -> WrestlerMatchResult.Result.WIN
                index % 3 == 1 -> WrestlerMatchResult.Result.LOSS
                else -> if (index % 2 == 0) WrestlerMatchResult.Result.SEPARATED else WrestlerMatchResult.Result.EXPELLED
            }

            // Determinar categoría correcta según la fecha de nacimiento del luchador
            val categoriaEdad = wrestler.category.displayName()

            // Añadir el resultado
            results.add(
                WrestlerMatchResult(
                    opponentName = "${opponent.name} ${opponent.surname}",
                    result = result,
                    fouls = index % 3,
                    opponentFouls = (index + 1) % 3,
                    category = categoriaEdad,
                    classification = wrestler.classification.displayName(),
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
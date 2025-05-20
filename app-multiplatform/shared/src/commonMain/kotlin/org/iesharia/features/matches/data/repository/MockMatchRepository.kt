package org.iesharia.features.matches.data.repository

import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.AppError
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.model.WrestlerClassification

class MockMatchRepository(
    private val mockDataGenerator: MockDataGenerator
) : MatchRepository {

    override suspend fun getMatch(matchId: String): Match? {
        println("Buscando match con ID: $matchId")

        try {
            // Primero busca el enfrentamiento y guarda resultados intermedios
            val allMatches = mockDataGenerator.competitions.flatMap { competition ->
                competition.matchDays.flatMap { matchDay ->
                    matchDay.matches
                }
            }

            println("Total de matches encontrados: ${allMatches.size}")

            // Busca el match específico e imprime información
            val match = allMatches.find { it.id == matchId }
            if (match != null) {
                println("Match encontrado: ${match.localTeam.name} vs ${match.visitorTeam.name}")
            } else {
                println("No se encontró match con ID: $matchId")
                return allMatches.firstOrNull()
            }

            return match
        } catch (e: Exception) {
            println("Error al buscar match: ${e.message}")
            throw AppError.ServerError(message = "Error al obtener enfrentamiento: ${e.message}")
        }
    }

    override suspend fun getMatchDetails(matchId: String): Triple<Match, List<Wrestler>, List<Wrestler>> {
        val match = getMatch(matchId) ?: throw AppError.UnknownError(message = "No se encontró el enfrentamiento")

        // Obtener luchadores de cada equipo
        val localTeamWrestlers = mockDataGenerator.wrestlers.filter { it.teamId == match.localTeam.id }
            .sortedBy { wrestler ->
                // Ordenar por clasificación (Puntales primero, luego Destacados, etc.)
                WrestlerClassification.getOrderedValues().indexOf(wrestler.classification)
            }

        val visitorTeamWrestlers = mockDataGenerator.wrestlers.filter { it.teamId == match.visitorTeam.id }
            .sortedBy { wrestler ->
                WrestlerClassification.getOrderedValues().indexOf(wrestler.classification)
            }

        return Triple(match, localTeamWrestlers, visitorTeamWrestlers)
    }

    override suspend fun getMatchReferees(matchId: String): Pair<String, List<String>> {
        // Simulamos datos de árbitros (en una implementación real, vendrían de una API o base de datos)
        return Pair(
            "Juan Rodríguez Pérez", // Árbitro principal
            listOf("Antonio González", "María Fernández") // Árbitros asistentes
        )
    }

    override suspend fun getMatchStatistics(matchId: String): Map<String, Any> {
        // Simulamos estadísticas (en una implementación real, vendrían de una API o base de datos)
        return mapOf(
            "localTeamWins" to 7,
            "visitorTeamWins" to 5,
            "draws" to 0,
            "separations" to 2,
            "expulsions" to 0,
            "totalGrips" to 14,
            "duration" to "01:35",
            "spectators" to 350
        )
    }
}
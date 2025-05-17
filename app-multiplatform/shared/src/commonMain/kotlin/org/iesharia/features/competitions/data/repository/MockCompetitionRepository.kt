package org.iesharia.features.competitions.data.repository

import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

class MockCompetitionRepository(
    private val mockDataGenerator: MockDataGenerator
) : CompetitionRepository {

    init {
        // Inicializar datos mock
        try {
            mockDataGenerator.generateAllData()
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al generar datos mock: ${e.message}")
        }
    }

    override suspend fun getCompetitions(): List<Competition> {
        return try {
            mockDataGenerator.competitions
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener competiciones: ${e.message}")
        }
    }

    override suspend fun getCompetition(id: String): Competition? {
        return try {
            mockDataGenerator.competitions.find { it.id == id }
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener competición: ${e.message}")
        }
    }

    override suspend fun getFavorites(): List<Favorite> {
        return try {
            mockDataGenerator.favorites
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener favoritos: ${e.message}")
        }
    }

    override suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>> {
        return try {
            mockDataGenerator.getTeamMatches(teamId)
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener enfrentamientos: ${e.message}")
        }
    }

    override suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> {
        return try {
            mockDataGenerator.getWrestlerResults(wrestlerId)
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener resultados: ${e.message}")
        }
    }
}
package org.iesharia.features.wrestlers.data.repository

import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.AppError
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository

class MockWrestlerRepository(
    private val mockDataGenerator: MockDataGenerator
) : WrestlerRepository {

    override suspend fun getWrestlers(): List<Wrestler> {
        return try {
            mockDataGenerator.wrestlers
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener luchadores: ${e.message}")
        }
    }

    override suspend fun getWrestlersByTeamId(teamId: String): List<Wrestler> {
        return try {
            mockDataGenerator.wrestlers.filter { it.teamId == teamId }
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener luchadores del equipo: ${e.message}")
        }
    }

    override suspend fun getWrestler(id: String): Wrestler? {
        return try {
            mockDataGenerator.wrestlers.find { it.id == id }
        } catch (e: Exception) {
            throw AppError.ServerError(message = "Error al obtener luchador: ${e.message}")
        }
    }
}
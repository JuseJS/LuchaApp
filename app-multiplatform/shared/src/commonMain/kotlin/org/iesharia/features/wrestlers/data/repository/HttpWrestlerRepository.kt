package org.iesharia.features.wrestlers.data.repository

import org.iesharia.core.domain.model.AppError
import org.iesharia.core.network.mapper.WrestlerMappers.toDomain
import org.iesharia.core.network.service.WrestlerService
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository

class HttpWrestlerRepository(
    private val wrestlerService: WrestlerService
) : WrestlerRepository {
    
    override suspend fun getWrestlers(): List<Wrestler> {
        return try {
            val wrestlers = wrestlerService.getAllWrestlers()
            wrestlers.map { it.toDomain() }
        } catch (e: Exception) {
            throw AppError.NetworkError("Failed to fetch wrestlers: ${e.message}")
        }
    }
    
    override suspend fun getWrestler(id: String): Wrestler? {
        return try {
            val wrestlerDto = wrestlerService.getWrestlerById(id)
            wrestlerDto.toDomain()
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getWrestlersByTeamId(teamId: String): List<Wrestler> {
        return try {
            val wrestlers = wrestlerService.getWrestlersByTeamId(teamId)
            wrestlers.map { it.toDomain() }
        } catch (e: Exception) {
            throw AppError.NetworkError("Failed to fetch team wrestlers: ${e.message}")
        }
    }
    
    // Additional method for extended functionality with filters
    suspend fun getAllWrestlersWithFilters(
        teamId: String? = null,
        category: String? = null,
        classification: String? = null,
        island: String? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<Wrestler> {
        return try {
            val wrestlers = wrestlerService.getAllWrestlers(
                teamId = teamId,
                category = category,
                classification = classification,
                island = island,
                search = search,
                page = page,
                size = size
            )
            wrestlers.map { it.toDomain() }
        } catch (e: Exception) {
            throw AppError.NetworkError("Failed to fetch wrestlers: ${e.message}")
        }
    }
}
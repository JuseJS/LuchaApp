package org.iesharia.data.repositories

import org.iesharia.data.models.WrestlerDocument
import org.iesharia.domain.models.dto.PagedResponse
import org.iesharia.domain.models.dto.WrestlerFilters

interface WrestlerRepository {
    suspend fun findById(id: String): WrestlerDocument?
    suspend fun findByLicenseNumber(licenseNumber: String): WrestlerDocument?
    suspend fun findByTeamId(teamId: String): List<WrestlerDocument>
    suspend fun findAll(): List<WrestlerDocument>
    suspend fun findAll(filters: WrestlerFilters): PagedResponse<WrestlerDocument>
    suspend fun search(query: String): List<WrestlerDocument>
    suspend fun create(wrestler: WrestlerDocument): WrestlerDocument
    suspend fun update(id: String, wrestler: WrestlerDocument): WrestlerDocument?
    suspend fun delete(id: String): Boolean
    suspend fun existsByLicenseNumber(licenseNumber: String): Boolean
    suspend fun existsByDni(dni: String): Boolean
    suspend fun countByTeamId(teamId: String): Long
}
package org.iesharia.data.repositories

import org.iesharia.data.models.CompetitionDocument

interface CompetitionRepository {
    suspend fun findAll(): List<CompetitionDocument>
    suspend fun findById(id: String): CompetitionDocument?
    suspend fun findByFilters(
        ageCategory: String? = null,
        divisionCategory: String? = null,
        island: String? = null,
        season: String? = null
    ): List<CompetitionDocument>
    suspend fun create(competition: CompetitionDocument): CompetitionDocument
    suspend fun update(id: String, competition: CompetitionDocument): CompetitionDocument?
    suspend fun delete(id: String): Boolean
}
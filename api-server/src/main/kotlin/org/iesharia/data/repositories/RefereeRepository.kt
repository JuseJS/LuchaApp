package org.iesharia.data.repositories

import org.iesharia.data.models.RefereeDocument

interface RefereeRepository {
    suspend fun findAll(): List<RefereeDocument>
    suspend fun findById(id: String): RefereeDocument?
    suspend fun findActive(): List<RefereeDocument>
    suspend fun create(referee: RefereeDocument): RefereeDocument
    suspend fun update(id: String, referee: RefereeDocument): RefereeDocument?
    suspend fun delete(id: String): Boolean
}
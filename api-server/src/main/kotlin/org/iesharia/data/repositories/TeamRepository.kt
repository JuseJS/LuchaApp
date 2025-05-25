package org.iesharia.data.repositories

import org.iesharia.data.models.TeamDocument

interface TeamRepository {
    suspend fun findAll(): List<TeamDocument>
    suspend fun findById(id: String): TeamDocument?
    suspend fun findByName(name: String): TeamDocument?
    suspend fun findByDivision(division: String): List<TeamDocument>
    suspend fun search(query: String): List<TeamDocument>
    suspend fun create(team: TeamDocument): TeamDocument
    suspend fun update(id: String, team: TeamDocument): TeamDocument?
    suspend fun delete(id: String): Boolean
    suspend fun existsByName(name: String): Boolean
}
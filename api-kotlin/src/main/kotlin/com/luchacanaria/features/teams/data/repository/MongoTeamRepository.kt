package com.luchacanaria.features.teams.data.repository

import com.luchacanaria.core.database.DatabaseConfig
import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory
import com.luchacanaria.features.teams.data.document.TeamDocument
import com.luchacanaria.features.teams.data.mapper.TeamMapper
import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.features.teams.domain.repository.TeamRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MongoTeamRepository : TeamRepository {
    private val collection = DatabaseConfig.teamsCollection

    override suspend fun getAllTeams(
        island: Island?,
        divisionCategory: DivisionCategory?,
        search: String?,
        page: Int,
        size: Int
    ): List<Team> {
        val filters = mutableListOf<org.bson.conversions.Bson>()
        filters.add(Filters.eq(TeamDocument::isActive.name, true))
        
        island?.let {
            filters.add(Filters.eq(TeamDocument::island.name, it.name))
        }
        
        divisionCategory?.let {
            filters.add(Filters.eq(TeamDocument::divisionCategory.name, it.name))
        }
        
        search?.let {
            val searchFilter = Filters.or(
                Filters.regex(TeamDocument::name.name, it, "i"),
                Filters.regex(TeamDocument::venue.name, it, "i")
            )
            filters.add(searchFilter)
        }
        
        val filter = Filters.and(filters)
        
        return collection.find(filter)
            .sort(Sorts.ascending(TeamDocument::name.name))
            .skip(page * size)
            .limit(size)
            .toList()
            .map { TeamMapper.toDomain(it) }
    }

    override suspend fun getTeamById(id: String): Team? {
        return try {
            val objectId = ObjectId(id)
            val document = collection.find(
                Filters.and(
                    Filters.eq("_id", objectId),
                    Filters.eq(TeamDocument::isActive.name, true)
                )
            ).firstOrNull()
            document?.let { TeamMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun createTeam(team: Team): Team {
        val document = TeamMapper.toDocument(team)
        val result = collection.insertOne(document)
        return team.copy(id = result.insertedId!!.asObjectId().value.toHexString())
    }

    override suspend fun updateTeam(team: Team): Team? {
        return try {
            val objectId = ObjectId(team.id)
            val existingDocument = collection.find(
                Filters.and(
                    Filters.eq("_id", objectId),
                    Filters.eq(TeamDocument::isActive.name, true)
                )
            ).firstOrNull() ?: return null
            
            val updatedDocument = existingDocument.copy(
                name = team.name,
                imageUrl = team.imageUrl,
                island = team.island.name,
                venue = team.venue,
                divisionCategory = team.divisionCategory.name,
                updatedAt = System.currentTimeMillis()
            )
            
            collection.replaceOne(Filters.eq("_id", objectId), updatedDocument)
            team
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteTeam(id: String): Boolean {
        return try {
            val objectId = ObjectId(id)
            val result = collection.updateOne(
                Filters.eq("_id", objectId),
                Updates.set("isActive", false)
            )
            result.modifiedCount > 0
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getTeamsByIds(ids: List<String>): List<Team> {
        val objectIds = ids.mapNotNull {
            try {
                ObjectId(it)
            } catch (e: Exception) {
                null
            }
        }
        
        return collection.find(
            Filters.and(
                Filters.`in`("_id", objectIds),
                Filters.eq(TeamDocument::isActive.name, true)
            )
        )
            .toList()
            .map { TeamMapper.toDomain(it) }
    }

    override suspend fun countTeams(): Long {
        return collection.countDocuments(Filters.eq(TeamDocument::isActive.name, true))
    }

    override suspend fun getTeamsByIsland(island: Island): List<Team> {
        return collection.find(
            Filters.and(
                Filters.eq(TeamDocument::island.name, island.name),
                Filters.eq(TeamDocument::isActive.name, true)
            )
        )
            .sort(Sorts.ascending(TeamDocument::name.name))
            .toList()
            .map { TeamMapper.toDomain(it) }
    }

    override suspend fun getTeamsByDivision(divisionCategory: DivisionCategory): List<Team> {
        return collection.find(
            Filters.and(
                Filters.eq(TeamDocument::divisionCategory.name, divisionCategory.name),
                Filters.eq(TeamDocument::isActive.name, true)
            )
        )
            .sort(Sorts.ascending(TeamDocument::name.name))
            .toList()
            .map { TeamMapper.toDomain(it) }
    }
}
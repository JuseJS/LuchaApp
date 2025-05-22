package com.luchacanaria.features.wrestlers.data.repository

import com.luchacanaria.core.database.DatabaseConfig
import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.wrestlers.data.document.WrestlerDocument
import com.luchacanaria.features.wrestlers.data.mapper.WrestlerMapper
import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import com.luchacanaria.features.wrestlers.domain.repository.WrestlerRepository
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MongoWrestlerRepository : WrestlerRepository {
    private val collection = DatabaseConfig.wrestlersCollection
    private val teamsCollection = DatabaseConfig.teamsCollection

    override suspend fun getAllWrestlers(
        teamId: String?,
        category: WrestlerCategory?,
        classification: WrestlerClassification?,
        island: Island?,
        search: String?,
        page: Int,
        size: Int
    ): List<Wrestler> {
        val filters = mutableListOf<org.bson.conversions.Bson>()
        filters.add(Filters.eq(WrestlerDocument::isActive.name, true))
        
        teamId?.let {
            filters.add(Filters.eq(WrestlerDocument::teamId.name, it))
        }
        
        category?.let {
            filters.add(Filters.eq(WrestlerDocument::category.name, it.name))
        }
        
        classification?.let {
            filters.add(Filters.eq(WrestlerDocument::classification.name, it.name))
        }
        
        search?.let {
            val searchFilter = Filters.or(
                Filters.regex(WrestlerDocument::name.name, it, "i"),
                Filters.regex(WrestlerDocument::surname.name, it, "i"),
                Filters.regex(WrestlerDocument::licenseNumber.name, it, "i"),
                Filters.regex(WrestlerDocument::nickname.name, it, "i")
            )
            filters.add(searchFilter)
        }

        // TODO: Add island filter by joining with teams collection if needed
        
        val filter = Filters.and(filters)
        
        return collection.find(filter)
            .sort(Sorts.ascending(WrestlerDocument::surname.name, WrestlerDocument::name.name))
            .skip(page * size)
            .limit(size)
            .toList()
            .map { WrestlerMapper.toDomain(it) }
    }

    override suspend fun getWrestlerById(id: String): Wrestler? {
        return try {
            val objectId = ObjectId(id)
            val document = collection.find(
                Filters.and(
                    Filters.eq("_id", objectId),
                    Filters.eq(WrestlerDocument::isActive.name, true)
                )
            ).firstOrNull()
            document?.let { WrestlerMapper.toDomain(it) }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getWrestlersByTeamId(teamId: String): List<Wrestler> {
        return collection.find(
            Filters.and(
                Filters.eq(WrestlerDocument::teamId.name, teamId),
                Filters.eq(WrestlerDocument::isActive.name, true)
            )
        )
            .sort(Sorts.ascending(WrestlerDocument::surname.name, WrestlerDocument::name.name))
            .toList()
            .map { WrestlerMapper.toDomain(it) }
    }

    override suspend fun getWrestlerByLicenseNumber(licenseNumber: String): Wrestler? {
        val document = collection.find(
            Filters.and(
                Filters.eq(WrestlerDocument::licenseNumber.name, licenseNumber),
                Filters.eq(WrestlerDocument::isActive.name, true)
            )
        ).firstOrNull()
        return document?.let { WrestlerMapper.toDomain(it) }
    }

    override suspend fun createWrestler(wrestler: Wrestler): Wrestler {
        val document = WrestlerMapper.toDocument(wrestler)
        val result = collection.insertOne(document)
        return wrestler.copy(id = result.insertedId!!.asObjectId().value.toHexString())
    }

    override suspend fun updateWrestler(wrestler: Wrestler): Wrestler? {
        return try {
            val objectId = ObjectId(wrestler.id)
            val existingDocument = collection.find(
                Filters.and(
                    Filters.eq("_id", objectId),
                    Filters.eq(WrestlerDocument::isActive.name, true)
                )
            ).firstOrNull() ?: return null
            
            val updatedDocument = existingDocument.copy(
                licenseNumber = wrestler.licenseNumber,
                name = wrestler.name,
                surname = wrestler.surname,
                imageUrl = wrestler.imageUrl,
                teamId = wrestler.teamId,
                category = wrestler.category.name,
                classification = wrestler.classification.name,
                height = wrestler.height,
                weight = wrestler.weight,
                birthDate = wrestler.birthDate?.toString(),
                nickname = wrestler.nickname,
                updatedAt = System.currentTimeMillis()
            )
            
            collection.replaceOne(Filters.eq("_id", objectId), updatedDocument)
            wrestler
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun deleteWrestler(id: String): Boolean {
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

    override suspend fun countWrestlers(): Long {
        return collection.countDocuments(Filters.eq(WrestlerDocument::isActive.name, true))
    }

    override suspend fun getWrestlersByIds(ids: List<String>): List<Wrestler> {
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
                Filters.eq(WrestlerDocument::isActive.name, true)
            )
        )
            .toList()
            .map { WrestlerMapper.toDomain(it) }
    }
}
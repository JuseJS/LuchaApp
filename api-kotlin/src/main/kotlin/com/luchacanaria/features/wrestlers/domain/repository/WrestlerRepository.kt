package com.luchacanaria.features.wrestlers.domain.repository

import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import com.luchacanaria.core.domain.model.Island

interface WrestlerRepository {
    suspend fun getAllWrestlers(
        teamId: String? = null,
        category: WrestlerCategory? = null,
        classification: WrestlerClassification? = null,
        island: Island? = null,
        search: String? = null,
        page: Int = 0,
        size: Int = 20
    ): List<Wrestler>

    suspend fun getWrestlerById(id: String): Wrestler?
    suspend fun getWrestlersByTeamId(teamId: String): List<Wrestler>
    suspend fun getWrestlerByLicenseNumber(licenseNumber: String): Wrestler?
    suspend fun createWrestler(wrestler: Wrestler): Wrestler
    suspend fun updateWrestler(wrestler: Wrestler): Wrestler?
    suspend fun deleteWrestler(id: String): Boolean
    suspend fun countWrestlers(): Long
    suspend fun getWrestlersByIds(ids: List<String>): List<Wrestler>
}
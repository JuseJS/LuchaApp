package org.iesharia.features.wrestlers.domain.repository

import org.iesharia.features.wrestlers.domain.model.Wrestler

interface WrestlerRepository {
    suspend fun getWrestlers(): List<Wrestler>
    suspend fun getWrestlersByTeamId(teamId: String): List<Wrestler>
    suspend fun getWrestler(id: String): Wrestler?
}
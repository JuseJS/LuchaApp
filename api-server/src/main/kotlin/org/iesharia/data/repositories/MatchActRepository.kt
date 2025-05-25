package org.iesharia.data.repositories

import org.iesharia.data.models.MatchActDocument

interface MatchActRepository {
    suspend fun findById(id: String): MatchActDocument?
    suspend fun findByMatchId(matchId: String): MatchActDocument?
    suspend fun create(matchAct: MatchActDocument): MatchActDocument
    suspend fun update(id: String, matchAct: MatchActDocument): MatchActDocument?
    suspend fun delete(id: String): Boolean
}
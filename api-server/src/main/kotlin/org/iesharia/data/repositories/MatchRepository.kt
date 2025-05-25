package org.iesharia.data.repositories

import kotlinx.datetime.LocalDate
import org.iesharia.data.models.MatchDocument

interface MatchRepository {
    suspend fun findAll(): List<MatchDocument>
    suspend fun findById(id: String): MatchDocument?
    suspend fun findByTeamId(teamId: String): List<MatchDocument>
    suspend fun findByCompetitionId(competitionId: String): List<MatchDocument>
    suspend fun findUpcoming(from: LocalDate, limit: Int): List<MatchDocument>
    suspend fun findRecent(until: LocalDate, limit: Int): List<MatchDocument>
    suspend fun findByDateRange(start: LocalDate, end: LocalDate): List<MatchDocument>
    suspend fun create(match: MatchDocument): MatchDocument
    suspend fun update(id: String, match: MatchDocument): MatchDocument?
    suspend fun delete(id: String): Boolean
    suspend fun updateHasAct(matchId: String, hasAct: Boolean): Boolean
    suspend fun updateScore(matchId: String, localScore: Int, visitorScore: Int, completed: Boolean): Boolean
}
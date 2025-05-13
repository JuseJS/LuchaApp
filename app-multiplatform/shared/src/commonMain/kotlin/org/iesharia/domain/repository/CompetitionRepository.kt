package org.iesharia.domain.repository

import org.iesharia.domain.model.*

interface CompetitionRepository {
    suspend fun getCompetitions(): List<Competition>
    suspend fun getCompetition(id: String): Competition?
    suspend fun getFavorites(): List<Favorite>
    suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>>
    suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult>
}
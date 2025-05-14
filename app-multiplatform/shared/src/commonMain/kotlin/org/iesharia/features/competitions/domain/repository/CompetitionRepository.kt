package org.iesharia.features.competitions.domain.repository

import org.iesharia.core.domain.model.Favorite
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

interface CompetitionRepository {
    suspend fun getCompetitions(): List<Competition>
    suspend fun getCompetition(id: String): Competition?
    suspend fun getFavorites(): List<Favorite>
    suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>>
    suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult>
}
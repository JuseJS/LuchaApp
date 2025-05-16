package org.iesharia.features.competitions.data.repository

import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.core.domain.model.Favorite
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

class MockCompetitionRepository(
    private val mockDataGenerator: MockDataGenerator
) : CompetitionRepository {

    init {
        // Inicializar datos mock
        mockDataGenerator.generateAllData()
    }

    override suspend fun getCompetitions(): List<Competition> =
        mockDataGenerator.competitions

    override suspend fun getCompetition(id: String): Competition? =
        mockDataGenerator.competitions.find { it.id == id }

    override suspend fun getFavorites(): List<Favorite> =
        mockDataGenerator.favorites

    override suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>> =
        mockDataGenerator.getTeamMatches(teamId)

    override suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> =
        mockDataGenerator.getWrestlerResults(wrestlerId)
}
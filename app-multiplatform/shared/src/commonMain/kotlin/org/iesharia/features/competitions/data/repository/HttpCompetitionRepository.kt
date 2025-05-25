
package org.iesharia.features.competitions.data.repository

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.domain.model.Island
import org.iesharia.core.network.service.CompetitionService
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult

class HttpCompetitionRepository(
    private val competitionService: CompetitionService
) : CompetitionRepository {
    
    override suspend fun getCompetitions(): List<Competition> {
        return try {
            val competitions = competitionService.getAllCompetitions()
            competitions.map { dto ->
                Competition(
                    id = dto.id,
                    name = dto.name,
                    ageCategory = AgeCategory.valueOf(dto.ageCategory),
                    divisionCategory = DivisionCategory.valueOf(dto.divisionCategory),
                    island = Island.valueOf(dto.island),
                    season = dto.season,
                    matchDays = dto.matchDays.map { matchDayDto ->
                        MatchDay(
                            id = matchDayDto.id,
                            number = matchDayDto.number,
                            competitionId = dto.id,
                            matches = matchDayDto.matches.map { matchDto ->
                                Match(
                                    id = matchDto.id,
                                    localTeam = Team(
                                        id = matchDto.localTeamId,
                                        name = matchDto.localTeamName,
                                        imageUrl = "",
                                        island = Island.TENERIFE, // Default
                                        venue = "",
                                        divisionCategory = DivisionCategory.PRIMERA // Default
                                    ),
                                    visitorTeam = Team(
                                        id = matchDto.visitorTeamId,
                                        name = matchDto.visitorTeamName,
                                        imageUrl = "",
                                        island = Island.TENERIFE, // Default
                                        venue = "",
                                        divisionCategory = DivisionCategory.PRIMERA // Default
                                    ),
                                    localScore = matchDto.localScore,
                                    visitorScore = matchDto.visitorScore,
                                    date = try {
                                        kotlinx.datetime.LocalDateTime.parse(matchDto.date)
                                    } catch (e: Exception) {
                                        try {
                                            kotlinx.datetime.LocalDateTime.parse(matchDto.date + "T00:00:00")
                                        } catch (e2: Exception) {
                                            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                        }
                                    },
                                    venue = matchDto.venue,
                                    completed = matchDto.completed,
                                    hasAct = false
                                )
                            }
                        )
                    },
                    teams = dto.teams.map { teamDto ->
                        Team(
                            id = teamDto.id,
                            name = teamDto.name,
                            imageUrl = teamDto.imageUrl,
                            island = Island.valueOf(teamDto.island),
                            venue = teamDto.venue,
                            divisionCategory = DivisionCategory.valueOf(teamDto.divisionCategory)
                        )
                    }
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getCompetition(id: String): Competition? {
        return try {
            val dto = competitionService.getCompetitionById(id)
            
            Competition(
                id = dto.id,
                name = dto.name,
                ageCategory = AgeCategory.valueOf(dto.ageCategory),
                divisionCategory = DivisionCategory.valueOf(dto.divisionCategory),
                island = Island.valueOf(dto.island),
                season = dto.season,
                matchDays = dto.matchDays.map { matchDayDto ->
                    MatchDay(
                        id = matchDayDto.id,
                        number = matchDayDto.number,
                        competitionId = dto.id,
                        matches = matchDayDto.matches.map { matchDto ->
                            Match(
                                id = matchDto.id,
                                localTeam = Team(
                                    id = "",
                                    name = matchDto.localTeamName,
                                    imageUrl = "",
                                    island = Island.TENERIFE, // Default
                                    venue = "",
                                    divisionCategory = DivisionCategory.PRIMERA // Default
                                ),
                                visitorTeam = Team(
                                    id = "",
                                    name = matchDto.visitorTeamName,
                                    imageUrl = "",
                                    island = Island.TENERIFE, // Default
                                    venue = "",
                                    divisionCategory = DivisionCategory.PRIMERA // Default
                                ),
                                localScore = matchDto.localScore,
                                visitorScore = matchDto.visitorScore,
                                date = try {
                                    kotlinx.datetime.LocalDateTime.parse(matchDto.date)
                                } catch (e: Exception) {
                                    try {
                                        kotlinx.datetime.LocalDateTime.parse(matchDto.date + "T00:00:00")
                                    } catch (e2: Exception) {
                                        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                                    }
                                },
                                venue = matchDto.venue,
                                completed = matchDto.completed,
                                hasAct = false
                            )
                        }
                    )
                },
                teams = dto.teams.map { teamDto ->
                    Team(
                        id = teamDto.id,
                        name = teamDto.name,
                        imageUrl = teamDto.imageUrl,
                        island = Island.valueOf(teamDto.island),
                        venue = teamDto.venue,
                        divisionCategory = DivisionCategory.valueOf(teamDto.divisionCategory)
                    )
                }
            )
        } catch (e: Exception) {
            println("Error loading competition details: ${e.message}")
            null
        }
    }
    
    override suspend fun getFavorites(): List<Favorite> {
        // Por ahora, devolver lista vacía. En el futuro, esto debería venir de la API
        return emptyList()
    }
    
    override suspend fun getTeamMatches(teamId: String): Pair<List<Match>, List<Match>> {
        // Por ahora, devolver listas vacías. En el futuro, esto debería venir de la API
        return Pair(emptyList(), emptyList())
    }
    
    override suspend fun getWrestlerResults(wrestlerId: String): List<WrestlerMatchResult> {
        // Por ahora, devolver lista vacía. En el futuro, esto debería venir de la API
        return emptyList()
    }
}
package org.iesharia.domain.services

import kotlinx.datetime.LocalDate
import org.iesharia.data.repositories.CompetitionRepository
import org.iesharia.data.repositories.MatchRepository
import org.iesharia.data.repositories.TeamRepository
import org.iesharia.domain.models.dto.CommonDtos
import org.iesharia.domain.models.dto.TeamDto

class CompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) {
    suspend fun getAllCompetitions(): List<CommonDtos.CompetitionDto> {
        return competitionRepository.findAll().map { competition ->
            CommonDtos.CompetitionDto(
                id = competition.id,
                name = competition.name,
                ageCategory = competition.ageCategory,
                divisionCategory = competition.divisionCategory,
                island = competition.island,
                season = competition.season,
                matchDays = getMatchDaysByCompetitionId(competition.id),
                teams = getTeamsByCompetitionId(competition.id)
            )
        }
    }
    
    suspend fun getCompetitionById(id: String): CommonDtos.CompetitionDto? {
        val competition = competitionRepository.findById(id) ?: return null
        return CommonDtos.CompetitionDto(
            id = competition.id,
            name = competition.name,
            ageCategory = competition.ageCategory,
            divisionCategory = competition.divisionCategory,
            island = competition.island,
            season = competition.season,
            matchDays = getMatchDaysByCompetitionId(competition.id),
            teams = getTeamsByCompetitionId(competition.id)
        )
    }
    
    suspend fun getCompetitionsByFilters(
        ageCategory: String? = null,
        divisionCategory: String? = null,
        island: String? = null,
        season: String? = null
    ): List<CommonDtos.CompetitionDto> {
        return competitionRepository.findByFilters(
            ageCategory = ageCategory,
            divisionCategory = divisionCategory,
            island = island,
            season = season
        ).map { competition ->
            CommonDtos.CompetitionDto(
                id = competition.id,
                name = competition.name,
                ageCategory = competition.ageCategory,
                divisionCategory = competition.divisionCategory,
                island = competition.island,
                season = competition.season,
                matchDays = getMatchDaysByCompetitionId(competition.id),
                teams = getTeamsByCompetitionId(competition.id)
            )
        }
    }
    
    suspend fun getMatchDaysByCompetitionId(competitionId: String): List<CommonDtos.MatchDayDto> {
        val matches = matchRepository.findByCompetitionId(competitionId)
        
        // Group matches by date to create match days
        return matches.groupBy { it.date }
            .map { (date, dayMatches) ->
                CommonDtos.MatchDayDto(
                    id = "day_${date.date.toEpochDays()}",
                    number = dayMatches.firstOrNull()?.round ?: 1,
                    date = date.toString(),
                    matches = dayMatches.map { match ->
                        CommonDtos.MatchSummaryDto(
                            id = match.id,
                            localTeamId = match.localTeamId,
                            localTeamName = teamRepository.findById(match.localTeamId)?.name ?: "",
                            visitorTeamId = match.visitorTeamId,
                            visitorTeamName = teamRepository.findById(match.visitorTeamId)?.name ?: "",
                            localScore = match.localScore,
                            visitorScore = match.visitorScore,
                            date = match.date.toString(),
                            venue = match.venue,
                            completed = match.completed
                        )
                    }
                )
            }
            .sortedBy { it.number }
    }
    
    suspend fun getTeamsByCompetitionId(competitionId: String): List<TeamDto> {
        // Get competition to get team IDs
        val competition = competitionRepository.findById(competitionId) ?: return emptyList()
        
        // Get team details for each team ID
        return competition.teamIds.mapNotNull { teamId ->
            teamRepository.findById(teamId)?.let { team ->
                TeamDto(
                    id = team.id,
                    name = team.name,
                    imageUrl = team.imageUrl,
                    island = team.island,
                    venue = team.venue,
                    divisionCategory = team.divisionCategory
                )
            }
        }
    }
}
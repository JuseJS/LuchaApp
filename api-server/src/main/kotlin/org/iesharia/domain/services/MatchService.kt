package org.iesharia.domain.services

import kotlinx.datetime.*
import org.iesharia.data.repositories.CompetitionRepository
import org.iesharia.data.repositories.MatchRepository
import org.iesharia.data.repositories.TeamRepository
import org.iesharia.domain.models.dto.CommonDtos.MatchDto

class MatchService(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository,
    private val competitionRepository: CompetitionRepository
) {
    suspend fun getMatchById(id: String): MatchDto? {
        val match = matchRepository.findById(id) ?: return null
        val localTeam = teamRepository.findById(match.localTeamId)
        val visitorTeam = teamRepository.findById(match.visitorTeamId)
        val competition = match.competitionId?.let { competitionRepository.findById(it) }
        
        return MatchDto(
            id = match.id,
            localTeamId = match.localTeamId,
            visitorTeamId = match.visitorTeamId,
            localTeamName = localTeam?.name ?: "Unknown",
            visitorTeamName = visitorTeam?.name ?: "Unknown",
            localScore = match.localScore,
            visitorScore = match.visitorScore,
            date = match.date.toString(),
            venue = match.venue,
            completed = match.completed,
            hasAct = match.hasAct,
            competitionId = match.competitionId,
            competitionName = competition?.name,
            round = match.round
        )
    }
    
    suspend fun getMatchesByTeamId(teamId: String): List<MatchDto> {
        val matches = matchRepository.findByTeamId(teamId)
        
        return matches.map { match ->
            val localTeam = teamRepository.findById(match.localTeamId)
            val visitorTeam = teamRepository.findById(match.visitorTeamId)
            val competition = match.competitionId?.let { competitionRepository.findById(it) }
            
            MatchDto(
                id = match.id,
                localTeamId = match.localTeamId,
                visitorTeamId = match.visitorTeamId,
                localTeamName = localTeam?.name ?: "Unknown",
                visitorTeamName = visitorTeam?.name ?: "Unknown",
                localScore = match.localScore,
                visitorScore = match.visitorScore,
                date = match.date.toString(),
                venue = match.venue,
                completed = match.completed,
                hasAct = match.hasAct,
                competitionId = match.competitionId,
                competitionName = competition?.name,
                round = match.round
            )
        }
    }
    
    suspend fun getUpcomingMatches(limit: Int): List<MatchDto> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val matches = matchRepository.findUpcoming(today, limit)
        
        return matches.map { match ->
            val localTeam = teamRepository.findById(match.localTeamId)
            val visitorTeam = teamRepository.findById(match.visitorTeamId)
            val competition = match.competitionId?.let { competitionRepository.findById(it) }
            
            MatchDto(
                id = match.id,
                localTeamId = match.localTeamId,
                visitorTeamId = match.visitorTeamId,
                localTeamName = localTeam?.name ?: "Unknown",
                visitorTeamName = visitorTeam?.name ?: "Unknown",
                localScore = match.localScore,
                visitorScore = match.visitorScore,
                date = match.date.toString(),
                venue = match.venue,
                completed = match.completed,
                hasAct = match.hasAct,
                competitionId = match.competitionId,
                competitionName = competition?.name,
                round = match.round
            )
        }
    }
    
    suspend fun getRecentMatches(limit: Int): List<MatchDto> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val matches = matchRepository.findRecent(today, limit)
        
        return matches.map { match ->
            val localTeam = teamRepository.findById(match.localTeamId)
            val visitorTeam = teamRepository.findById(match.visitorTeamId)
            val competition = match.competitionId?.let { competitionRepository.findById(it) }
            
            MatchDto(
                id = match.id,
                localTeamId = match.localTeamId,
                visitorTeamId = match.visitorTeamId,
                localTeamName = localTeam?.name ?: "Unknown",
                visitorTeamName = visitorTeam?.name ?: "Unknown",
                localScore = match.localScore,
                visitorScore = match.visitorScore,
                date = match.date.toString(),
                venue = match.venue,
                completed = match.completed,
                hasAct = match.hasAct,
                competitionId = match.competitionId,
                competitionName = competition?.name,
                round = match.round
            )
        }
    }
    
    suspend fun getMatchesByCompetitionId(competitionId: String): List<MatchDto> {
        val matches = matchRepository.findByCompetitionId(competitionId)
        val competition = competitionRepository.findById(competitionId)
        
        return matches.map { match ->
            val localTeam = teamRepository.findById(match.localTeamId)
            val visitorTeam = teamRepository.findById(match.visitorTeamId)
            
            MatchDto(
                id = match.id,
                localTeamId = match.localTeamId,
                visitorTeamId = match.visitorTeamId,
                localTeamName = localTeam?.name ?: "Unknown",
                visitorTeamName = visitorTeam?.name ?: "Unknown",
                localScore = match.localScore,
                visitorScore = match.visitorScore,
                date = match.date.toString(),
                venue = match.venue,
                completed = match.completed,
                hasAct = match.hasAct,
                competitionId = competitionId,
                competitionName = competition?.name,
                round = match.round
            )
        }
    }
    
    suspend fun getMatchesByDateRange(startDate: String, endDate: String): List<MatchDto> {
        val start = LocalDate.parse(startDate)
        val end = LocalDate.parse(endDate)
        val matches = matchRepository.findByDateRange(start, end)
        
        return matches.map { match ->
            val localTeam = teamRepository.findById(match.localTeamId)
            val visitorTeam = teamRepository.findById(match.visitorTeamId)
            val competition = match.competitionId?.let { competitionRepository.findById(it) }
            
            MatchDto(
                id = match.id,
                localTeamId = match.localTeamId,
                visitorTeamId = match.visitorTeamId,
                localTeamName = localTeam?.name ?: "Unknown",
                visitorTeamName = visitorTeam?.name ?: "Unknown",
                localScore = match.localScore,
                visitorScore = match.visitorScore,
                date = match.date.toString(),
                venue = match.venue,
                completed = match.completed,
                hasAct = match.hasAct,
                competitionId = match.competitionId,
                competitionName = competition?.name,
                round = match.round
            )
        }
    }
}
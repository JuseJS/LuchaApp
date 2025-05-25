package org.iesharia.features.matches.data.repository

import kotlinx.datetime.LocalDateTime
import org.iesharia.core.domain.model.Island
import org.iesharia.core.network.service.MatchService
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.matches.domain.model.Referee
import org.iesharia.core.network.service.WrestlerService
import org.iesharia.core.network.mapper.WrestlerMappers.toDomain

class HttpMatchRepository(
    private val matchService: MatchService,
    private val wrestlerService: WrestlerService
) : MatchRepository {
    
    override suspend fun getMatch(matchId: String): Match? {
        return try {
            val dto = matchService.getMatchById(matchId)
            Match(
                id = dto.id,
                localTeam = Team(
                    id = dto.localTeamId,
                    name = dto.localTeamName,
                    imageUrl = "",
                    island = Island.TENERIFE, // Default
                    venue = "",
                    divisionCategory = DivisionCategory.PRIMERA // Default
                ),
                visitorTeam = Team(
                    id = dto.visitorTeamId,
                    name = dto.visitorTeamName,
                    imageUrl = "",
                    island = Island.TENERIFE, // Default
                    venue = "",
                    divisionCategory = DivisionCategory.PRIMERA // Default
                ),
                localScore = dto.localScore,
                visitorScore = dto.visitorScore,
                date = LocalDateTime.parse(dto.date),
                venue = dto.venue,
                completed = dto.completed,
                hasAct = dto.hasAct
            )
        } catch (e: Exception) {
            null
        }
    }
    
    override suspend fun getMatchDetails(matchId: String): Triple<Match, List<Wrestler>, List<Wrestler>> {
        // Obtener el partido primero
        val match = getMatch(matchId) ?: throw Exception("Match not found")
        
        // Obtener los luchadores de cada equipo usando el WrestlerService
        val localWrestlers = try {
            val dtos = wrestlerService.getWrestlersByTeamId(match.localTeam.id)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            println("Error getting local team wrestlers: ${e.message}")
            emptyList()
        }
        
        val visitorWrestlers = try {
            val dtos = wrestlerService.getWrestlersByTeamId(match.visitorTeam.id)
            dtos.map { it.toDomain() }
        } catch (e: Exception) {
            println("Error getting visitor team wrestlers: ${e.message}")
            emptyList()
        }
        
        println("HttpMatchRepository - Loaded ${localWrestlers.size} local wrestlers and ${visitorWrestlers.size} visitor wrestlers")
        
        return Triple(match, localWrestlers, visitorWrestlers)
    }
    
    override suspend fun getMatchReferees(matchId: String): Pair<String, List<String>> {
        // Simplified implementation
        return Pair("", emptyList())
    }
    
    override suspend fun getMatchStatistics(matchId: String): Map<String, Any> {
        // Simplified implementation
        return emptyMap()
    }
    
    override suspend fun getMatchDay(matchId: String): MatchDay? {
        // Obtener el match para conocer la competición
        val match = try {
            matchService.getMatchById(matchId)
        } catch (e: Exception) {
            println("Error getting match for matchDay: ${e.message}")
            return null
        }
        
        // Si el match tiene competitionId, crear un MatchDay simplificado
        return if (match.competitionId != null) {
            MatchDay(
                id = "matchday_${match.id}",
                number = match.round ?: 1,
                matches = emptyList(), // No necesitamos todos los matches aquí
                competitionId = match.competitionId
            )
        } else {
            null
        }
    }
}
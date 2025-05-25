package org.iesharia.domain.services

import org.iesharia.data.repositories.TeamRepository
import org.iesharia.data.repositories.WrestlerRepository
import org.iesharia.domain.models.dto.TeamDto
import org.iesharia.domain.models.dto.WrestlerDto

class TeamService(
    private val teamRepository: TeamRepository,
    private val wrestlerRepository: WrestlerRepository
) {
    suspend fun getAllTeams(): List<TeamDto> {
        return teamRepository.findAll().map { team ->
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
    
    suspend fun getTeamsByDivision(division: String): List<TeamDto> {
        return teamRepository.findByDivision(division).map { team ->
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
    
    suspend fun getTeamById(id: String): TeamDto? {
        val team = teamRepository.findById(id) ?: return null
        return TeamDto(
            id = team.id,
            name = team.name,
            imageUrl = team.imageUrl,
            island = team.island,
            venue = team.venue,
            divisionCategory = team.divisionCategory
        )
    }
    
    suspend fun getWrestlersByTeamId(teamId: String): List<WrestlerDto> {
        return wrestlerRepository.findByTeamId(teamId).map { wrestler ->
            WrestlerDto(
                id = wrestler.id,
                licenseNumber = wrestler.licenseNumber,
                name = wrestler.name,
                surname = wrestler.surname,
                imageUrl = wrestler.imageUrl,
                teamId = wrestler.teamId,
                category = wrestler.category,
                classification = wrestler.classification,
                height = wrestler.height,
                weight = wrestler.weight,
                birthDate = wrestler.birthDate?.toString(),
                nickname = wrestler.nickname
            )
        }
    }
    
    suspend fun searchTeams(query: String): List<TeamDto> {
        return teamRepository.search(query).map { team ->
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
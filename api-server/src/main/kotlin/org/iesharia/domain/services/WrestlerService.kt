package org.iesharia.domain.services

import org.iesharia.data.repositories.WrestlerRepository
import org.iesharia.domain.models.dto.WrestlerDto

class WrestlerService(
    private val wrestlerRepository: WrestlerRepository
) {
    suspend fun getAllWrestlers(): List<WrestlerDto> {
        return wrestlerRepository.findAll().map { wrestler ->
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
    
    suspend fun getWrestlerById(id: String): WrestlerDto? {
        val wrestler = wrestlerRepository.findById(id) ?: return null
        return WrestlerDto(
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
    
    suspend fun searchWrestlers(query: String): List<WrestlerDto> {
        return wrestlerRepository.search(query).map { wrestler ->
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
}
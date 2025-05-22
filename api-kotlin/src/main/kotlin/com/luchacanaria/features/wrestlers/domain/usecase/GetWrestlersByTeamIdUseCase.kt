package com.luchacanaria.features.wrestlers.domain.usecase

import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.repository.WrestlerRepository

class GetWrestlersByTeamIdUseCase(private val wrestlerRepository: WrestlerRepository) {
    suspend operator fun invoke(teamId: String): List<Wrestler> {
        return wrestlerRepository.getWrestlersByTeamId(teamId)
    }
}
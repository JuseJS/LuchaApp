package com.luchacanaria.features.wrestlers.domain.usecase

import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.repository.WrestlerRepository

class GetWrestlerUseCase(private val wrestlerRepository: WrestlerRepository) {
    suspend operator fun invoke(id: String): Wrestler? {
        return wrestlerRepository.getWrestlerById(id)
    }
}
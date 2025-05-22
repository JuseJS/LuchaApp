package com.luchacanaria.features.wrestlers.domain.usecase

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.wrestlers.domain.model.Wrestler
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import com.luchacanaria.features.wrestlers.domain.repository.WrestlerRepository

data class GetWrestlersRequest(
    val teamId: String? = null,
    val category: WrestlerCategory? = null,
    val classification: WrestlerClassification? = null,
    val island: Island? = null,
    val search: String? = null,
    val page: Int = 0,
    val size: Int = 20
)

class GetWrestlersUseCase(private val wrestlerRepository: WrestlerRepository) {
    suspend operator fun invoke(request: GetWrestlersRequest): List<Wrestler> {
        return wrestlerRepository.getAllWrestlers(
            teamId = request.teamId,
            category = request.category,
            classification = request.classification,
            island = request.island,
            search = request.search,
            page = request.page,
            size = request.size
        )
    }
}
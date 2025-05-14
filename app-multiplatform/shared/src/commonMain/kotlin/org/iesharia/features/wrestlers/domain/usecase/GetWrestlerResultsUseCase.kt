package org.iesharia.features.wrestlers.domain.usecase

import org.iesharia.features.wrestlers.domain.model.WrestlerMatchResult
import org.iesharia.features.competitions.domain.repository.CompetitionRepository

class GetWrestlerResultsUseCase(private val repository: CompetitionRepository) {
    /**
     * Obtiene los resultados de los enfrentamientos de un luchador
     * @param wrestlerId ID del luchador
     * @return Lista de resultados de enfrentamientos
     */
    suspend operator fun invoke(wrestlerId: String): List<WrestlerMatchResult> =
        repository.getWrestlerResults(wrestlerId)
}
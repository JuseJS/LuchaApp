package org.iesharia.features.matches.domain.usecase

import org.iesharia.features.matches.domain.model.MatchAct
import org.iesharia.features.matches.domain.repository.MatchActRepository

/**
 * Caso de uso para guardar un acta de enfrentamiento
 */
class SaveMatchActUseCase(
    private val matchActRepository: MatchActRepository
) {
    /**
     * Guarda un acta de enfrentamiento
     */
    suspend operator fun invoke(matchAct: MatchAct): MatchAct {
        return matchActRepository.saveMatchAct(matchAct)
    }
    
    /**
     * Finaliza un acta de enfrentamiento
     */
    suspend fun complete(actId: String): MatchAct {
        return matchActRepository.completeMatchAct(actId)
    }
}
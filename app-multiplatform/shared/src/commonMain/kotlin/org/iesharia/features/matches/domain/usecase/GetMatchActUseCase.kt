package org.iesharia.features.matches.domain.usecase

import org.iesharia.features.matches.domain.model.MatchAct
import org.iesharia.features.matches.domain.repository.MatchActRepository

/**
 * Caso de uso para obtener un acta de enfrentamiento
 */
class GetMatchActUseCase(
    private val matchActRepository: MatchActRepository
) {
    /**
     * Obtiene un acta por su ID
     */
    suspend operator fun invoke(actId: String): MatchAct? {
        val act = matchActRepository.getMatchAct(actId)
        println("GetMatchActUseCase.invoke - Act found: ${act?.id}")
        return act
    }
    
    /**
     * Obtiene un acta por el ID del enfrentamiento
     */
    suspend fun byMatchId(matchId: String): MatchAct? {
        val act = matchActRepository.getMatchActByMatchId(matchId)
        println("GetMatchActUseCase.byMatchId - matchId: $matchId, Act found: ${act?.id}")
        return act
    }
}
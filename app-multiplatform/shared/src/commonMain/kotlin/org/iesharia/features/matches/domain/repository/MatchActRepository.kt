package org.iesharia.features.matches.domain.repository

import org.iesharia.features.matches.domain.model.MatchAct
import org.iesharia.features.matches.domain.model.Referee

/**
 * Repositorio para gestionar actas de enfrentamiento
 */
interface MatchActRepository {
    /**
     * Obtiene un acta por su ID
     */
    suspend fun getMatchAct(actId: String): MatchAct?
    
    /**
     * Obtiene un acta por el ID del enfrentamiento
     */
    suspend fun getMatchActByMatchId(matchId: String): MatchAct?
    
    /**
     * Guarda un acta
     */
    suspend fun saveMatchAct(matchAct: MatchAct): MatchAct
    
    /**
     * Finaliza un acta
     */
    suspend fun completeMatchAct(actId: String): MatchAct
    
    /**
     * Obtiene la lista de árbitros disponibles
     */
    suspend fun getAvailableReferees(): List<Referee>
}
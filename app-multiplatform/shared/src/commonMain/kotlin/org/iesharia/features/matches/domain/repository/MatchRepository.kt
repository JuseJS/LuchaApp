package org.iesharia.features.matches.domain.repository

import org.iesharia.features.competitions.domain.model.MatchDay
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.Wrestler

interface MatchRepository {
    /**
     * Obtiene un enfrentamiento por su ID
     */
    suspend fun getMatch(matchId: String): Match?

    /**
     * Obtiene un enfrentamiento con toda la información detallada incluyendo luchadores
     * @return Triple con: (enfrentamiento, luchadores del equipo local, luchadores del equipo visitante)
     */
    suspend fun getMatchDetails(matchId: String): Triple<Match, List<Wrestler>, List<Wrestler>>

    /**
     * Obtiene información sobre el arbitraje del enfrentamiento
     * @return Par con: (árbitro principal, árbitros asistentes)
     */
    suspend fun getMatchReferees(matchId: String): Pair<String, List<String>>

    /**
     * Obtiene estadísticas adicionales del enfrentamiento
     */
    suspend fun getMatchStatistics(matchId: String): Map<String, Any>

    /**
     * Obtiene la jornada a la que pertenece un enfrentamiento
     */
    suspend fun getMatchDay(matchId: String): MatchDay?
}
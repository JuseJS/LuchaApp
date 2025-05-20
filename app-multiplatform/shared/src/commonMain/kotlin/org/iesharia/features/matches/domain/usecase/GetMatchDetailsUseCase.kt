package org.iesharia.features.matches.domain.usecase

import org.iesharia.core.domain.model.AppError
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.teams.domain.model.Match
import org.iesharia.features.wrestlers.domain.model.Wrestler

/**
 * Caso de uso para obtener detalles de un enfrentamiento, incluyendo alineación de luchadores
 */
class GetMatchDetailsUseCase(private val repository: MatchRepository) {

    /**
     * Obtiene los detalles completos de un enfrentamiento incluyendo luchadores
     * @param matchId ID del enfrentamiento
     * @return Triple con: (enfrentamiento, luchadores del equipo local, luchadores del equipo visitante)
     */
    suspend operator fun invoke(matchId: String): Triple<Match, List<Wrestler>, List<Wrestler>> {
        try {
            return repository.getMatchDetails(matchId)
        } catch (e: Exception) {
            if (e is AppError) throw e
            throw AppError.UnknownError(e, "Error al obtener detalles del enfrentamiento")
        }
    }
}
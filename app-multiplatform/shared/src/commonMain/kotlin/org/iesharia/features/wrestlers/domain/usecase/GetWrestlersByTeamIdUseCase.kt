package org.iesharia.features.wrestlers.domain.usecase

import org.iesharia.core.domain.model.AppError
import org.iesharia.features.wrestlers.domain.model.Wrestler
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository

class GetWrestlersByTeamIdUseCase(private val repository: WrestlerRepository) {
    suspend operator fun invoke(teamId: String): List<Wrestler> {
        try {
            return repository.getWrestlersByTeamId(teamId)
        } catch (e: Exception) {
            if (e is AppError) throw e
            throw AppError.UnknownError(e, "Error al obtener luchadores del equipo")
        }
    }
}
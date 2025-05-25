package org.iesharia.features.common.domain.model

import org.iesharia.core.domain.model.Island
import org.iesharia.features.competitions.domain.model.AgeCategory
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.core.resources.AppStrings

/**
 * Opciones para el tipo de favorito seleccionado
 */
enum class FavoriteType {
    ALL,
    COMPETITIONS,
    TEAMS,
    WRESTLERS;

    fun displayName(): String = when(this) {
        ALL -> AppStrings.Home.all
        COMPETITIONS -> AppStrings.Home.competitions
        TEAMS -> AppStrings.Home.teams
        WRESTLERS -> AppStrings.Home.wrestlers
    }
}

/**
 * Filtros para la sección de competiciones
 */
data class CompetitionFilters(
    val ageCategory: AgeCategory? = null,
    val divisionCategory: DivisionCategory? = null,
    val island: Island? = null
)
package org.iesharia.features.teams.domain.model

import org.iesharia.core.domain.model.Island
import org.iesharia.features.competitions.domain.model.DivisionCategory

/**
 * Modelo que representa un equipo de lucha canaria
 */
data class Team(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: Island,
    val venue: String,
    val divisionCategory: DivisionCategory = DivisionCategory.PRIMERA
)
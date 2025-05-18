package org.iesharia.features.teams.domain.model

import org.iesharia.core.domain.model.Island

/**
 * Modelo que representa un equipo de lucha canaria
 */
data class Team(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: Island,
    val venue: String
)
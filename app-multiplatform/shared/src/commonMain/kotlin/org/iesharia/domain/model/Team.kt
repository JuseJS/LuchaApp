package org.iesharia.domain.model

/**
 * Modelo que representa un equipo de lucha canaria
 */
data class Team(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: Island
)
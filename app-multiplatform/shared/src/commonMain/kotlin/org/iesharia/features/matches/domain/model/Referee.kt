package org.iesharia.features.matches.domain.model

/**
 * Modelo que representa un árbitro de lucha canaria
 */
data class Referee(
    val id: String,
    val name: String,
    val licenseNumber: String,
    val isMain: Boolean = false,
    val isActive: Boolean = true
)
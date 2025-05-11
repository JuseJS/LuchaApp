package org.iesharia.domain.model

/**
 * Modelo que representa a un luchador
 */
data class Wrestler(
    val id: String,
    val name: String,
    val surname: String,
    val imageUrl: String?,
    val teamId: String,
    val position: String, // Posición en la que lucha
    val weight: Int? = null,
    val height: Int? = null,
    val birthYear: Int? = null,
    val achievements: List<String> = emptyList()
) {
    val fullName: String get() = "$name $surname"
}
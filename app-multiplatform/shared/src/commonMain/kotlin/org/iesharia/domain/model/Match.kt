package org.iesharia.domain.model

import kotlinx.datetime.LocalDateTime

/**
 * Modelo que representa un enfrentamiento entre dos equipos
 */
data class Match(
    val id: String,
    val localTeam: Team,
    val visitorTeam: Team,
    val localScore: Int? = null,
    val visitorScore: Int? = null,
    val date: LocalDateTime,
    val venue: String, // Terrero donde se celebra
    val completed: Boolean = false
) {
    /**
     * Determina el equipo ganador del enfrentamiento
     */
    val winner: Team? get() = when {
        !completed || localScore == null || visitorScore == null -> null
        localScore > visitorScore -> localTeam
        visitorScore > localScore -> visitorTeam
        else -> null // Empate
    }

    /**
     * Determina si el enfrentamiento terminó en empate
     */
    val isDrawn: Boolean get() =
        completed && localScore != null && visitorScore != null && localScore == visitorScore
}
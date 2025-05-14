package org.iesharia.features.competitions.domain.model

import org.iesharia.core.domain.model.Island
import org.iesharia.features.teams.domain.model.Team

/**
 * Categoría de edad en la competición
 */
enum class AgeCategory {
    JUVENIL,
    REGIONAL;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

/**
 * Categoría de la competición por nivel
 */
enum class DivisionCategory {
    PRIMERA,
    SEGUNDA,
    TERCERA;

    fun displayName(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}

/**
 * Modelo que representa una competición
 */
data class Competition(
    val id: String,
    val name: String,
    val ageCategory: AgeCategory,
    val divisionCategory: DivisionCategory,
    val island: Island,
    val season: String,
    val matchDays: List<MatchDay> = emptyList(),
    val teams: List<Team> = emptyList()
) {
    /**
     * Obtiene la última jornada completada
     */
    val lastCompletedMatchDay: MatchDay? get() =
        matchDays.filter { matchDay ->
            matchDay.matches.all { it.completed }
        }.maxByOrNull { it.number }

    /**
     * Obtiene la próxima jornada
     */
    val nextMatchDay: MatchDay? get() =
        matchDays.filter { matchDay ->
            matchDay.matches.any { !it.completed }
        }.minByOrNull { it.number }
}
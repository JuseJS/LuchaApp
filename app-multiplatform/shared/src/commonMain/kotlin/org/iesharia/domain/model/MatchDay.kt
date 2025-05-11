package org.iesharia.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Modelo que representa una jornada de competición
 */
data class MatchDay(
    val id: String,
    val number: Int,
    val matches: List<Match>,
    val competitionId: String
) {
    /**
     * Fecha del primer enfrentamiento de la jornada
     */
    val firstMatchDate: LocalDate? get() =
        matches.minByOrNull { it.date }?.date?.toLocalDate()

    /**
     * Fecha del último enfrentamiento de la jornada
     */
    val lastMatchDate: LocalDate? get() =
        matches.maxByOrNull { it.date }?.date?.toLocalDate()

    /**
     * Formatea el rango de fechas de la jornada
     */
    fun getDateRangeFormatted(): String {
        val formatter = DateTimeFormatter.ofPattern("d-M-yy")

        val first = firstMatchDate?.format(formatter) ?: "-"
        val last = lastMatchDate?.format(formatter) ?: "-"

        return if (first == last) first else "$first // $last"
    }
}
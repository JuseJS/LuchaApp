package org.iesharia.domain.model

import kotlinx.datetime.LocalDate

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
        matches.minByOrNull { it.date }?.date?.date

    /**
     * Fecha del último enfrentamiento de la jornada
     */
    val lastMatchDate: LocalDate? get() =
        matches.maxByOrNull { it.date }?.date?.date

    /**
     * Formatea el rango de fechas de la jornada
     */
    fun getDateRangeFormatted(): String {
        val first = firstMatchDate?.toString()?.split("-")?.let { "${it[2]}-${it[1]}-${it[0].takeLast(2)}" } ?: "-"
        val last = lastMatchDate?.toString()?.split("-")?.let { "${it[2]}-${it[1]}-${it[0].takeLast(2)}" } ?: "-"

        return if (first == last) first else "$first // $last"
    }
}
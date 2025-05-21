package org.iesharia.core.ui.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * Formatea una fecha en formato legible
 */
fun formatDate(date: LocalDate, locale: Locale = Locale("es", "ES")): String {
    val javaDate = date.toJavaLocalDate()
    val formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.MEDIUM)
        .withLocale(locale)

    return formatter.format(javaDate)
}
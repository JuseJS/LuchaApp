package org.iesharia.core.ui.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime
import org.iesharia.features.wrestlers.ui.util.getEffectivenessColor
import androidx.compose.ui.graphics.Color

/**
 * Utilidades para formateo de datos en la UI.
 * Esta clase consolida diferentes funciones de formato que estaban dispersas
 * en el proyecto, siguiendo el principio DRY (Don't Repeat Yourself).
 */
object FormatUtils {

    /**
     * Formatos para fechas
     */
    object Date {
        /**
         * Formatea una fecha LocalDateTime a formato corto dd/MM/yyyy
         */
        fun formatShortDate(date: LocalDateTime): String {
            return "${date.dayOfMonth.toString().padStart(2, '0')}/${date.monthNumber.toString().padStart(2, '0')}/${date.year}"
        }
        
        /**
         * Formatea una hora LocalDateTime a formato corto HH:mm
         */
        fun formatShortTime(date: LocalDateTime): String {
            return "${date.hour.toString().padStart(2, '0')}:${date.minute.toString().padStart(2, '0')}"
        }
        
        /**
         * Formatea una fecha y hora LocalDateTime a formato corto dd/MM/yyyy - HH:mm
         */
        fun formatShortDateTime(date: LocalDateTime): String {
            return "${formatShortDate(date)} - ${formatShortTime(date)}"
        }
        
        /**
         * Formatea una fecha LocalDateTime a formato largo "d de MMMM de yyyy"
         */
        fun formatLongDate(date: LocalDateTime): String {
            val month = when (date.monthNumber) {
                1 -> "enero"
                2 -> "febrero"
                3 -> "marzo"
                4 -> "abril"
                5 -> "mayo"
                6 -> "junio"
                7 -> "julio"
                8 -> "agosto"
                9 -> "septiembre"
                10 -> "octubre"
                11 -> "noviembre"
                12 -> "diciembre"
                else -> ""
            }
            
            return "${date.dayOfMonth} de $month de ${date.year}"
        }
    }
    
    /**
     * Formatos para números y métricas
     */
    object Number {
        /**
         * Formatea un porcentaje con precisión específica
         */
        fun formatPercentage(value: Double, precision: Int = 1): String {
            return String.format("%.${precision}f%%", value)
        }
        
        /**
         * Formatea un número decimal con precisión específica
         */
        fun formatDecimal(value: Double, precision: Int = 1): String {
            return String.format("%.${precision}f", value)
        }
        
        /**
         * Devuelve el color adecuado según un valor de efectividad
         */
        @Composable
        fun getPerformanceColor(effectivenessPercentage: Double): Color {
            return getEffectivenessColor(effectivenessPercentage)
        }
    }
    
    /**
     * Formatos para textos
     */
    object Text {
        /**
         * Trunca un texto a una longitud máxima añadiendo "..." si es necesario
         */
        fun truncate(text: String, maxLength: Int): String {
            return if (text.length <= maxLength) {
                text
            } else {
                text.take(maxLength - 3) + "..."
            }
        }
        
        /**
         * Capitaliza la primera letra de cada palabra
         */
        fun capitalizeWords(text: String): String {
            return text.split(" ")
                .filter { it.isNotEmpty() }
                .joinToString(" ") { word ->
                    word.first().uppercase() + word.drop(1).lowercase()
                }
        }
    }
}
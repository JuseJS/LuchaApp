package org.iesharia.domain.model

import androidx.compose.ui.graphics.Color

/**
 * Modelo para representar los resultados de un luchador en un enfrentamiento
 */
data class WrestlerMatchResult(
    val opponentName: String,
    val result: Result,
    val fouls: Int,
    val opponentFouls: Int,
    val category: String,
    val classification: String,
    val date: String
) {
    enum class Result {
        WIN, LOSS, DRAW, EXPELLED, SEPARATED;

        fun displayName(): String = when(this) {
            WIN -> "Victoria"
            LOSS -> "Derrota"
            DRAW -> "Empate"
            EXPELLED -> "Expulsión por faltas"
            SEPARATED -> "Separación"
        }

        fun color(): Color = when(this) {
            WIN -> Color(0xFF4CAF50)
            LOSS -> Color(0xFFF44336)
            DRAW -> Color(0xFFFF9800)
            EXPELLED -> Color(0xFF9C27B0)
            SEPARATED -> Color(0xFF607D8B)
        }
    }
}
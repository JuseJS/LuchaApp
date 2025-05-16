package org.iesharia.features.wrestlers.domain.model

import androidx.compose.ui.graphics.Color
import org.iesharia.core.resources.AppStrings

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
            WIN -> AppStrings.Wrestlers.Results.win
            LOSS -> AppStrings.Wrestlers.Results.loss
            DRAW -> AppStrings.Wrestlers.Results.draw
            EXPELLED -> AppStrings.Wrestlers.Results.expelled
            SEPARATED -> AppStrings.Wrestlers.Results.separated
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
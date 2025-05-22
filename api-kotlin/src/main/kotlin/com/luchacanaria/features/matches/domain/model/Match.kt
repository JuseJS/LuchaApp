package com.luchacanaria.features.matches.domain.model

import kotlinx.datetime.LocalDateTime
import com.luchacanaria.features.teams.domain.model.Team

data class Match(
    val id: String,
    val localTeam: Team,
    val visitorTeam: Team,
    val localScore: Int? = null,
    val visitorScore: Int? = null,
    val date: LocalDateTime,
    val venue: String,
    val completed: Boolean = false,
    val hasAct: Boolean = false
) {
    val winner: Team? get() = when {
        !completed || localScore == null || visitorScore == null -> null
        localScore > visitorScore -> localTeam
        visitorScore > localScore -> visitorTeam
        else -> null
    }

    val isDrawn: Boolean get() =
        completed && localScore != null && visitorScore != null && localScore == visitorScore
}
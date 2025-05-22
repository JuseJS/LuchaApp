package com.luchacanaria.features.teams.domain.model

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory

data class Team(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: Island,
    val venue: String,
    val divisionCategory: DivisionCategory = DivisionCategory.PRIMERA
)
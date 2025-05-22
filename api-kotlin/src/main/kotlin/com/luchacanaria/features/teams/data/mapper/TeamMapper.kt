package com.luchacanaria.features.teams.data.mapper

import com.luchacanaria.features.teams.data.document.TeamDocument
import com.luchacanaria.features.teams.domain.model.Team
import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory

object TeamMapper {
    fun toDomain(document: TeamDocument): Team {
        return Team(
            id = document.id,
            name = document.name,
            imageUrl = document.imageUrl,
            island = Island.valueOf(document.island),
            venue = document.venue,
            divisionCategory = DivisionCategory.valueOf(document.divisionCategory)
        )
    }

    fun toDocument(team: Team): TeamDocument {
        return TeamDocument(
            name = team.name,
            imageUrl = team.imageUrl,
            island = team.island.name,
            venue = team.venue,
            divisionCategory = team.divisionCategory.name
        )
    }
}
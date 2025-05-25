package org.iesharia.core.network.mapper

import org.iesharia.core.domain.model.Island
import org.iesharia.core.network.dto.TeamDto
import org.iesharia.features.competitions.domain.model.DivisionCategory
import org.iesharia.features.teams.domain.model.Team

object TeamMappers {
    
    fun TeamDto.toDomain(): Team {
        return Team(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            island = Island.valueOf(this.island),
            venue = this.venue,
            divisionCategory = DivisionCategory.valueOf(this.divisionCategory)
        )
    }
    
    fun Team.toDto(): TeamDto {
        return TeamDto(
            id = this.id,
            name = this.name,
            imageUrl = this.imageUrl,
            island = this.island.name,
            venue = this.venue,
            divisionCategory = this.divisionCategory.name
        )
    }
}
package org.iesharia.core.domain.model

import org.iesharia.features.competitions.domain.model.Competition
import org.iesharia.features.teams.domain.model.Team
import org.iesharia.features.wrestlers.domain.model.Wrestler

/**
 * Tipos de favoritos que pueden añadirse
 */
sealed class Favorite {
    data class TeamFavorite(val team: Team) : Favorite()
    data class CompetitionFavorite(val competition: Competition) : Favorite()
    data class WrestlerFavorite(val wrestler: Wrestler) : Favorite()
}
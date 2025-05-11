package org.iesharia.domain.model

/**
 * Tipos de favoritos que pueden añadirse
 */
sealed class Favorite {
    data class TeamFavorite(val team: Team) : Favorite()
    data class CompetitionFavorite(val competition: Competition) : Favorite()
    data class WrestlerFavorite(val wrestler: Wrestler) : Favorite()
}
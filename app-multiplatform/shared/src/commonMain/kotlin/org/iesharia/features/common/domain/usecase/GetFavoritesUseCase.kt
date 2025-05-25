package org.iesharia.features.common.domain.usecase

import org.iesharia.core.domain.model.AppError
import org.iesharia.core.domain.model.Favorite
import org.iesharia.core.network.dto.EntityTypeDto
import org.iesharia.features.common.domain.repository.FavoriteRepository
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.teams.domain.repository.TeamRepository
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository

class GetFavoritesUseCase(
    private val favoriteRepository: FavoriteRepository,
    private val teamRepository: TeamRepository,
    private val wrestlerRepository: WrestlerRepository,
    private val competitionRepository: CompetitionRepository
) {
    suspend operator fun invoke(): Result<List<Favorite>> {
        return try {
            favoriteRepository.getFavorites().fold(
                onSuccess = { favoriteInfos ->
                    val favorites = mutableListOf<Favorite>()
                    
                    favoriteInfos.forEach { favoriteInfo ->
                        when (favoriteInfo.entityType) {
                            "TEAM" -> {
                                teamRepository.getTeam(favoriteInfo.entityId)?.let { team ->
                                    favorites.add(Favorite.TeamFavorite(team))
                                }
                            }
                            "WRESTLER" -> {
                                wrestlerRepository.getWrestler(favoriteInfo.entityId)?.let { wrestler ->
                                    favorites.add(Favorite.WrestlerFavorite(wrestler))
                                }
                            }
                            "COMPETITION" -> {
                                competitionRepository.getCompetition(favoriteInfo.entityId)?.let { competition ->
                                    favorites.add(Favorite.CompetitionFavorite(competition))
                                }
                            }
                        }
                    }
                    
                    Result.success(favorites)
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(AppError.UnknownError(e, "Error al obtener favoritos"))
        }
    }
}
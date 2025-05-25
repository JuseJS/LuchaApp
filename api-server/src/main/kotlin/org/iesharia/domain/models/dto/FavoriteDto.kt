package org.iesharia.domain.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    val id: String,
    val userId: String,
    val entityId: String,
    val entityType: String,
    val createdAt: String
)

@Serializable
data class CreateFavoriteRequest(
    val entityId: String,
    val entityType: String
)

@Serializable
data class FavoritesResponse(
    val favorites: List<FavoriteDto>,
    val total: Int
)
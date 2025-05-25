package org.iesharia.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequestDto(
    val entityId: String,
    val entityType: EntityTypeDto
)

@Serializable
data class FavoriteResponseDto(
    val id: String,
    val userId: String,
    val entityId: String,
    val entityType: EntityTypeDto,
    val createdAt: String
)

@Serializable
enum class EntityTypeDto {
    TEAM,
    WRESTLER,
    COMPETITION
}

@Serializable
data class FavoriteStatusDto(
    val isFavorite: Boolean
)
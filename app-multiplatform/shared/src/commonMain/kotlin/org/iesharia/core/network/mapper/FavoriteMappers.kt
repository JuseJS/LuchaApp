package org.iesharia.core.network.mapper

import org.iesharia.core.network.dto.FavoriteResponseDto

data class FavoriteInfo(
    val entityId: String,
    val entityType: String
)

fun FavoriteResponseDto.toDomain(): FavoriteInfo = FavoriteInfo(
    entityId = this.entityId,
    entityType = this.entityType.name
)
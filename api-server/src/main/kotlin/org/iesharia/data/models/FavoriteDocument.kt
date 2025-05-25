package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant
import org.bson.types.ObjectId

@Serializable
data class FavoriteDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val userId: String,
    val entityId: String,
    val entityType: EntityType,
    @Contextual
    val createdAt: Instant
) {
    val id: String get() = _id.toHexString()
}

@Serializable
enum class EntityType {
    TEAM,
    WRESTLER,
    COMPETITION
}
package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
@Serializable
data class TeamDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val name: String,
    val imageUrl: String,
    val island: String,
    val venue: String,
    val divisionCategory: String
) {
    val id: String get() = _id.toHexString()
}
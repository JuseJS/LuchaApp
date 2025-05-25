package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class CompetitionDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val name: String,
    val ageCategory: String,
    val divisionCategory: String,
    val island: String,
    val season: String,
    val teamIds: List<String> = emptyList()
) {
    val id: String get() = _id.toHexString()
}
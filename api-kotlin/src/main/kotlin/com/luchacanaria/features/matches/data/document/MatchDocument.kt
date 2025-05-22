package com.luchacanaria.features.matches.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class MatchDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val localTeamId: String,
    val visitorTeamId: String,
    val localScore: Int? = null,
    val visitorScore: Int? = null,
    val matchDateTime: String, // ISO DateTime string
    val venue: String,
    val competitionId: String,
    val matchDayNumber: Int,
    val completed: Boolean = false,
    val hasAct: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
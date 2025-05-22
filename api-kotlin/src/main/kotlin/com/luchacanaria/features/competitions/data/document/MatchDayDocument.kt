package com.luchacanaria.features.competitions.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class MatchDayDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val number: Int,
    val competitionId: String,
    val matchIds: List<String> = emptyList(),
    val ended: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
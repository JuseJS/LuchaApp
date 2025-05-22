package com.luchacanaria.features.teams.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class TeamDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val name: String,
    val imageUrl: String,
    val island: String, // Island enum as string
    val venue: String,
    val divisionCategory: String = "PRIMERA", // DivisionCategory enum as string
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
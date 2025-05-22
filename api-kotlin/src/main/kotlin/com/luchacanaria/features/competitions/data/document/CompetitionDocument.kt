package com.luchacanaria.features.competitions.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class CompetitionDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val name: String,
    val ageCategory: String, // AgeCategory enum as string
    val divisionCategory: String, // DivisionCategory enum as string
    val island: String, // Island enum as string
    val season: String,
    val teamIds: List<String> = emptyList(),
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
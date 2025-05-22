package com.luchacanaria.features.wrestlers.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class WrestlerDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val licenseNumber: String,
    val name: String,
    val surname: String,
    val imageUrl: String? = null,
    val teamId: String,
    val category: String, // WrestlerCategory enum as string
    val classification: String, // WrestlerClassification enum as string
    val height: Int? = null,
    val weight: Int? = null,
    val birthDate: String? = null, // ISO format date string
    val nickname: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
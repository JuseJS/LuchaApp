package com.luchacanaria.features.auth.data.document

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class UserDocument(
    @BsonId
    @Contextual
    val _id: ObjectId = ObjectId(),
    val email: String,
    val passwordHash: String,
    val name: String,
    val surname: String,
    val role: String = "GUEST",
    val associatedTeamId: String? = null,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val id: String get() = _id.toHexString()
}
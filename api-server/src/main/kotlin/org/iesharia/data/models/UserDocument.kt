package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import kotlinx.datetime.Instant
import org.iesharia.domain.models.UserRole

@Serializable
data class UserDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val email: String,
    val password: String, // Hashed password
    val name: String,
    val surname: String,
    val phone: String? = null,
    val role: UserRole,
    val permissions: List<String> = emptyList(),
    val associatedTeamId: String? = null,
    val isActive: Boolean = true,
    @Contextual
    val createdAt: Instant,
    @Contextual
    val updatedAt: Instant,
    @Contextual
    val lastLogin: Instant? = null
) {
    val id: String get() = _id.toHexString()
}
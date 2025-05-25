package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId

@Serializable
data class RefereeDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val name: String,
    val licenseNumber: String,
    val isMain: Boolean = false,
    val isActive: Boolean = true
) {
    val id: String get() = _id.toHexString()
}
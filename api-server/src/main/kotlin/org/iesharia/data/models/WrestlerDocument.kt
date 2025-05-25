package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
import kotlinx.datetime.LocalDate
@Serializable
data class WrestlerDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val licenseNumber: String,
    val name: String,
    val surname: String,
    val imageUrl: String? = null,
    val teamId: String,
    val category: String,
    val classification: String,
    val height: Int? = null,
    val weight: Int? = null,
    @Contextual
    val birthDate: LocalDate? = null,
    val nickname: String? = null
) {
    val id: String get() = _id.toHexString()
}
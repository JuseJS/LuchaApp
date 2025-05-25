package org.iesharia.data.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import org.bson.types.ObjectId
import kotlinx.datetime.LocalDateTime

@Serializable
data class MatchDocument(
    @SerialName("_id")
    @Contextual
    val _id: ObjectId = ObjectId(),
    val localTeamId: String,
    val visitorTeamId: String,
    val localScore: Int? = null,
    val visitorScore: Int? = null,
    @Contextual
    val date: LocalDateTime,
    val venue: String,
    val completed: Boolean = false,
    val hasAct: Boolean = false,
    val competitionId: String? = null,
    val round: Int? = null
) {
    val id: String get() = _id.toHexString()
}
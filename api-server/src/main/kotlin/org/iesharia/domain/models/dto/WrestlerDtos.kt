package org.iesharia.domain.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class WrestlerDto(
    val id: String,
    val licenseNumber: String,
    val name: String,
    val surname: String,
    val imageUrl: String?,
    val teamId: String,
    val category: String,
    val classification: String,
    val height: Int?,
    val weight: Int?,
    val birthDate: String?, // ISO format
    val nickname: String?
)

@Serializable
data class CreateWrestlerRequest(
    val licenseNumber: String,
    val name: String,
    val surname: String,
    val imageUrl: String? = null,
    val teamId: String,
    val category: String,
    val classification: String,
    val height: Int? = null,
    val weight: Int? = null,
    val birthDate: String? = null,
    val nickname: String? = null
)

@Serializable
data class UpdateWrestlerRequest(
    val name: String? = null,
    val surname: String? = null,
    val imageUrl: String? = null,
    val teamId: String? = null,
    val category: String? = null,
    val classification: String? = null,
    val height: Int? = null,
    val weight: Int? = null,
    val birthDate: String? = null,
    val nickname: String? = null
)

@Serializable
data class WrestlerFilters(
    val teamId: String? = null,
    val category: String? = null,
    val classification: String? = null,
    val search: String? = null,
    val page: Int = 1,
    val limit: Int = 20
)

@Serializable
data class WrestlerResultDto(
    val id: String,
    val matchId: String,
    val opponentId: String,
    val opponentName: String,
    val opponentClassification: String,
    val teamName: String,
    val date: String,
    val result: String,
    val score: Double,
    val falls: Int,
    val penalties: Int
)
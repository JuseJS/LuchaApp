package org.iesharia.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class WrestlerDto(
    val id: String,
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
    val nickname: String? = null
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
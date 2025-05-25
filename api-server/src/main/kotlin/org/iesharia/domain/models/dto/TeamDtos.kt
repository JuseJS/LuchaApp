package org.iesharia.domain.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: String,
    val venue: String,
    val divisionCategory: String
)

@Serializable
data class CreateTeamRequest(
    val name: String,
    val imageUrl: String,
    val island: String,
    val venue: String,
    val divisionCategory: String
)

@Serializable
data class UpdateTeamRequest(
    val name: String? = null,
    val imageUrl: String? = null,
    val island: String? = null,
    val venue: String? = null,
    val divisionCategory: String? = null
)

@Serializable
data class TeamFilters(
    val island: String? = null,
    val divisionCategory: String? = null,
    val search: String? = null,
    val page: Int = 1,
    val limit: Int = 20
)
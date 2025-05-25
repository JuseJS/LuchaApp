package org.iesharia.core.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TeamDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val island: String, // Island enum as string
    val venue: String,
    val divisionCategory: String // DivisionCategory enum as string
)

@Serializable
data class CreateTeamRequest(
    val name: String,
    val imageUrl: String,
    val island: String,
    val venue: String,
    val divisionCategory: String = "PRIMERA"
)

@Serializable
data class UpdateTeamRequest(
    val name: String,
    val imageUrl: String,
    val island: String,
    val venue: String,
    val divisionCategory: String
)
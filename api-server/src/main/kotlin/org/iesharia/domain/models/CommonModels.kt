package org.iesharia.domain.models

import kotlinx.serialization.Serializable

/**
 * Common models shared between API and app
 * These models should be identical in both projects
 */

@Serializable
data class MatchDay(
    val id: String,
    val number: Int,
    val date: String, // ISO format
    val matches: List<String> = emptyList() // Match IDs
)

@Serializable
data class IndividualMatch(
    val wrestlerId: String,
    val opponentId: String,
    val result: MatchResult,
    val score: Double,
    val falls: Int = 0,
    val penalties: Int = 0
)

@Serializable
data class MatchStatistics(
    val localScore: Double,
    val visitorScore: Double,
    val individualMatches: List<IndividualMatch> = emptyList()
)
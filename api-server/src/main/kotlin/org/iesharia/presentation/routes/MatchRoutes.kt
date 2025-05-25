package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.services.MatchService
import org.koin.ktor.ext.inject

fun Route.matchRoutes() {
    val matchService by inject<MatchService>()
    
    route("/matches") {
        // Get match by ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Match ID is required")
            )
            
            val match = matchService.getMatchById(id)
            if (match != null) {
                call.respond(match)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match not found"))
            }
        }
        
        // Get matches by team ID
        get("/team/{teamId}") {
            val teamId = call.parameters["teamId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Team ID is required")
            )
            
            val matches = matchService.getMatchesByTeamId(teamId)
            call.respond(matches)
        }
        
        // Get upcoming matches
        get("/upcoming") {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val matches = matchService.getUpcomingMatches(limit)
            call.respond(matches)
        }
        
        // Get recent matches
        get("/recent") {
            val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 10
            val matches = matchService.getRecentMatches(limit)
            call.respond(matches)
        }
        
        // Get matches by competition
        get("/competition/{competitionId}") {
            val competitionId = call.parameters["competitionId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Competition ID is required")
            )
            
            val matches = matchService.getMatchesByCompetitionId(competitionId)
            call.respond(matches)
        }
        
        // Get matches by date range
        get("/daterange") {
            val startDate = call.request.queryParameters["startDate"]
            val endDate = call.request.queryParameters["endDate"]
            
            if (startDate == null || endDate == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Both startDate and endDate are required")
                )
                return@get
            }
            
            val matches = matchService.getMatchesByDateRange(startDate, endDate)
            call.respond(matches)
        }
    }
}
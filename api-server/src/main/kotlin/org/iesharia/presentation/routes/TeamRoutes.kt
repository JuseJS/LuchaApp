package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.services.TeamService
import org.koin.ktor.ext.inject

fun Route.teamRoutes() {
    val teamService by inject<TeamService>()
    
    route("/teams") {
        // Get all teams
        get {
            val teams = teamService.getAllTeams()
            call.respond(teams)
        }
        
        // Get teams by division
        get("/division/{division}") {
            val division = call.parameters["division"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Division parameter is required")
            )
            
            val teams = teamService.getTeamsByDivision(division)
            call.respond(teams)
        }
        
        // Get team by ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Team ID is required")
            )
            
            val team = teamService.getTeamById(id)
            if (team != null) {
                call.respond(team)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Team not found"))
            }
        }
        
        // Get wrestlers by team ID
        get("/{id}/wrestlers") {
            val teamId = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Team ID is required")
            )
            
            val wrestlers = teamService.getWrestlersByTeamId(teamId)
            call.respond(wrestlers)
        }
        
        // Search teams
        get("/search") {
            val query = call.request.queryParameters["q"] ?: ""
            val teams = teamService.searchTeams(query)
            call.respond(teams)
        }
    }
}
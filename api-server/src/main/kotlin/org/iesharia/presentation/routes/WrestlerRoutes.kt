package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.services.WrestlerService
import org.koin.ktor.ext.inject

fun Route.wrestlerRoutes() {
    val wrestlerService by inject<WrestlerService>()
    
    route("/wrestlers") {
        // Get all wrestlers
        get {
            val wrestlers = wrestlerService.getAllWrestlers()
            call.respond(wrestlers)
        }
        
        // Get wrestler by ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Wrestler ID is required")
            )
            
            val wrestler = wrestlerService.getWrestlerById(id)
            if (wrestler != null) {
                call.respond(wrestler)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Wrestler not found"))
            }
        }
        
        // Get wrestlers by team ID
        get("/team/{teamId}") {
            val teamId = call.parameters["teamId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Team ID is required")
            )
            
            val wrestlers = wrestlerService.getWrestlersByTeamId(teamId)
            call.respond(wrestlers)
        }
        
        // Search wrestlers
        get("/search") {
            val query = call.request.queryParameters["q"] ?: ""
            val wrestlers = wrestlerService.searchWrestlers(query)
            call.respond(wrestlers)
        }
    }
}
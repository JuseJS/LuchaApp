package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.services.CompetitionService
import org.koin.ktor.ext.inject

fun Route.competitionRoutes() {
    val competitionService by inject<CompetitionService>()
    
    route("/competitions") {
        // Get all competitions
        get {
            val competitions = competitionService.getAllCompetitions()
            call.respond(competitions)
        }
        
        // Get competition by ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Competition ID is required")
            )
            
            val competition = competitionService.getCompetitionById(id)
            if (competition != null) {
                call.respond(competition)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Competition not found"))
            }
        }
        
        // Get competitions by filters
        get("/filter") {
            val ageCategory = call.request.queryParameters["ageCategory"]
            val divisionCategory = call.request.queryParameters["divisionCategory"]
            val island = call.request.queryParameters["island"]
            val season = call.request.queryParameters["season"]
            
            val competitions = competitionService.getCompetitionsByFilters(
                ageCategory = ageCategory,
                divisionCategory = divisionCategory,
                island = island,
                season = season
            )
            call.respond(competitions)
        }
        
        // Get match days for competition
        get("/{id}/matchdays") {
            val competitionId = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Competition ID is required")
            )
            
            val matchDays = competitionService.getMatchDaysByCompetitionId(competitionId)
            call.respond(matchDays)
        }
        
        // Get teams in competition
        get("/{id}/teams") {
            val competitionId = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Competition ID is required")
            )
            
            val teams = competitionService.getTeamsByCompetitionId(competitionId)
            call.respond(teams)
        }
    }
}
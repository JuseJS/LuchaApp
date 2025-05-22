package com.luchacanaria.features.teams.presentation

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.competitions.domain.model.DivisionCategory
import com.luchacanaria.features.teams.domain.usecase.GetTeamUseCase
import com.luchacanaria.features.teams.domain.usecase.GetTeamsRequest
import com.luchacanaria.features.teams.domain.usecase.GetTeamsUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.teamRoutes(
    getTeamsUseCase: GetTeamsUseCase,
    getTeamUseCase: GetTeamUseCase
) {
    route("/teams") {
        get {
            try {
                val islandParam = call.request.queryParameters["island"]
                val divisionParam = call.request.queryParameters["division"]
                val search = call.request.queryParameters["search"]
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
                
                val island = islandParam?.let {
                    try {
                        Island.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                
                val divisionCategory = divisionParam?.let {
                    try {
                        DivisionCategory.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                
                val request = GetTeamsRequest(
                    island = island,
                    divisionCategory = divisionCategory,
                    search = search,
                    page = page,
                    size = size
                )
                
                val teams = getTeamsUseCase(request)
                call.respond(HttpStatusCode.OK, teams)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error retrieving teams"))
            }
        }
        
        get("/{id}") {
            try {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid team ID"))
                    return@get
                }
                
                val team = getTeamUseCase(id)
                if (team != null) {
                    call.respond(HttpStatusCode.OK, team)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Team not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error retrieving team"))
            }
        }
    }
}
package com.luchacanaria.features.wrestlers.presentation

import com.luchacanaria.core.domain.model.Island
import com.luchacanaria.features.wrestlers.domain.model.WrestlerCategory
import com.luchacanaria.features.wrestlers.domain.model.WrestlerClassification
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlerUseCase
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlersRequest
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlersUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.wrestlerRoutes(
    getWrestlersUseCase: GetWrestlersUseCase,
    getWrestlersByTeamIdUseCase: GetWrestlersByTeamIdUseCase,
    getWrestlerUseCase: GetWrestlerUseCase
) {
    route("/wrestlers") {
        get {
            try {
                val teamId = call.request.queryParameters["teamId"]
                val categoryParam = call.request.queryParameters["category"]
                val classificationParam = call.request.queryParameters["classification"]
                val islandParam = call.request.queryParameters["island"]
                val search = call.request.queryParameters["search"]
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 20
                
                val category = categoryParam?.let {
                    try {
                        WrestlerCategory.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                
                val classification = classificationParam?.let {
                    try {
                        WrestlerClassification.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                
                val island = islandParam?.let {
                    try {
                        Island.valueOf(it.uppercase())
                    } catch (e: IllegalArgumentException) {
                        null
                    }
                }
                
                val request = GetWrestlersRequest(
                    teamId = teamId,
                    category = category,
                    classification = classification,
                    island = island,
                    search = search,
                    page = page,
                    size = size
                )
                
                val wrestlers = getWrestlersUseCase(request)
                call.respond(HttpStatusCode.OK, wrestlers)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error retrieving wrestlers"))
            }
        }
        
        get("/{id}") {
            try {
                val id = call.parameters["id"]
                if (id.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid wrestler ID"))
                    return@get
                }
                
                val wrestler = getWrestlerUseCase(id)
                if (wrestler != null) {
                    call.respond(HttpStatusCode.OK, wrestler)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Wrestler not found"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error retrieving wrestler"))
            }
        }
        
        get("/team/{teamId}") {
            try {
                val teamId = call.parameters["teamId"]
                if (teamId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid team ID"))
                    return@get
                }
                
                val wrestlers = getWrestlersByTeamIdUseCase(teamId)
                call.respond(HttpStatusCode.OK, wrestlers)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error retrieving team wrestlers"))
            }
        }
    }
}
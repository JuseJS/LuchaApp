package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.models.dto.MatchActDto
import org.iesharia.domain.services.MatchActService
import org.koin.ktor.ext.inject

fun Route.matchActRoutes() {
    val matchActService by inject<MatchActService>()
    
    // Get available referees
    get("/referees/available") {
        val referees = matchActService.getAvailableReferees()
        call.respond(referees)
    }
    
    route("/matchacts") {
        // Get match act by ID
        get("/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Match Act ID is required")
            )
            
            val matchAct = matchActService.getMatchActById(id)
            if (matchAct != null) {
                call.respond(matchAct)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match Act not found"))
            }
        }
        
        // Get match act by match ID
        get("/match/{matchId}") {
            val matchId = call.parameters["matchId"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Match ID is required")
            )
            
            try {
                val matchAct = matchActService.getMatchActByMatchId(matchId)
                if (matchAct != null) {
                    call.respond(matchAct)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match Act not found for this match"))
                }
            } catch (e: Exception) {
                println("MatchActRoutes: Error obteniendo acta para match $matchId - ${e.message}")
                e.printStackTrace()
                // Si hay un error de serialización, devolver NotFound en lugar de BadRequest
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match Act not found or corrupted"))
            }
        }
        
        
        // Create match act
        post {
            try {
                println("MatchActRoutes: Recibiendo POST request...")
                
                val matchActDto = call.receive<MatchActDto>()
                println("MatchActRoutes: DTO deserializado - matchId: ${matchActDto.matchId}")
                println("MatchActRoutes: localTeam - teamId: '${matchActDto.localTeam.teamId}', clubName: '${matchActDto.localTeam.clubName}'")
                println("MatchActRoutes: visitorTeam - teamId: '${matchActDto.visitorTeam.teamId}', clubName: '${matchActDto.visitorTeam.clubName}'")
                
                val result = matchActService.createMatchAct(matchActDto)
                call.respond(HttpStatusCode.Created, result)
            } catch (e: Exception) {
                println("MatchActRoutes: Error en POST - ${e.message}")
                println("MatchActRoutes: Error tipo: ${e::class.simpleName}")
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to (e.message ?: "Invalid match act data"))
                )
            }
        }
        
        // Update match act
        put("/{id}") {
            val id = call.parameters["id"] ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Match Act ID is required")
            )
            
            try {
                val matchActDto = call.receive<MatchActDto>()
                val result = matchActService.updateMatchAct(id, matchActDto)
                if (result != null) {
                    call.respond(result)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match Act not found"))
                }
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to (e.message ?: "Invalid match act data"))
                )
            }
        }
        
        // Complete match act
        post("/{id}/complete") {
            val id = call.parameters["id"] ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Match Act ID is required")
            )
            
            val result = matchActService.completeMatchAct(id)
            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "Match Act not found"))
            }
        }
    }
}
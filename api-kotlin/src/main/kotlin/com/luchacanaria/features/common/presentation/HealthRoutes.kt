package com.luchacanaria.features.common.presentation

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.datetime.Clock

fun Route.healthRoutes() {
    get("/health") {
        call.respond(HttpStatusCode.OK, mapOf(
            "status" to "healthy",
            "timestamp" to Clock.System.now().toString(),
            "service" to "Lucha Canaria API",
            "version" to "1.0.0"
        ))
    }
    
    get("/") {
        call.respond(HttpStatusCode.OK, mapOf(
            "message" to "🤼 Lucha Canaria API - Ready to serve!",
            "version" to "1.0.0",
            "endpoints" to mapOf(
                "auth" to "/api/v1/auth/login",
                "wrestlers" to "/api/v1/wrestlers",
                "teams" to "/api/v1/teams",
                "health" to "/health"
            ),
            "docs" to "See API_COMPLETE_GUIDE.md for full documentation"
        ))
    }
}
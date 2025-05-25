package org.iesharia.config

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.iesharia.presentation.routes.*

fun Application.configureRouting() {
    routing {
        route("/api/v1") {
            healthRoutes()
            authRoutes()
            userRoutes()
            wrestlerRoutes()
            teamRoutes()
            competitionRoutes()
            matchRoutes()
            matchActRoutes()
            favoriteRoutes()
        }
    }
}
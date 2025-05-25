package org.iesharia.presentation.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.iesharia.domain.models.dto.*
import org.iesharia.domain.services.AuthService
import org.koin.ktor.ext.inject

fun Route.authRoutes() {
    val authService by inject<AuthService>()
    
    route("/auth") {
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                val response = authService.login(request)
                call.respond(HttpStatusCode.OK, response)
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Nothing>(
                        success = false,
                        message = e.message
                    )
                )
            } catch (e: SecurityException) {
                call.respond(
                    HttpStatusCode.Forbidden,
                    ApiResponse<Nothing>(
                        success = false,
                        message = e.message
                    )
                )
            }
        }
        
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                val response = authService.register(request)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Nothing>(
                        success = false,
                        message = e.message
                    )
                )
            }
        }
    }
}
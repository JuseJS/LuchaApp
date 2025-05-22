package com.luchacanaria.features.auth.presentation

import com.luchacanaria.features.auth.domain.usecase.LoginRequest
import com.luchacanaria.features.auth.domain.usecase.LoginUseCase
import com.luchacanaria.features.auth.domain.usecase.RegisterRequest
import com.luchacanaria.features.auth.domain.usecase.RegisterUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    loginUseCase: LoginUseCase,
    registerUseCase: RegisterUseCase
) {
    route("/auth") {
        post("/login") {
            try {
                val request = call.receive<LoginRequest>()
                
                // Basic validation
                if (request.email.isBlank() || request.password.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Email and password are required"))
                    return@post
                }
                
                val result = loginUseCase(request)
                if (result != null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, mapOf("error" to "Invalid credentials"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request format"))
            }
        }
        
        post("/register") {
            try {
                val request = call.receive<RegisterRequest>()
                
                // Basic validation
                if (request.email.isBlank() || request.password.isBlank() || 
                    request.name.isBlank() || request.surname.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "All fields are required"))
                    return@post
                }
                
                val result = registerUseCase(request)
                if (result != null) {
                    call.respond(HttpStatusCode.Created, result)
                } else {
                    call.respond(HttpStatusCode.Conflict, mapOf("error" to "User already exists"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid request format"))
            }
        }
    }
}
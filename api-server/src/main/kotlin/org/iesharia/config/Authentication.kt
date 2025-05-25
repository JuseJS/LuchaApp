package org.iesharia.config

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import org.iesharia.domain.models.dto.ErrorResponse
import org.iesharia.utils.JwtManager
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val jwtManager by inject<JwtManager>()
    val jwtSecret = EnvironmentConfig.requireEnv("JWT_SECRET")
    val jwtIssuer = EnvironmentConfig.getEnv("JWT_ISSUER", "luchaapp-api")!!
    val jwtAudience = EnvironmentConfig.getEnv("JWT_AUDIENCE", "luchaapp-client")!!
    
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwtManager.verifier)
            
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            
            challenge { _, _ ->
                call.respond(
                    io.ktor.http.HttpStatusCode.Unauthorized,
                    ErrorResponse(
                        error = "Unauthorized",
                        message = "Token is not valid or has expired",
                        statusCode = io.ktor.http.HttpStatusCode.Unauthorized.value,
                        timestamp = kotlinx.datetime.Clock.System.now().toString()
                    )
                )
            }
        }
    }
}
package org.iesharia.config

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import org.iesharia.domain.models.dto.ErrorResponse

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = "Bad Request",
                    message = cause.message ?: "Invalid request parameters",
                    statusCode = HttpStatusCode.BadRequest.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
        
        exception<NoSuchElementException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    error = "Not Found",
                    message = cause.message ?: "Resource not found",
                    statusCode = HttpStatusCode.NotFound.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
        
        exception<SecurityException> { call, cause ->
            call.respond(
                HttpStatusCode.Forbidden,
                ErrorResponse(
                    error = "Forbidden",
                    message = cause.message ?: "Access denied",
                    statusCode = HttpStatusCode.Forbidden.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
        
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "Internal Server Error",
                    message = "An unexpected error occurred",
                    statusCode = HttpStatusCode.InternalServerError.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
        
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Not Found",
                    message = "The requested resource was not found",
                    statusCode = status.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
        
        status(HttpStatusCode.Unauthorized) { call, status ->
            call.respond(
                status,
                ErrorResponse(
                    error = "Unauthorized",
                    message = "Authentication required",
                    statusCode = status.value,
                    timestamp = Clock.System.now().toString()
                )
            )
        }
    }
}
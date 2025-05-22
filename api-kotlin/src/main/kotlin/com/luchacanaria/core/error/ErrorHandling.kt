package com.luchacanaria.core.error

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: String? = null,
    val timestamp: String = kotlinx.datetime.Clock.System.now().toString()
) {
    companion object {
        fun <T> success(data: T) = ApiResponse(success = true, data = data)
        fun <T> error(message: String) = ApiResponse<T>(success = false, error = message)
    }
}

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<ValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse.error<Any>("Validación fallida: ${cause.message}")
            )
        }

        exception<AuthenticationException> { call, cause ->
            call.respond(
                HttpStatusCode.Unauthorized,
                ApiResponse.error<Any>("Autenticación requerida: ${cause.message}")
            )
        }

        exception<NotFoundException> { call, cause ->
            call.respond(
                HttpStatusCode.NotFound,
                ApiResponse.error<Any>("Recurso no encontrado: ${cause.message}")
            )
        }

        exception<ConflictException> { call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ApiResponse.error<Any>("Conflicto: ${cause.message}")
            )
        }

        exception<Exception> { call, cause ->
            call.application.log.error("Error no manejado", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse.error<Any>("Error interno del servidor")
            )
        }
    }
}

class ValidationException(message: String) : Exception(message)
class AuthenticationException(message: String) : Exception(message)
class NotFoundException(message: String) : Exception(message)
class ConflictException(message: String) : Exception(message)

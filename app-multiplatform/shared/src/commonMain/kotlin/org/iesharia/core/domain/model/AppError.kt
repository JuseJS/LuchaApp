package org.iesharia.core.domain.model

/**
 * Clase sealed que representa los diferentes tipos de errores en la aplicación
 */
sealed class AppError(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Error de red (sin conexión, timeout, etc.)
     */
    class NetworkError(
        override val message: String = "Error de conexión. Comprueba tu red.",
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de servidor (500, 503, etc.)
     */
    class ServerError(
        val code: Int? = null,
        override val message: String = "Error en el servidor. Inténtalo más tarde.",
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de validación (formularios, etc.)
     */
    class ValidationError(
        val field: String? = null,
        override val message: String = "Error de validación",
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de autenticación (credenciales inválidas, token expirado, etc.)
     */
    class AuthError(
        override val message: String = "Error de autenticación",
        override val cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error desconocido o inesperado
     */
    class UnknownError(
        override val cause: Throwable? = null,
        override val message: String = cause?.message ?: "Ha ocurrido un error inesperado."
    ) : AppError(message, cause)
}
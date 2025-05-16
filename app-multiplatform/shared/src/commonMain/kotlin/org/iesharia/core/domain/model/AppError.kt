package org.iesharia.core.domain.model

/**
 * Clase sealed que representa los diferentes tipos de errores en la aplicación
 * Ahora hereda de Exception para poder usar throw
 */
sealed class AppError(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    /**
     * Error de red (sin conexión, timeout, etc.)
     */
    class NetworkError(
        message: String = "Error de conexión. Comprueba tu red y vuelve a intentarlo.",
        cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de servidor (500, 503, etc.)
     */
    class ServerError(
        val code: Int? = null,
        message: String = "Error en el servidor. Inténtalo de nuevo más tarde.",
        cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de validación (formularios, etc.)
     */
    class ValidationError(
        val field: String? = null,
        message: String = "Error de validación",
        cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de autenticación (credenciales inválidas, token expirado, etc.)
     */
    class AuthError(
        message: String = "Error de autenticación",
        cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error de permisos (acceso denegado)
     */
    class PermissionError(
        message: String = "Se requieren permisos para esta acción",
        cause: Throwable? = null
    ) : AppError(message, cause)

    /**
     * Error desconocido o inesperado
     */
    class UnknownError(
        cause: Throwable? = null,
        message: String = cause?.message ?: "Ha ocurrido un error inesperado."
    ) : AppError(message, cause)

    /**
     * Obtiene un mensaje de error legible para el usuario
     */
    fun getUserMessage(): String = message ?: "Error desconocido"
}
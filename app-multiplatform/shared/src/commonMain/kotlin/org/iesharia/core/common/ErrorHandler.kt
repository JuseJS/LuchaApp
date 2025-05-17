package org.iesharia.core.common

import org.iesharia.core.domain.model.AppError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Interfaz para el manejador de errores centralizado
 */
interface ErrorHandler {
    /**
     * Flow de errores global que pueden ser observados por componentes de UI
     */
    val errors: SharedFlow<AppError>

    /**
     * Maneja un error específico
     */
    suspend fun handleError(error: AppError)

    /**
     * Convierte una excepción en un AppError apropiado
     */
    fun convertException(exception: Throwable): AppError
}

/**
 * Implementación por defecto del manejador de errores
 */
class DefaultErrorHandler : ErrorHandler {
    private val _errors = MutableSharedFlow<AppError>()
    override val errors: SharedFlow<AppError> = _errors.asSharedFlow()

    override suspend fun handleError(error: AppError) {
        _errors.emit(error)
    }

    override fun convertException(exception: Throwable): AppError {
        // Si ya es un AppError, simplemente lo devolvemos
        if (exception is AppError) return exception

        // Clasificar excepciones genéricas
        val message = exception.message ?: "Error desconocido"
        val exceptionName = exception::class.simpleName ?: ""

        return when {
            // Errores de red
            exceptionName.contains("Network", ignoreCase = true) ||
                    exceptionName.contains("IO", ignoreCase = true) ||
                    exceptionName.contains("Connection", ignoreCase = true) ||
                    message.contains("host", ignoreCase = true) ||
                    message.contains("connection", ignoreCase = true) ||
                    message.contains("timeout", ignoreCase = true) ->
                AppError.NetworkError(message, exception)

            // Errores de validación
            exceptionName == "IllegalArgumentException" ||
                    message.contains("invalid", ignoreCase = true) ||
                    message.contains("validation", ignoreCase = true) ->
                AppError.ValidationError(message = message, cause = exception)

            // Errores de autenticación
            message.contains("auth", ignoreCase = true) ||
                    message.contains("login", ignoreCase = true) ||
                    message.contains("credential", ignoreCase = true) ||
                    message.contains("token", ignoreCase = true) ->
                AppError.AuthError(message, exception)

            // Otros errores
            else -> AppError.UnknownError(exception, message)
        }
    }
}
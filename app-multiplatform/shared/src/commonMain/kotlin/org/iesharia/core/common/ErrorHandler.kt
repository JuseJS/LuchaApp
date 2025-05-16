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
     * Flow de errores que pueden ser observados por componentes de UI
     */
    val errors: SharedFlow<AppError>

    /**
     * Maneja un error específico
     * @param error El error a manejar
     */
    suspend fun handleError(error: AppError)

    /**
     * Convierte una excepción en un AppError apropiado si no es ya un AppError
     * @param exception La excepción a convertir
     * @return El AppError correspondiente
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

        // Versión compatible con KMP para clasificar excepciones genéricas
        val message = exception.message ?: "Error desconocido"
        val exceptionName = exception::class.simpleName ?: ""

        return when {
            // Identificamos los tipos de error por el mensaje o la clase
            message.contains("host", ignoreCase = true) ||
                    message.contains("connection", ignoreCase = true) ||
                    message.contains("timeout", ignoreCase = true) ||
                    message.contains("network", ignoreCase = true) ||
                    exceptionName.contains("Network", ignoreCase = true) ||
                    exceptionName.contains("IO", ignoreCase = true) ||
                    exceptionName.contains("Connection", ignoreCase = true) ||
                    exceptionName.contains("Socket", ignoreCase = true) ->
                AppError.NetworkError(message, exception)

            message.contains("permission", ignoreCase = true) ||
                    message.contains("denied", ignoreCase = true) ||
                    message.contains("access", ignoreCase = true) ||
                    exceptionName.contains("Security", ignoreCase = true) ||
                    exceptionName.contains("Permission", ignoreCase = true) ->
                AppError.PermissionError(message, exception)

            message.contains("invalid", ignoreCase = true) ||
                    message.contains("validation", ignoreCase = true) ||
                    exceptionName == "IllegalArgumentException" ->
                AppError.ValidationError(message = message, cause = exception)

            message.contains("auth", ignoreCase = true) ||
                    message.contains("login", ignoreCase = true) ||
                    message.contains("credential", ignoreCase = true) ||
                    message.contains("token", ignoreCase = true) ||
                    exceptionName.contains("Auth", ignoreCase = true) ||
                    exceptionName.contains("Credential", ignoreCase = true) ->
                AppError.AuthError(message, exception)

            else -> AppError.UnknownError(exception, message)
        }
    }
}
package org.iesharia.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.iesharia.core.domain.model.AppError

/**
 * ViewModel base que proporciona funcionalidad común para gestión de estado y manejo de errores
 * @param initialState El estado inicial del ViewModel
 * @param errorHandler Manejador de errores a utilizar (opcional)
 */
abstract class BaseViewModel<T>(
    initialState: T,
    protected val errorHandler: ErrorHandler? = null
) : ViewModel() {

    // StateFlow protegido que puede ser actualizado por clases derivadas
    protected val _uiState = MutableStateFlow(initialState)

    // StateFlow inmutable público expuesto a la UI
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    // Flow para errores locales del ViewModel (solo se usa si errorHandler no es null)
    private val _errors = MutableSharedFlow<AppError>()
    val errors: SharedFlow<AppError> = _errors.asSharedFlow()

    /**
     * Actualiza el estado actual de forma segura
     * @param update Función que toma el estado actual y devuelve el nuevo estado
     */
    protected fun updateState(update: (T) -> T) {
        _uiState.update(update)
    }

    /**
     * Lanza una corrutina con manejo de errores
     * @param dispatcher El dispatcher de corrutina a usar
     * @param handleError Función para manejar errores (versión antigua para mantener compatibilidad)
     * @param handleAppError Función para manejar AppError (nueva versión)
     * @param block El bloque a ejecutar
     */
    protected fun launchSafe(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        handleError: ((Exception) -> Unit)? = null,
        handleAppError: (suspend (AppError) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(SupervisorJob() + dispatcher) {
            try {
                block()
            } catch (e: Throwable) {
                // Manejar la excepción según el sistema disponible
                if (e is AppError) {
                    // Ya es un AppError
                    handleAppErrorInternal(e, handleAppError)
                } else if (e is Exception && handleError != null) {
                    // Usar el handler antiguo si está disponible
                    handleError(e)
                } else {
                    // Convertir a AppError y manejar con el nuevo sistema
                    val appError = errorHandler?.convertException(e) ?: AppError.UnknownError(e)
                    handleAppErrorInternal(appError, handleAppError)
                }
            }
        }
    }

    /**
     * Maneja un AppError según la configuración disponible
     */
    private suspend fun handleAppErrorInternal(appError: AppError, handler: (suspend (AppError) -> Unit)?) {
        if (handler != null) {
            // Si hay un handler específico, usarlo
            handler(appError)
        } else if (errorHandler != null) {
            // Si hay un errorHandler global, emitir al flow local y al handler global
            _errors.emit(appError)
            errorHandler.handleError(appError)
        } else {
            // Fallback al manejo por defecto
            handleDefaultError(appError)
        }
    }

    /**
     * Lanza una corrutina en contexto IO con manejo de errores
     * @param handleError Función para manejar errores (versión antigua)
     * @param handleAppError Función para manejar AppError (nueva versión)
     * @param block El bloque a ejecutar
     */
    protected fun launchIO(
        handleError: ((Exception) -> Unit)? = null,
        handleAppError: (suspend (AppError) -> Unit)? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launchSafe(Dispatchers.IO, handleError, handleAppError, block)
    }

    /**
     * Método para manejar un error específico desde el ViewModel
     */
    protected suspend fun handleAppError(error: AppError) {
        _errors.emit(error)
        errorHandler?.handleError(error)
    }

    /**
     * Manejo de errores por defecto
     */
    protected open fun handleDefaultError(e: Throwable) {
        // Implementación por defecto - registra el error o maneja según necesidades
        println("Error en ViewModel: ${e.message}")
    }
}
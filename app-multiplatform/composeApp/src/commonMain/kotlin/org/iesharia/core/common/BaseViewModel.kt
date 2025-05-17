package org.iesharia.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
 * ViewModel base con funcionalidad para manejo de estado y errores
 */
abstract class BaseViewModel<T>(
    initialState: T,
    protected val errorHandler: ErrorHandler
) : ViewModel() {

    // Estado UI
    protected val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<T> = _uiState.asStateFlow()

    // Flow para errores de este ViewModel (para observación local)
    private val _errors = MutableSharedFlow<AppError>()
    val errors: SharedFlow<AppError> = _errors.asSharedFlow()

    /**
     * Actualiza el estado actual
     */
    protected fun updateState(update: (T) -> T) {
        _uiState.update(update)
    }

    /**
     * Lanza una corrutina con manejo de errores.
     * Los errores se emiten automáticamente tanto al flow local como al errorHandler global.
     *
     * @param dispatcher El dispatcher a usar
     * @param errorHandler Función opcional para manejar el error antes de propagarlo
     * @param block El código a ejecutar
     */
    protected fun launchSafe(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        errorHandler: (suspend (AppError) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        viewModelScope.launch(dispatcher) {
            try {
                block()
            } catch (e: Throwable) {
                val appError = if (e is AppError) e
                else this@BaseViewModel.errorHandler.convertException(e)

                // Permitir manejo personalizado
                errorHandler?.invoke(appError)

                // Siempre emitir al flow local y global
                _errors.emit(appError)
                this@BaseViewModel.errorHandler.handleError(appError)
            }
        }
    }

    /**
     * Versión de launchSafe que usa Dispatchers.IO
     */
    protected fun launchIO(
        errorHandler: (suspend (AppError) -> Unit)? = null,
        block: suspend () -> Unit
    ) {
        launchSafe(Dispatchers.IO, errorHandler, block)
    }
}
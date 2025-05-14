package org.iesharia.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel base que proporciona funcionalidad común para gestión de estado y manejo de errores
 * @param initialState El estado inicial del ViewModel
 */
abstract class BaseViewModel<T>(initialState: T) : ViewModel() {

    // StateFlow protegido que puede ser actualizado por clases derivadas
    protected val _uiState = MutableStateFlow(initialState)

    // StateFlow inmutable público expuesto a la UI
    val uiState: StateFlow<T> = _uiState.asStateFlow()

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
     * @param handleError Función para manejar errores
     * @param block El bloque a ejecutar
     */
    protected fun launchSafe(
        dispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
        handleError: (Exception) -> Unit = { e -> handleDefaultError(e) },
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch(SupervisorJob() + dispatcher) {
            try {
                block()
            } catch (e: Exception) {
                handleError(e)
            }
        }
    }

    /**
     * Lanza una corrutina en contexto IO con manejo de errores
     * @param handleError Función para manejar errores
     * @param block El bloque a ejecutar
     */
    protected fun launchIO(
        handleError: (Exception) -> Unit = { e -> handleDefaultError(e) },
        block: suspend CoroutineScope.() -> Unit
    ) {
        launchSafe(Dispatchers.IO, handleError, block)
    }

    /**
     * Manejo de errores por defecto
     * Puede ser sobrescrito por ViewModels específicos
     */
    protected open fun handleDefaultError(e: Exception) {
        // Implementación por defecto - registra el error o maneja según necesidades
        println("Error en ViewModel: ${e.message}")
    }
}
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
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.navigation.Routes

/**
 * ViewModel base con funcionalidad para manejo de estado, errores y navegación
 */
abstract class BaseViewModel<T>(
    initialState: T,
    protected val errorHandler: ErrorHandler,
    protected val navigationManager: NavigationManager? = null
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
     * Función para cargar una entidad con manejo de errores estándar
     */
    protected fun <E> loadEntity(
        entityId: String,
        fetchEntity: suspend () -> E?,
        handleError: (AppError) -> T = { error -> updateErrorState(_uiState.value, error) },
        processEntity: suspend (E) -> T
    ) {
        launchSafe(
            errorHandler = { error ->
                updateState {
                    handleError(error)
                }
            }
        ) {
            // Obtener entidad
            val entity = fetchEntity()
                ?: throw AppError.UnknownError(message = "No se encontró la entidad")

            // Procesar entidad y actualizar estado
            val newState = processEntity(entity)
            updateState { newState }
        }
    }

    /**
     * Actualizar estado con error
     */
    protected open fun updateErrorState(currentState: T, error: AppError): T {
        // Implementación por defecto, debe ser sobrescrita en subclases si es necesario
        return currentState
    }

    /**
     * Lanza una corrutina con manejo de errores.
     * Los errores se emiten automáticamente tanto al flow local como al errorHandler global.
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

    // --- Funcionalidad de navegación ---

    /**
     * Tipos de entidades para navegación
     */
    protected enum class EntityType {
        WRESTLER,
        TEAM,
        COMPETITION,
        MATCH
    }

    /**
     * Navegación a detalle de entidad
     */
    protected fun navigateToEntityDetail(entityType: EntityType, entityId: String) {
        navigationManager?.let { manager ->
            launchSafe {
                val route = when (entityType) {
                    EntityType.WRESTLER -> Routes.Wrestler.Detail()
                    EntityType.TEAM -> Routes.Team.Detail()
                    EntityType.COMPETITION -> Routes.Competition.Detail()
                    EntityType.MATCH -> Routes.Match.Detail()
                }
                manager.navigateWithParams(route, entityId)
            }
        }
    }

    /**
     * Navegación a la pantalla principal
     */
    protected fun navigateToHome() {
        navigationManager?.let { manager ->
            launchSafe {
                manager.navigate(Routes.Home.Main)
            }
        }
    }

    /**
     * Navegación atrás
     */
    fun navigateBack() {
        navigationManager?.let { manager ->
            launchSafe {
                manager.navigateBack()
            }
        }
    }
}
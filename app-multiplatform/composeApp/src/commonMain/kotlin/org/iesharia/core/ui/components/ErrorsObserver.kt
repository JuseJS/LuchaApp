package org.iesharia.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import org.iesharia.core.domain.model.AppError

/**
 * Componente que observa múltiples flujos de errores y los muestra en un SnackbarHostState.
 * Permite centralizar la observación de errores desde múltiples fuentes (ViewModels, managers, etc.)
 * en un solo lugar para mostrarlos en la UI.
 *
 * Este componente reemplaza la necesidad de tener varios ErrorObserver para diferentes ViewModels.
 *
 * @param errorFlows Array de SharedFlow<AppError> a observar
 * @param snackbarHostState SnackbarHostState donde se mostrarán los errores
 * @param actionLabel Etiqueta opcional para la acción del Snackbar (por defecto "OK")
 * @param onAction Callback opcional que se invoca cuando el usuario pulsa la acción
 */
@Composable
fun ErrorsObserver(
    vararg errorFlows: SharedFlow<AppError>,
    snackbarHostState: SnackbarHostState,
    actionLabel: String? = "OK",
    onAction: (AppError) -> Unit = {}
) {
    // Para cada flujo de errores, crear un LaunchedEffect para observar
    errorFlows.forEach { errorFlow ->
        LaunchedEffect(errorFlow) {
            errorFlow.collect { error ->
                // Mostrar el error en el Snackbar
                val result = snackbarHostState.showSnackbar(
                    message = error.message,
                    actionLabel = actionLabel
                )
                
                // Si se pulsa la acción, invocar el callback con el error
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    onAction(error)
                }
            }
        }
    }
}

/**
 * Versión simplificada que acepta un solo ViewModel y observa su flujo de errores.
 * Es similar al componente ErrorObserver original pero con parámetros mejorados.
 *
 * @param viewModel ViewModel con un flujo de errores (debe implementar ErrorEmitting)
 * @param snackbarHostState SnackbarHostState donde se mostrarán los errores
 * @param actionLabel Etiqueta opcional para la acción del Snackbar
 * @param onAction Callback opcional que se invoca cuando el usuario pulsa la acción
 */
@Composable
fun <T : ErrorEmitting> ViewModelErrorObserver(
    viewModel: T,
    snackbarHostState: SnackbarHostState,
    actionLabel: String? = "OK",
    onAction: (AppError) -> Unit = {}
) {
    ErrorsObserver(
        errorFlows = arrayOf(viewModel.errors),
        snackbarHostState = snackbarHostState,
        actionLabel = actionLabel,
        onAction = onAction
    )
}

/**
 * Interfaz que deben implementar los ViewModels que emiten errores
 */
interface ErrorEmitting {
    val errors: SharedFlow<AppError>
}
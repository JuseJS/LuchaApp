package org.iesharia.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import org.iesharia.core.domain.model.AppError

/**
 * Interfaz que deben implementar los ViewModels que emiten errores
 */
interface ErrorEmitting {
    val errors: SharedFlow<AppError>
}

/**
 * Componente unificado para manejar errores de múltiples fuentes
 * Reemplaza completamente ErrorObserver y ViewModelErrorHandler
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

                // Sí se pulsa la acción, invocar el callback con el error
                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    onAction(error)
                }
            }
        }
    }
}

/**
 * Versión optimizada para un solo ViewModel
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
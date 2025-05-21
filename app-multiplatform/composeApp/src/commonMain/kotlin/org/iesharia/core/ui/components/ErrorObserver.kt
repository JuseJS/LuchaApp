package org.iesharia.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.SharedFlow
import org.iesharia.core.domain.model.AppError

/**
 * Componente para observar errores de un ViewModel y mostrarlos en un SnackbarHostState
 */
@Composable
fun ErrorObserver(
    errorFlow: SharedFlow<AppError>,
    snackbarHostState: SnackbarHostState,
    actionLabel: String? = "OK"
) {
    LaunchedEffect(errorFlow) {
        errorFlow.collect { error ->
            snackbarHostState.showSnackbar(
                message = error.message,
                actionLabel = actionLabel
            )
        }
    }
}
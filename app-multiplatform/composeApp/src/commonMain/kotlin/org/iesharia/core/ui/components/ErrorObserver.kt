package org.iesharia.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.SharedFlow
import org.iesharia.core.domain.model.AppError

/**
 * Componente para observar errores de un ViewModel y mostrarlos en un SnackbarHostState
 * 
 * @deprecated Use ErrorsObserver or ViewModelErrorObserver instead for improved functionality
 */
@Composable
fun ErrorObserver(
    errorFlow: SharedFlow<AppError>,
    snackbarHostState: SnackbarHostState,
    actionLabel: String? = "OK"
) {
    ErrorsObserver(
        errorFlows = arrayOf(errorFlow),
        snackbarHostState = snackbarHostState,
        actionLabel = actionLabel
    )
}
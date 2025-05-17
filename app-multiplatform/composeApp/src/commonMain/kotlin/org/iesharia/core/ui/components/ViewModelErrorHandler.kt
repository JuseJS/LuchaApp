package org.iesharia.core.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.iesharia.core.common.BaseViewModel
import org.iesharia.core.common.ErrorHandler
import org.koin.compose.koinInject

/**
 * Componente Composable que configura la observación de errores para pantallas con ViewModels
 * Observa tanto errores locales del ViewModel como errores globales del ErrorHandler
 *
 * @param viewModel El ViewModel de la pantalla
 * @param snackbarHostState El SnackbarHostState para mostrar errores
 * @param errorHandler El ErrorHandler global opcional (se inyecta por defecto)
 */
@Composable
fun <T> ViewModelErrorHandler(
    viewModel: BaseViewModel<T>,
    snackbarHostState: SnackbarHostState,
    errorHandler: ErrorHandler = koinInject()
) {
    // Observar errores locales del ViewModel
    ErrorObserver(
        errorFlow = viewModel.errors,
        snackbarHostState = snackbarHostState
    )

    // Observar errores globales del ErrorHandler
    LaunchedEffect(Unit) {
        errorHandler.errors.collect { error ->
            snackbarHostState.showSnackbar(
                message = error.message,
                actionLabel = "OK"
            )
        }
    }
}
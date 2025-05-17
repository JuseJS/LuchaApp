package org.iesharia.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.iesharia.core.domain.model.AppError

/**
 * Componente para mostrar mensajes de error en una barra superior
 */
@Composable
fun ErrorBanner(
    error: AppError?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    autoDismissMillis: Long = 5000 // Auto-descartar después de 5 segundos
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(error) {
        if (error != null) {
            visible = true
            if (autoDismissMillis > 0) {
                delay(autoDismissMillis)
                visible = false
                onDismiss()
            }
        } else {
            visible = false
        }
    }

    AnimatedVisibility(
        visible = visible && error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        ErrorBannerContent(error = error!!, onDismiss = {
            visible = false
            onDismiss()
        })
    }
}

@Composable
private fun ErrorBannerContent(
    error: AppError,
    onDismiss: () -> Unit
) {
    // Determinar el color y el icono según el tipo de error
    val (backgroundColor, textColor, icon) = when (error) {
        is AppError.NetworkError, is AppError.ServerError ->
            Triple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, Icons.Default.Warning)
        is AppError.ValidationError ->
            Triple(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.onSurfaceVariant, Icons.Default.Error)
        is AppError.AuthError, is AppError.PermissionError ->
            Triple(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer, Icons.Default.Error)
        else ->
            Triple(MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer, Icons.Default.Error)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.padding(end = 16.dp)
        )

        Text(
            text = error.getUserMessage(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(start = 40.dp, end = 48.dp)
                .fillMaxWidth()
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = textColor
            )
        }
    }
}

/**
 * Host para snackbar de errores
 */
@Composable
fun ErrorSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = modifier,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(text = data.visuals.message)
                },
                action = {
                    data.visuals.actionLabel?.let { actionLabel ->
                        IconButton(onClick = { data.performAction() }) {
                            Text(
                                text = actionLabel,
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    }
                }
            )
        }
    )
}

/**
 * Componente para observar errores de un ViewModel y mostrarlos en un SnackbarHostState
 */
@Composable
fun ObserveErrorsAsSnackbars(
    viewModel: org.iesharia.core.common.BaseViewModel<*>,
    snackbarHostState: SnackbarHostState,
    actionLabel: String? = "OK"
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel) {
        viewModel.errors.collect { error ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = error.getUserMessage(),
                    actionLabel = actionLabel
                )
            }
        }
    }
}
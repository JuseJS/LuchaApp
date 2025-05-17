package org.iesharia.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Scaffold base para toda la aplicación con manejo uniforme de errores y carga
 */
@Composable
fun WrestlingScaffold(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    actions: @Composable (() -> Unit) = {},
    snackbarHostState: SnackbarHostState,
    isLoading: Boolean = false,
    content: @Composable (paddingValues: androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            WrestlingTopBar(
                title = title,
                onNavigateBack = onNavigateBack,
                actions = { actions() }
            )
        },
        snackbarHost = {
            ErrorSnackbarHost(snackbarHostState)
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Contenido principal
            content(paddingValues)

            // Overlay de carga
            if (isLoading) {
                WrestlingLoadingOverlay()
            }
        }
    }
}
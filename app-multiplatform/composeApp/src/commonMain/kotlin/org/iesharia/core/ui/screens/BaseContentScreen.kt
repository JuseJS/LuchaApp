package org.iesharia.core.ui.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.iesharia.core.navigation.AppScreen
import org.iesharia.core.ui.components.WrestlingScaffold

/**
 * Clase base para pantallas con lógica común como manejo de errores, navegación y estructura Scaffold
 */
abstract class BaseContentScreen : AppScreen() {

    @Composable
    protected abstract fun TopBarActions()

    @Composable
    protected abstract fun ScreenTitle(): String

    @Composable
    protected abstract fun OnNavigateBack(): (() -> Unit)?

    @Composable
    protected abstract fun IsLoading(): Boolean

    @Composable
    protected abstract fun ContentImpl()

    @Composable
    protected open fun SetupViewModel() {
        // Para ser sobrescrito por las subclases
    }

    @Composable
    final override fun ScreenContent() {
        val snackbarHostState = remember { SnackbarHostState() }

        // Configurar ViewModel
        SetupViewModel()

        // Contenido principal con scaffold
        WrestlingScaffold(
            title = ScreenTitle(),
            onNavigateBack = OnNavigateBack(),
            actions = { TopBarActions() },
            snackbarHostState = snackbarHostState,
            isLoading = IsLoading()
        ) {
            ContentImpl()
        }
    }
}
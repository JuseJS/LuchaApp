package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.koin.compose.koinInject

/**
 * Clase base para todas las pantallas de la aplicación
 */
abstract class AppScreen : Screen {
    /**
     * Obtiene el navegador actual o lanza una excepción si no está disponible
     */
    @Composable
    protected fun requireNavigator() = LocalNavigator.currentOrThrow

    /**
     * Navega a una ruta específica.
     */
    @Composable
    protected fun navigateTo(route: NavigationRoute) {
        val navigator = requireNavigator()
        val navigationFactory: NavigationFactory = koinInject()

        val screen = navigationFactory.createScreen(route)
        navigator.push(screen)
    }

    /**
     * Navega hacia atrás.
     */
    @Composable
    protected fun navigateBack() {
        val navigator = requireNavigator()
        navigator.pop()
    }
}
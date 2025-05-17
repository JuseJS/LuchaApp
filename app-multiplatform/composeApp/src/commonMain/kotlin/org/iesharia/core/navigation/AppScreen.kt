package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

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
     * Contenido específico de la pantalla a implementar por las subclases
     */
    @Composable
    protected abstract fun ScreenContent()

    /**
     * Implementación final del método Content de Screen
     * Este método no puede ser sobrescrito por las subclases
     */
    @Composable
    final override fun Content() {
        // Llamar al contenido específico de la pantalla
        ScreenContent()
    }
}
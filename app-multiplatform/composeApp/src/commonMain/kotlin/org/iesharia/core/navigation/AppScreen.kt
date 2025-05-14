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
}
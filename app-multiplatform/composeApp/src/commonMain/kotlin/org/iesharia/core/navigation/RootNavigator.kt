package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.iesharia.features.auth.ui.screens.LoginScreen
import org.koin.compose.koinInject

/**
 * Componente principal de navegación para la aplicación
 * Configura la navegación una sola vez a nivel global
 */
@Composable
fun RootNavigator() {
    val navigationManager = koinInject<NavigationManager>()
    val navigationFactory = koinInject<NavigationFactory>()

    Navigator(
        screen = LoginScreen()
    ) { navigator ->
        // Conectar NavigationManager a Voyager - solo una vez a nivel global
        navigator.HandleNavigationManager(navigationManager, navigationFactory)

        // Aplicar transición
        SlideTransition(navigator)
    }
}
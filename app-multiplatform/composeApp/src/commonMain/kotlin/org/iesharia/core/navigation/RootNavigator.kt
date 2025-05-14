package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.iesharia.features.auth.ui.screens.LoginScreen

/**
 * Componente principal de navegación para la aplicación
 */
@Composable
fun RootNavigator() {
    Navigator(
        screen = LoginScreen()
    ) { navigator ->
        SlideTransition(navigator)
    }
}
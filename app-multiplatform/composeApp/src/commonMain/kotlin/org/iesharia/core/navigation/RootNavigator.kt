package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.iesharia.features.auth.ui.screens.LoginScreen
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.koinInject
import kotlinx.coroutines.flow.collectLatest

/**
 * Componente principal de navegación para la aplicación
 */
@Composable
fun RootNavigator() {
    val navigationManager: NavigationManager = koinInject()
    val navigationFactory: NavigationFactory = koinInject()

    Navigator(
        screen = LoginScreen()
    ) { navigator ->
        // Manejar eventos de navegación
        LaunchedEffect(navigator) {
            navigationManager.navigationEvents.collectLatest { command ->
                when (command) {
                    is NavigationCommand.Navigate -> {
                        val screen = navigationFactory.createScreen(command.route)
                        navigator.push(screen)
                    }
                    is NavigationCommand.NavigateBack -> {
                        navigator.pop()
                    }
                    is NavigationCommand.NavigateWithParams<*> -> {
                        val route = command.route
                        val params = command.params

                        // Crear una instancia con la ruta actualizada
                        val navigationRoute = when (route) {
                            is Routes.Competition.Detail -> Routes.Competition.Detail(params as String)
                            is Routes.Team.Detail -> Routes.Team.Detail(params as String)
                            is Routes.Wrestler.Detail -> Routes.Wrestler.Detail(params as String)
                            else -> route
                        }

                        val screen = navigationFactory.createScreen(navigationRoute)
                        navigator.push(screen)
                    }
                }
            }
        }

        SlideTransition(navigator)
    }
}
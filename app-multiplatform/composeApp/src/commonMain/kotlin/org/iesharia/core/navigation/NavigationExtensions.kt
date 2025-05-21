package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

/**
 * Extension function para manejar el NavigationManager desde Voyager
 */
@Composable
fun Navigator.HandleNavigationManager(
    navigationManager: NavigationManager = koinInject(),
    navigationFactory: NavigationFactory = koinInject()
) {
    LaunchedEffect(this) {
        navigationManager.navigationEvents.collectLatest { command ->
            when (command) {
                is NavigationCommand.Navigate -> {
                    val screen = navigationFactory.createScreen(command.route)
                    push(screen)
                }
                is NavigationCommand.NavigateBack -> {
                    pop()
                }
                is NavigationCommand.NavigateWithParams<*> -> {
                    val route = command.route
                    val params = command.params

                    val navigationRoute = when (route) {
                        is Routes.Competition.Detail -> Routes.Competition.Detail(params as String)
                        is Routes.Team.Detail -> Routes.Team.Detail(params as String)
                        is Routes.Wrestler.Detail -> Routes.Wrestler.Detail(params as String)
                        is Routes.Match.Detail -> Routes.Match.Detail(params as String)
                        is Routes.Match.Act -> Routes.Match.Act(params as String)
                        else -> route
                    }

                    val screen = navigationFactory.createScreen(navigationRoute)
                    push(screen)
                }
            }
        }
    }
}
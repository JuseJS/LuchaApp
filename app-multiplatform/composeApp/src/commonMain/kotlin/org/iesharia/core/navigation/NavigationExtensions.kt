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
        println("🔌 NavigationExtensions: LaunchedEffect iniciado para Navigator")
        navigationManager.navigationEvents.collectLatest { command ->
            println("📍 NavigationExtensions: Comando recibido: $command")
            when (command) {
                is NavigationCommand.Navigate -> {
                    println("🚀 NavigationExtensions: Navegando a ${command.route.baseRoute}, clearBackStack=${command.clearBackStack}")
                    val screen = navigationFactory.createScreen(command.route)
                    println("📱 NavigationExtensions: Screen creado: ${screen::class.simpleName}")
                    
                    if (command.clearBackStack) {
                        println("🧹 NavigationExtensions: Limpiando stack y reemplazando con nueva pantalla")
                        // Log current stack before replacing
                        println("📚 NavigationExtensions: Stack actual antes de replaceAll: ${items.map { it::class.simpleName }}")
                        replaceAll(screen)
                        println("✅ NavigationExtensions: replaceAll ejecutado")
                        // Log stack after replacing
                        println("📚 NavigationExtensions: Stack después de replaceAll: ${items.map { it::class.simpleName }}")
                    } else {
                        println("➕ NavigationExtensions: Push normal a la pantalla")
                        push(screen)
                    }
                }
                is NavigationCommand.NavigateBack -> {
                    println("⬅️ NavigationExtensions: Navegando hacia atrás")
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
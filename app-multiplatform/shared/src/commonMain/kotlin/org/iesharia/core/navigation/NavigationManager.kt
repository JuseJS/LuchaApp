package org.iesharia.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {
    private val _navigationEvents = MutableSharedFlow<NavigationCommand>()
    val navigationEvents: SharedFlow<NavigationCommand> = _navigationEvents.asSharedFlow()

    suspend fun navigate(route: NavigationRoute, clearBackStack: Boolean = false) {
        println("🎯 NavigationManager: Emitiendo navigate a ${route.baseRoute}, clearBackStack=$clearBackStack")
        val command = NavigationCommand.Navigate(route, clearBackStack)
        _navigationEvents.emit(command)
        println("✅ NavigationManager: Comando emitido: $command")
    }

    suspend fun navigateBack() {
        println("🎯 NavigationManager: Emitiendo navigateBack")
        _navigationEvents.emit(NavigationCommand.NavigateBack)
    }

    suspend fun navigateWithParams(routeWithParams: NavigationRouteWithParams<*>, params: Any) {
        @Suppress("UNCHECKED_CAST")
        println("🎯 NavigationManager: Emitiendo navigateWithParams a ${routeWithParams.baseRoute} con params: $params")
        _navigationEvents.emit(NavigationCommand.NavigateWithParams(routeWithParams as NavigationRouteWithParams<Any>, params))
    }
}

sealed class NavigationCommand {
    data class Navigate(val route: NavigationRoute, val clearBackStack: Boolean = false) : NavigationCommand()
    data object NavigateBack : NavigationCommand()
    data class NavigateWithParams<T>(val route: NavigationRouteWithParams<T>, val params: T) : NavigationCommand()
}
package org.iesharia.core.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {
    private val _navigationEvents = MutableSharedFlow<NavigationCommand>()
    val navigationEvents: SharedFlow<NavigationCommand> = _navigationEvents.asSharedFlow()

    suspend fun navigate(route: NavigationRoute) {
        _navigationEvents.emit(NavigationCommand.Navigate(route))
    }

    suspend fun navigateBack() {
        _navigationEvents.emit(NavigationCommand.NavigateBack)
    }

    suspend fun navigateWithParams(routeWithParams: NavigationRouteWithParams<*>, params: Any) {
        @Suppress("UNCHECKED_CAST")
        _navigationEvents.emit(NavigationCommand.NavigateWithParams(routeWithParams as NavigationRouteWithParams<Any>, params))
    }
}

sealed class NavigationCommand {
    data class Navigate(val route: NavigationRoute) : NavigationCommand()
    data object NavigateBack : NavigationCommand()
    data class NavigateWithParams<T>(val route: NavigationRouteWithParams<T>, val params: T) : NavigationCommand()
}
package org.iesharia.core.navigation

import cafe.adriel.voyager.core.screen.Screen

/**
 * Eventos de navegación que pueden ser emitidos por los ViewModels
 */
sealed class NavigationEvent {
    data class NavigateTo(val screen: Screen) : NavigationEvent()
    data object NavigateBack : NavigationEvent()
}
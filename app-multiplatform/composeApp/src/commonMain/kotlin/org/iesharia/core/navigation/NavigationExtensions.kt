package org.iesharia.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

/**
 * Extension function para manejar eventos de navegación desde un ViewModel
 */
@Composable
fun Navigator.HandleNavigation(events: Flow<NavigationEvent>) {
    LaunchedEffect(this) {
        events.collectLatest { event ->
            when (event) {
                is NavigationEvent.NavigateTo -> push(event.screen)
                is NavigationEvent.NavigateBack -> pop()
            }
        }
    }
}
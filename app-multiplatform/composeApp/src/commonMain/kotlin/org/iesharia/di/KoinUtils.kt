package org.iesharia.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import org.koin.compose.LocalKoinScope
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * Función para obtener un ViewModel de Koin en Compose
 */
@Composable
inline fun <reified T : ViewModel> rememberViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): T {
    val koinScope = LocalKoinScope.current

    return remember(qualifier, parameters) {
        try {
            koinScope.get<T>(qualifier, parameters)
        } catch (e: Exception) {
            // Log detallado del error
            println("Error obteniendo ViewModel ${T::class.simpleName}: ${e.message}")

            // Como fallback, intentamos obtener directamente del contexto global
            org.koin.core.context.GlobalContext.get().get<T>(qualifier, parameters)
        }
    }
}
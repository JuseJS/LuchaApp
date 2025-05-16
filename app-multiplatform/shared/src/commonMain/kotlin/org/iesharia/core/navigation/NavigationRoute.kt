package org.iesharia.core.navigation

/**
 * Interfaz base para todas las rutas de navegación.
 */
interface NavigationRoute {
    /**
     * La ruta base sin parámetros
     */
    val baseRoute: String

    /**
     * La ruta completa, incluyendo parámetros si los hay
     */
    val route: String
        get() = baseRoute
}

/**
 * Interfaz para rutas que requieren parámetros.
 */
interface NavigationRouteWithParams<T> : NavigationRoute {
    /**
     * Crea la ruta completa con los parámetros proporcionados.
     */
    fun createRoute(params: T): String
}
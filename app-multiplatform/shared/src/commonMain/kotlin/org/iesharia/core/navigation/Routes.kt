package org.iesharia.core.navigation

/**
 * Objeto que contiene todas las rutas de navegación de la aplicación.
 */
object Routes {
    /**
     * Rutas relacionadas con la autenticación.
     */
    object Auth {
        /**
         * Ruta para la pantalla de inicio de sesión/registro.
         */
        object Login : NavigationRoute {
            override val baseRoute: String = "auth/login"
        }
    }

    /**
     * Rutas relacionadas con la pantalla principal.
     */
    object Home {
        /**
         * Ruta para la pantalla principal.
         */
        object Main : NavigationRoute {
            override val baseRoute: String = "home"
        }
    }

    /**
     * Rutas relacionadas con competiciones.
     */
    object Competition {
        /**
         * Ruta para la pantalla de detalle de competición.
         */
        data class Detail(private val competitionId: String? = null) : NavigationRouteWithParams<String> {
            override val baseRoute: String = "competition/detail"
            override val route: String
                get() = if (competitionId != null) "$baseRoute/$competitionId" else "$baseRoute/{competitionId}"

            override fun createRoute(params: String): String = "$baseRoute/$params"
        }
    }

    /**
     * Rutas relacionadas con equipos.
     */
    object Team {
        /**
         * Ruta para la pantalla de detalle de equipo.
         */
        data class Detail(private val teamId: String? = null) : NavigationRouteWithParams<String> {
            override val baseRoute: String = "team/detail"
            override val route: String
                get() = if (teamId != null) "$baseRoute/$teamId" else "$baseRoute/{teamId}"

            override fun createRoute(params: String): String = "$baseRoute/$params"
        }
    }

    /**
     * Rutas relacionadas con luchadores.
     */
    object Wrestler {
        /**
         * Ruta para la pantalla de detalle de luchador.
         */
        data class Detail(private val wrestlerId: String? = null) : NavigationRouteWithParams<String> {
            override val baseRoute: String = "wrestler/detail"
            override val route: String
                get() = if (wrestlerId != null) "$baseRoute/$wrestlerId" else "$baseRoute/{wrestlerId}"

            override fun createRoute(params: String): String = "$baseRoute/$params"
        }
    }

    object Match {
        /**
         * Ruta para la pantalla de detalle de enfrentamiento
         */
        data class Detail(private val matchId: String? = null) : NavigationRouteWithParams<String> {
            override val baseRoute: String = "match/detail"
            override val route: String
                get() = if (matchId != null) "$baseRoute/$matchId" else "$baseRoute/{matchId}"

            override fun createRoute(params: String): String = "$baseRoute/$params"
        }

        /**
         * Ruta para la pantalla de acta de enfrentamiento
         */
        data class Act(private val matchId: String? = null) : NavigationRouteWithParams<String> {
            override val baseRoute: String = "match/act"
            override val route: String
                get() = if (matchId != null) "$baseRoute/$matchId" else "$baseRoute/{matchId}"

            override fun createRoute(params: String): String = "$baseRoute/$params"
        }
    }

    /**
     * Rutas relacionadas con configuración.
     */
    object Config {
        /**
         * Ruta para la pantalla de configuración.
         */
        object Settings : NavigationRoute {
            override val baseRoute: String = "config/settings"
        }
    }
}
package org.iesharia.core.navigation

import cafe.adriel.voyager.core.screen.Screen
import org.iesharia.features.auth.ui.screens.LoginScreen
import org.iesharia.features.competitions.ui.screens.CompetitionDetailScreen
import org.iesharia.features.home.ui.screens.HomeScreen
import org.iesharia.features.matches.ui.screens.MatchActScreen
import org.iesharia.features.matches.ui.screens.MatchDetailScreen
import org.iesharia.features.teams.ui.screens.TeamDetailScreen
import org.iesharia.features.wrestlers.ui.screens.WrestlerDetailScreen

/**
 * Factory que convierte rutas de navegación en pantallas de Voyager.
 */
class NavigationFactory {
    /**
     * Crea una pantalla de Voyager a partir de una ruta.
     */
    fun createScreen(navigationRoute: NavigationRoute): Screen {
        return when (navigationRoute) {
            is Routes.Auth.Login -> LoginScreen()
            is Routes.Home.Main -> HomeScreen()

            // Competiciones
            is Routes.Competition.Detail -> {
                val competitionId = getLastPathSegment(navigationRoute.route)
                CompetitionDetailScreen(competitionId)
            }

            // Equipos
            is Routes.Team.Detail -> {
                val teamId = getLastPathSegment(navigationRoute.route)
                TeamDetailScreen(teamId)
            }

            // Luchadores
            is Routes.Wrestler.Detail -> {
                val wrestlerId = getLastPathSegment(navigationRoute.route)
                WrestlerDetailScreen(wrestlerId)
            }

            // Enfrentamientos
            is Routes.Match.Detail -> {
                val matchId = getLastPathSegment(navigationRoute.route)
                println("NavigationFactory: Creando MatchDetailScreen con ID: $matchId") // Debug
                MatchDetailScreen(matchId)
            }

            // Acta de enfrentamiento
            is Routes.Match.Act -> {
                val matchId = getLastPathSegment(navigationRoute.route)
                println("NavigationFactory: Creando MatchActScreen con ID: $matchId") // Debug
                MatchActScreen(matchId)
            }

            // Ruta desconocida
            else -> LoginScreen()
        }
    }

    /**
     * Obtiene el último segmento de una ruta, que normalmente es el parámetro.
     */
    private fun getLastPathSegment(route: String): String {
        return route.split("/").last()
    }
}
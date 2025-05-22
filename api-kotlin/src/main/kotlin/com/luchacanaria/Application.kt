package com.luchacanaria

import com.luchacanaria.core.database.DatabaseConfig
import com.luchacanaria.core.data.SampleDataSeeder
import com.luchacanaria.core.error.configureErrorHandling
import com.luchacanaria.core.security.configureSecurity
import com.luchacanaria.features.auth.data.repository.MongoUserRepository
import com.luchacanaria.features.auth.domain.usecase.LoginUseCase
import com.luchacanaria.features.auth.domain.usecase.RegisterUseCase
import com.luchacanaria.features.auth.presentation.authRoutes
import com.luchacanaria.features.wrestlers.data.repository.MongoWrestlerRepository
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlersUseCase
import com.luchacanaria.features.wrestlers.domain.usecase.GetWrestlerUseCase
import com.luchacanaria.features.wrestlers.presentation.wrestlerRoutes
import com.luchacanaria.features.teams.data.repository.MongoTeamRepository
import com.luchacanaria.features.teams.domain.usecase.GetTeamsUseCase
import com.luchacanaria.features.teams.domain.usecase.GetTeamUseCase
import com.luchacanaria.features.teams.presentation.teamRoutes
import com.luchacanaria.features.common.presentation.healthRoutes
import com.luchacanaria.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Configurar plugins
    configureSerialization()
    configureCORS()
    configureLogging()
    configureCompression()

    // Configurar seguridad y manejo de errores
    configureSecurity()
    configureErrorHandling()

    // Inicializar base de datos
    DatabaseConfig.init()

    // Configurar rutas
    routing {
        // Health check routes
        healthRoutes()
        
        route("/api/v1") {
            // Repositorios
            val userRepository = MongoUserRepository()
            val wrestlerRepository = MongoWrestlerRepository()
            val teamRepository = MongoTeamRepository()

            // Casos de uso Auth
            val loginUseCase = LoginUseCase(userRepository)
            val registerUseCase = RegisterUseCase(userRepository)

            // Casos de uso Wrestlers
            val getWrestlersUseCase = GetWrestlersUseCase(wrestlerRepository)
            val getWrestlersByTeamIdUseCase = GetWrestlersByTeamIdUseCase(wrestlerRepository)
            val getWrestlerUseCase = GetWrestlerUseCase(wrestlerRepository)

            // Casos de uso Teams
            val getTeamsUseCase = GetTeamsUseCase(teamRepository)
            val getTeamUseCase = GetTeamUseCase(teamRepository)

            // Rutas
            authRoutes(loginUseCase, registerUseCase)
            wrestlerRoutes(getWrestlersUseCase, getWrestlersByTeamIdUseCase, getWrestlerUseCase)
            teamRoutes(getTeamsUseCase, getTeamUseCase)

            // Seed sample data in development
            val seeder = SampleDataSeeder(userRepository, teamRepository, wrestlerRepository)
            launch {
                seeder.seedData()
            }
        }
    }
}

package org.iesharia.di

import org.iesharia.core.common.DefaultErrorHandler
import org.iesharia.core.common.ErrorHandler
import org.iesharia.features.auth.domain.usecase.LoginUseCase
import org.iesharia.features.auth.domain.usecase.LogoutUseCase
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.common.domain.usecase.ToggleFavoriteUseCase
import org.iesharia.features.common.domain.usecase.ObserveFavoritesUseCase
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.matches.domain.usecase.GetMatchActUseCase
import org.iesharia.features.matches.domain.usecase.GetMatchDetailsUseCase
import org.iesharia.features.matches.domain.usecase.SaveMatchActUseCase
import org.iesharia.features.teams.domain.usecase.GetTeamMatchesUseCase
import org.iesharia.features.teams.domain.usecase.GetAllTeamsUseCase
import org.iesharia.features.teams.domain.usecase.GetTeamByIdUseCase
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlersByTeamIdUseCase
import org.iesharia.features.auth.domain.security.SessionManager
import org.iesharia.features.auth.domain.security.RolePermissionManager

/**
 * Módulo para servicios comunes
 */
private val commonModule = module {
    // Registrar el ErrorHandler
    single<ErrorHandler> { DefaultErrorHandler() }

    // Registrar gestores de sesión y permisos
    single { SessionManager(get()) }
    single { RolePermissionManager(get()) }
}

/**
 * Módulo para los repositorios Mock (solo para desarrollo/pruebas)
 */
private val mockRepositoryModule = module {
    // Todos los repositorios ahora están implementados en el networkModule
    // Este módulo queda vacío pero lo mantenemos por si necesitamos mocks en el futuro
}

/**
 * Módulo para los casos de uso
 */
private val useCaseModule = module {
    factory { GetCompetitionsUseCase(get()) }
    factory { GetFavoritesUseCase(get(), get(), get(), get()) }
    factory { ToggleFavoriteUseCase(get()) }
    factory { ObserveFavoritesUseCase(get()) }
    factory { GetTeamMatchesUseCase(get()) }
    factory { GetAllTeamsUseCase(get()) }
    factory { GetTeamByIdUseCase(get()) }
    factory { GetWrestlerResultsUseCase(get()) }
    factory { LoginUseCase(get()) }
    factory { LogoutUseCase(get(), get()) }
    factory { GetWrestlersByTeamIdUseCase(get()) }
    factory { GetMatchDetailsUseCase(get()) }
    factory { GetMatchActUseCase(get()) }
    factory { SaveMatchActUseCase(get()) }
}

/**
 * Módulo para la navegación
 */
private val navigationModule = module {
    single { NavigationManager() }
}

/**
 * Lista de todos los módulos del paquete shared
 */
internal val sharedModules = listOf(
    commonModule,
    networkModule,      // Módulo de red para repositorios HTTP
    mockRepositoryModule, // Módulo Mock para datos que aún no tienen API
    useCaseModule,
    navigationModule
)

/**
 * Función para inicializar Koin con módulos adicionales
 */
fun initKoin(
    additionalModules: List<Module> = emptyList(),
    appDeclaration: KoinApplication.() -> Unit = {}
): KoinApplication {
    return try {
        // Intenta cerrar cualquier instancia previa (por si acaso)
        try {
            org.koin.core.context.stopKoin()
        } catch (e: Exception) {
            // Ignorar si no hay instancia previa
        }

        // Iniciar Koin con los módulos
        startKoin {
            appDeclaration()
            modules(sharedModules + additionalModules)
        }
    } catch (e: Exception) {
        println("Error detallado al inicializar Koin: ${e::class.simpleName}: ${e.message}")
        e.printStackTrace()
        // Retornar una instancia simple como fallback
        KoinApplication.init()
    }
}
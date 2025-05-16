package org.iesharia.di

import org.iesharia.core.data.mock.MockDataGenerator
import org.iesharia.features.competitions.data.repository.MockCompetitionRepository
import org.iesharia.features.auth.data.repository.MockUserRepository
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.auth.domain.repository.UserRepository
import org.iesharia.features.auth.domain.usecase.LoginUseCase
import org.iesharia.features.common.domain.usecase.GetFavoritesUseCase
import org.iesharia.features.competitions.domain.usecase.GetCompetitionsUseCase
import org.iesharia.features.teams.domain.usecase.GetTeamMatchesUseCase
import org.iesharia.features.wrestlers.domain.usecase.GetWrestlerResultsUseCase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.iesharia.core.navigation.NavigationManager

/**
 * Módulo para los repositorios
 */
private val repositoryModule = module {
    single { MockDataGenerator() }
    single<CompetitionRepository> { MockCompetitionRepository(get()) }
    single<UserRepository> { MockUserRepository(get()) }
}

/**
 * Módulo para los casos de uso
 */
private val useCaseModule = module {
    factory { GetCompetitionsUseCase(get()) }
    factory { GetFavoritesUseCase(get()) }
    factory { GetTeamMatchesUseCase(get()) }
    factory { GetWrestlerResultsUseCase(get()) }
    factory { LoginUseCase(get()) }
}

private val navigationModule = module {
    single { NavigationManager() }
}

/**
 * Lista de todos los módulos del paquete shared
 */
internal val sharedModules = listOf(
    repositoryModule,
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
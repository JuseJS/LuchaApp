package org.iesharia.config

import io.ktor.server.application.*
import org.iesharia.data.repositories.*
import org.iesharia.domain.services.*
import org.iesharia.utils.JwtManager
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(appModule)
    }
}

val appModule = module {
    // Database
    single { DatabaseConfig.createMongoClient() }
    single { DatabaseConfig.getDatabase(get()) }
    
    // Utilities
    singleOf(::JwtManager)
    
    // Repositories
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::WrestlerRepositoryImpl) { bind<WrestlerRepository>() }
    singleOf(::TeamRepositoryImpl) { bind<TeamRepository>() }
    singleOf(::CompetitionRepositoryImpl) { bind<CompetitionRepository>() }
    singleOf(::MatchRepositoryImpl) { bind<MatchRepository>() }
    singleOf(::MatchActRepositoryImpl) { bind<MatchActRepository>() }
    singleOf(::RefereeRepositoryImpl) { bind<RefereeRepository>() }
    singleOf(::FavoriteRepositoryImpl) { bind<FavoriteRepository>() }
    
    // Services
    singleOf(::AuthService)
    singleOf(::UserService)
    singleOf(::WrestlerService)
    singleOf(::TeamService)
    singleOf(::CompetitionService)
    singleOf(::MatchService)
    singleOf(::MatchActService)
    singleOf(::FavoriteService)
}
package org.iesharia.di

import org.iesharia.core.auth.AuthenticationManager
import org.iesharia.core.network.HttpClientProvider
import org.iesharia.core.network.TokenStorage
import org.iesharia.core.network.service.AuthService
import org.iesharia.core.network.service.TeamService
import org.iesharia.core.network.service.WrestlerService
import org.iesharia.core.network.service.CompetitionService
import org.iesharia.core.network.service.MatchService
import org.iesharia.core.network.service.HealthService
import org.iesharia.core.network.service.MatchActService
import org.iesharia.core.network.service.FavoriteService
import org.iesharia.features.auth.data.repository.HttpUserRepository
import org.iesharia.features.auth.domain.repository.UserRepository
import org.iesharia.features.wrestlers.data.repository.HttpWrestlerRepository
import org.iesharia.features.wrestlers.domain.repository.WrestlerRepository
import org.iesharia.features.teams.data.repository.HttpTeamRepository
import org.iesharia.features.teams.domain.repository.TeamRepository
import org.iesharia.features.competitions.data.repository.HttpCompetitionRepository
import org.iesharia.features.competitions.domain.repository.CompetitionRepository
import org.iesharia.features.matches.data.repository.HttpMatchRepository
import org.iesharia.features.matches.domain.repository.MatchRepository
import org.iesharia.features.matches.data.repository.HttpMatchActRepository
import org.iesharia.features.matches.domain.repository.MatchActRepository
import org.iesharia.features.common.data.repository.HttpFavoriteRepository
import org.iesharia.features.common.domain.repository.FavoriteRepository
import org.koin.dsl.module

/**
 * Módulo de red para la aplicación
 * Configura los clientes HTTP, servicios y repositorios que interactúan con la API
 */
val networkModule = module {
    // HTTP Client
    single { HttpClientProvider.createHttpClient(get()) }
    
    // Token Storage
    single { TokenStorage() }
    
    // Services
    single { AuthService(get(), get()) }
    single { TeamService(get()) }
    single { WrestlerService(get()) }
    single { CompetitionService(get()) }
    single { MatchService(get()) }
    single { HealthService(get()) }
    single { MatchActService(get()) }
    single { FavoriteService(get()) }
    
    // Authentication Manager
    single { AuthenticationManager(get(), get()) }
    
    // HTTP Repositories
    single<UserRepository> { HttpUserRepository(get()) }
    single<WrestlerRepository> { HttpWrestlerRepository(get()) }
    single<TeamRepository> { HttpTeamRepository(get()) }
    single<CompetitionRepository> { HttpCompetitionRepository(get()) }
    single<MatchRepository> { HttpMatchRepository(get(), get()) }
    single<MatchActRepository> { HttpMatchActRepository(get()) }
    single<FavoriteRepository> { HttpFavoriteRepository(get()) }
}
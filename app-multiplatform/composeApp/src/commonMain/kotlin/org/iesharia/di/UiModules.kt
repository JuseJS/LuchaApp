package org.iesharia.di

import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.auth.ui.viewmodel.LoginViewModel
import org.iesharia.core.navigation.NavigationFactory
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailViewModel
import org.iesharia.features.matches.ui.viewmodel.MatchActViewModel
import org.iesharia.features.matches.ui.viewmodel.MatchDetailViewModel
import org.iesharia.features.teams.ui.viewmodel.TeamDetailViewModel
import org.iesharia.features.wrestlers.ui.viewmodel.WrestlerDetailViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// Módulo específico para ViewModels (en composeApp)
val viewModelModule = module {
    factory { HomeViewModel(get(), get(), get(), get(), get<NavigationManager>(), get(), get(), get()) }
    factory { LoginViewModel(get<NavigationManager>(), get(), get(), get()) }
    factory { parameters ->
        CompetitionDetailViewModel(
            competitionId = parameters.get(),
            competitionRepository = get(),
            getFavoritesUseCase = get(),
            navigationManager = get(),
            errorHandler = get()
        )
    }
    factory { parameters ->
        TeamDetailViewModel(
            teamId = parameters.get(),
            competitionRepository = get(),
            getWrestlersByTeamIdUseCase = get(),
            getFavoritesUseCase = get(),
            navigationManager = get(),
            errorHandler = get()
        )
    }
    factory { parameters ->
        WrestlerDetailViewModel(
            wrestlerId = parameters.get(),
            wrestlerRepository = get(),
            competitionRepository = get(),
            getFavoritesUseCase = get(),
            getWrestlerResultsUseCase = get(),
            navigationManager = get(),
            errorHandler = get()
        )
    }
    factory { parameters ->
        MatchDetailViewModel(
            matchId = parameters.get(),
            getMatchDetailsUseCase = get(),
            matchRepository = get(),
            navigationManager = get(),
            errorHandler = get()
        )
    }
    factory { parameters ->
        MatchActViewModel(
            matchId = parameters.get(),
            getMatchDetailsUseCase = get(),
            matchRepository = get(),
            navigationManager = get(),
            errorHandler = get()
        )
    }
}

// Módulo para navegación UI
val navigationUiModule = module {
    factory { NavigationFactory() }
}

// Lista de todos los módulos UI
val uiModules: List<Module> = listOf(viewModelModule, navigationUiModule)
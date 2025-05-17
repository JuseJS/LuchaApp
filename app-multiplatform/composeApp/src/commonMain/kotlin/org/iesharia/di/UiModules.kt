package org.iesharia.di

import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.auth.ui.viewmodel.LoginViewModel
import org.iesharia.core.navigation.NavigationFactory
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.features.competitions.ui.viewmodel.CompetitionDetailViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// Módulo para ViewModels
val viewModelModule = module {
    factory { HomeViewModel(get(), get(), get(), get(), get<NavigationManager>(), get()) }
    factory { LoginViewModel(get<NavigationManager>(), get()) }
    factory { parameters ->
        CompetitionDetailViewModel(
            competitionId = parameters.get(),
            competitionRepository = get(),
            getFavoritesUseCase = get(),
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
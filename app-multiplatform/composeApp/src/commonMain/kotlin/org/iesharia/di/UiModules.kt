package org.iesharia.di

import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.iesharia.features.auth.ui.viewmodel.LoginViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// Módulo para ViewModels
val viewModelModule = module {
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { LoginViewModel() }
}

// Lista de todos los módulos UI
val uiModules: List<Module> = listOf(viewModelModule)
package org.iesharia.di

import org.iesharia.ui.screens.home.HomeViewModel
import org.iesharia.ui.screens.login.LoginViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

// Módulo para ViewModels
val viewModelModule = module {
    factory { HomeViewModel(get(), get(), get(), get()) }
    factory { LoginViewModel() }
}

// Lista de todos los módulos UI
val uiModules: List<Module> = listOf(viewModelModule)
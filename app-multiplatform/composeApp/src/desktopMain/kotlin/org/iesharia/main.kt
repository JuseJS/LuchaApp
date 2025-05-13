package org.iesharia

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.iesharia.app.App
import org.iesharia.di.initKoin
import org.iesharia.di.uiModules

fun main() {
    try {
        println("Iniciando la aplicación...")
        initKoin(uiModules)
        println("Koin inicializado correctamente")
    } catch (e: Exception) {
        println("ERROR CRÍTICO al inicializar Koin: ${e.message}")
        e.printStackTrace()
    }

    application {
        Window(onCloseRequest = ::exitApplication, title = "Lucha Canaria") {
            App()
        }
    }
}
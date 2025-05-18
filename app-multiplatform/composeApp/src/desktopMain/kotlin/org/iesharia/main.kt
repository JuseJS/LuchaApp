package org.iesharia

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
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
        // Estado para alternar entre ventana normal y maximizada
        var placement by remember { mutableStateOf(WindowPlacement.Maximized) }
        val windowState = rememberWindowState(placement = placement)

        Window(
            onCloseRequest = ::exitApplication,
            title = "Lucha Canaria",
            state = windowState,
            onKeyEvent = { keyEvent ->
                // Alternar entre normal y maximizado con la tecla F11
                if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.F11) {
                    placement = if (placement == WindowPlacement.Maximized)
                        WindowPlacement.Floating
                    else
                        WindowPlacement.Maximized
                    true
                } else {
                    false
                }
            }
        ) {
            App()
        }
    }
}
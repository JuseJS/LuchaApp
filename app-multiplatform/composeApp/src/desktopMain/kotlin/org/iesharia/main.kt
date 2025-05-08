package org.iesharia

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.iesharia.app.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "LuchaApp",
    ) {
        App()
    }
}
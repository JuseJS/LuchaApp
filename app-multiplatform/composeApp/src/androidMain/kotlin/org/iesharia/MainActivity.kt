package org.iesharia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.iesharia.app.App
import org.iesharia.di.initKoin
import org.iesharia.di.uiModules

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Asegurarse de que Koin está inicializado (debería hacerse en la clase Application)
        try {
            initKoin(uiModules)
        } catch (e: Exception) {
            // Ya fue inicializado en MainApplication, continuar
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
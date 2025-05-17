package org.iesharia.app

import androidx.compose.runtime.Composable
import org.iesharia.core.navigation.RootNavigator
import org.iesharia.core.ui.theme.WrestlingAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    org.koin.compose.KoinContext {
        WrestlingAppTheme {
            RootNavigator()
        }
    }
}
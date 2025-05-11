package org.iesharia.app

import androidx.compose.runtime.Composable
import org.iesharia.navigation.RootNavigator
import org.iesharia.ui.theme.LuchaAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LuchaAppTheme {
        RootNavigator()
    }
}
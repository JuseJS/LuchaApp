package org.iesharia.app

import androidx.compose.runtime.Composable
import org.iesharia.ui.screens.login.LoginScreen
import org.iesharia.ui.screens.login.LoginViewModel
import org.iesharia.ui.theme.LuchaAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    LuchaAppTheme {
        LoginScreen(viewModel = LoginViewModel())
    }
}
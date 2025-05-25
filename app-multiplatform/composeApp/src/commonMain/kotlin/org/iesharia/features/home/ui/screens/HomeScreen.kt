package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.DarkBackground
import org.iesharia.core.ui.theme.White90
import org.iesharia.di.rememberViewModel
import org.iesharia.features.auth.domain.security.SessionManager
import org.iesharia.features.home.ui.viewmodel.HomeViewModel
import org.koin.compose.koinInject

/**
 * Pantalla principal de la aplicación, optimizada para pantallas OLED
 */
class HomeScreen : BaseContentScreen() {

    private lateinit var viewModel: HomeViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<HomeViewModel>()
    }

    @Composable
    override fun ScreenTitle(): String {
        return AppStrings.Common.appName
    }

    @Composable
    override fun OnNavigateBack(): (() -> Unit)? {
        return null
    }

    @Composable
    override fun TopBarActions() {
        val sessionManager = koinInject<SessionManager>()
        val isLoggedIn = sessionManager.isLoggedIn.collectAsState()

        if (isLoggedIn.value) {
            // Acción de configuración para usuarios logueados
            IconButton(
                onClick = { viewModel.navigateToConfig() }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = White90
                )
            }
        } else {
            // Acción de login para invitados
            WrestlingButton(
                text = "Iniciar Sesión",
                onClick = { viewModel.navigateToLogin() },
                type = WrestlingButtonType.TEXT
            )
        }
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            if (uiState.errorMessage != null) {
                HomeErrorContent(
                    errorMessage = uiState.errorMessage ?: "Error desconocido",
                    onRetry = { viewModel.reloadData() }
                )
            } else {
                HomeContent(
                    viewModel = viewModel
                )
            }
        }
    }
}
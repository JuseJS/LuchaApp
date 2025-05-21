package org.iesharia.features.home.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.components.common.EmptyStateMessage
import org.iesharia.core.ui.components.common.SearchBar
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.*
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
            // Acción de logout para usuarios logueados
            WrestlingButton(
                text = "Cerrar Sesión",
                onClick = { viewModel.logout() },
                type = WrestlingButtonType.TEXT
            )
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
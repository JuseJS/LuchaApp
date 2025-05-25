package org.iesharia.features.config.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.WrestlingButton
import org.iesharia.core.ui.components.WrestlingButtonType
import org.iesharia.core.ui.screens.BaseContentScreen
import org.iesharia.core.ui.theme.*
import org.iesharia.di.rememberViewModel
import org.iesharia.features.config.ui.viewmodel.ConfigViewModel

/**
 * Pantalla de configuración de la aplicación
 */
class ConfigScreen : BaseContentScreen() {

    private lateinit var viewModel: ConfigViewModel

    @Composable
    override fun SetupViewModel() {
        viewModel = rememberViewModel<ConfigViewModel>()
    }

    @Composable
    override fun ScreenTitle(): String {
        return "Configuración"
    }

    @Composable
    override fun OnNavigateBack(): (() -> Unit)? {
        return { viewModel.navigateBack() }
    }
    
    @Composable
    override fun TopBarActions() {
        // No actions needed for config screen
    }

    @Composable
    override fun IsLoading(): Boolean {
        val uiState by viewModel.uiState.collectAsState()
        return uiState.isLoading
    }

    @Composable
    override fun ContentImpl() {
        val uiState by viewModel.uiState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
                .padding(WrestlingTheme.dimensions.spacing_16),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Información del usuario
            Surface(
                color = DarkSurface2,
                shape = WrestlingTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(WrestlingTheme.dimensions.spacing_24),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(80.dp)
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                    uiState.userName?.let { name ->
                        Text(
                            text = name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = White90,
                            textAlign = TextAlign.Center
                        )
                    }

                    uiState.userEmail?.let { email ->
                        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_8))
                        Text(
                            text = email,
                            style = MaterialTheme.typography.bodyLarge,
                            color = White80,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_32))

            // Sección de opciones (para futuras funcionalidades)
            Surface(
                color = DarkSurface2,
                shape = WrestlingTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(WrestlingTheme.dimensions.spacing_16)
                ) {
                    Text(
                        text = "Opciones",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = White90
                    )
                    
                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
                    
                    Text(
                        text = "Próximamente más opciones de configuración",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White80
                    )
                }
            }

            // Spacer para empujar el botón hacia abajo
            Spacer(modifier = Modifier.weight(1f))

            // Botón de cerrar sesión en la parte inferior
            WrestlingButton(
                text = "Cerrar Sesión",
                onClick = { viewModel.logout() },
                type = WrestlingButtonType.PRIMARY,
                modifier = Modifier.fillMaxWidth()
            )

            // Espacio inferior
            Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))
        }
    }
}
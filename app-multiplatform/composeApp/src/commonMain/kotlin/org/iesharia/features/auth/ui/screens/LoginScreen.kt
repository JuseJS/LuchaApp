package org.iesharia.features.auth.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import luchaapp.composeapp.generated.resources.Res
import luchaapp.composeapp.generated.resources.app_logo
import org.iesharia.core.navigation.AppScreen
import org.iesharia.core.navigation.HandleNavigationManager
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.*
import org.iesharia.core.ui.theme.LuchaTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.auth.ui.components.LoginForm
import org.iesharia.features.auth.ui.components.RegisterForm
import org.iesharia.features.auth.ui.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class LoginScreen : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel = rememberViewModel<LoginViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = requireNavigator()
        val navigationManager = koinInject<NavigationManager>()

        // Manejar navegación
        navigator.HandleNavigationManager(navigationManager)

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Contenido principal
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(LuchaTheme.dimensions.spacing_24),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Sección superior con logo y título
                    Spacer(modifier = Modifier.weight(0.1f))

                    // Logo (ahora usando componente global)
                    LuchaAppLogo(
                        painter = painterResource(Res.drawable.app_logo),
                        contentDescription = AppStrings.Common.appLogoDesc
                    )

                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_32))

                    // Encabezado (ahora usando componente global)
                    LuchaAppHeader(
                        title = AppStrings.Common.appName,
                        subtitle = AppStrings.Common.appSubtitle
                    )

                    Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_40))

                    // Contenedor del formulario
                    Column(
                        modifier = Modifier
                            .widthIn(max = LuchaTheme.dimensions.login_form_max_width)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animación entre formularios
                        AnimatedContent(
                            targetState = uiState.isLoginMode,
                            transitionSpec = {
                                if (targetState) {
                                    (slideInHorizontally(
                                        animationSpec = tween(durationMillis = 300),
                                        initialOffsetX = { it }
                                    ) + fadeIn(animationSpec = tween(300)))
                                        .togetherWith(
                                            slideOutHorizontally(
                                                animationSpec = tween(durationMillis = 300),
                                                targetOffsetX = { -it }
                                            ) + fadeOut(animationSpec = tween(300))
                                        )
                                } else {
                                    (slideInHorizontally(
                                        animationSpec = tween(durationMillis = 300),
                                        initialOffsetX = { -it }
                                    ) + fadeIn(animationSpec = tween(300)))
                                        .togetherWith(
                                            slideOutHorizontally(
                                                animationSpec = tween(durationMillis = 300),
                                                targetOffsetX = { it }
                                            ) + fadeOut(animationSpec = tween(300))
                                        )
                                }
                            },
                            label = "Auth Form Animation"
                        ) { isLoginMode ->
                            if (isLoginMode) {
                                LoginForm(
                                    email = uiState.loginEmail,
                                    password = uiState.loginPassword,
                                    emailError = uiState.loginEmailError,
                                    passwordError = uiState.loginPasswordError,
                                    onEmailChange = viewModel::updateLoginEmail,
                                    onPasswordChange = viewModel::updateLoginPassword,
                                    onSubmit = viewModel::submitLogin,
                                    isLoading = uiState.isLoading
                                )
                            } else {
                                RegisterForm(
                                    name = uiState.registerName,
                                    surname = uiState.registerSurname,
                                    email = uiState.registerEmail,
                                    password = uiState.registerPassword,
                                    confirmPassword = uiState.registerConfirmPassword,
                                    nameError = uiState.registerNameError,
                                    surnameError = uiState.registerSurnameError,
                                    emailError = uiState.registerEmailError,
                                    passwordError = uiState.registerPasswordError,
                                    confirmPasswordError = uiState.registerConfirmPasswordError,
                                    onNameChange = viewModel::updateRegisterName,
                                    onSurnameChange = viewModel::updateRegisterSurname,
                                    onEmailChange = viewModel::updateRegisterEmail,
                                    onPasswordChange = viewModel::updateRegisterPassword,
                                    onConfirmPasswordChange = viewModel::updateRegisterConfirmPassword,
                                    onSubmit = viewModel::submitRegister,
                                    isLoading = uiState.isLoading
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

                        // Botón para alternar entre login y registro
                        LuchaButton(
                            text = if (uiState.isLoginMode)
                                AppStrings.Auth.noAccount
                            else
                                AppStrings.Auth.alreadyHaveAccount,
                            onClick = viewModel::toggleAuthMode,
                            type = LuchaButtonType.TEXT,
                            customColor = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.weight(0.2f))
                }

                // Overlay de carga
                AnimatedVisibility(
                    visible = uiState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LuchaLoadingOverlay()
                }
            }
        }
    }
}
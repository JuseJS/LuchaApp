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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import luchaapp.composeapp.generated.resources.Res
import luchaapp.composeapp.generated.resources.app_logo
import org.iesharia.core.navigation.AppScreen
import org.iesharia.core.navigation.HandleNavigationManager
import org.iesharia.core.navigation.NavigationManager
import org.iesharia.core.resources.AppStrings
import org.iesharia.core.ui.components.*
import org.iesharia.core.ui.form.FormComponent
import org.iesharia.core.ui.theme.WrestlingTheme
import org.iesharia.di.rememberViewModel
import org.iesharia.features.auth.ui.components.AuthForms
import org.iesharia.features.auth.ui.viewmodel.LoginViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject

class LoginScreen : AppScreen() {
    @Composable
    override fun ScreenContent() {
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
                        .padding(WrestlingTheme.dimensions.spacing_24),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Sección superior con logo y título
                    Spacer(modifier = Modifier.weight(0.1f))

                    // Logo (ahora usando componente global)
                    WrestlingAppLogo(
                        painter = painterResource(Res.drawable.app_logo),
                        contentDescription = AppStrings.Common.appLogoDesc
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_32))

                    // Encabezado (ahora usando componente global)
                    WrestlingAppHeader(
                        title = AppStrings.Common.appName,
                        subtitle = AppStrings.Common.appSubtitle
                    )

                    Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_40))

                    // Contenedor del formulario
                    Column(
                        modifier = Modifier
                            .widthIn(max = WrestlingTheme.dimensions.login_form_max_width)
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
                                // LOGIN FORM - Usando el componente genérico
                                val initialValues = remember(uiState.loginEmail, uiState.loginPassword) {
                                    mapOf(
                                        AuthForms.KEY_EMAIL to uiState.loginEmail,
                                        AuthForms.KEY_PASSWORD to uiState.loginPassword
                                    )
                                }

                                val errors = remember(uiState.loginEmailError, uiState.loginPasswordError) {
                                    mapOf(
                                        AuthForms.KEY_EMAIL to uiState.loginEmailError,
                                        AuthForms.KEY_PASSWORD to uiState.loginPasswordError
                                    ).filterValues { it.isNotEmpty() }
                                }

                                FormComponent(
                                    formDefinition = AuthForms.loginForm,
                                    initialValues = initialValues,
                                    externalErrors = errors,
                                    isSubmitting = uiState.isLoading,
                                    onValueChange = { key, value ->
                                        when (key) {
                                            AuthForms.KEY_EMAIL -> viewModel.updateLoginEmail(value)
                                            AuthForms.KEY_PASSWORD -> viewModel.updateLoginPassword(value)
                                        }
                                    },
                                    onSubmit = { viewModel.submitLogin() }
                                )
                            } else {
                                // REGISTER FORM - Usando el componente genérico
                                val initialValues = remember(
                                    uiState.registerName, uiState.registerSurname,
                                    uiState.registerEmail, uiState.registerPassword,
                                    uiState.registerConfirmPassword
                                ) {
                                    mapOf(
                                        AuthForms.KEY_NAME to uiState.registerName,
                                        AuthForms.KEY_SURNAME to uiState.registerSurname,
                                        AuthForms.KEY_EMAIL to uiState.registerEmail,
                                        AuthForms.KEY_PASSWORD to uiState.registerPassword,
                                        AuthForms.KEY_CONFIRM_PASSWORD to uiState.registerConfirmPassword
                                    )
                                }

                                val errors = remember(
                                    uiState.registerNameError, uiState.registerSurnameError,
                                    uiState.registerEmailError, uiState.registerPasswordError,
                                    uiState.registerConfirmPasswordError
                                ) {
                                    mapOf(
                                        AuthForms.KEY_NAME to uiState.registerNameError,
                                        AuthForms.KEY_SURNAME to uiState.registerSurnameError,
                                        AuthForms.KEY_EMAIL to uiState.registerEmailError,
                                        AuthForms.KEY_PASSWORD to uiState.registerPasswordError,
                                        AuthForms.KEY_CONFIRM_PASSWORD to uiState.registerConfirmPasswordError
                                    ).filterValues { it.isNotEmpty() }
                                }

                                FormComponent(
                                    formDefinition = AuthForms.registerForm,
                                    initialValues = initialValues,
                                    externalErrors = errors,
                                    isSubmitting = uiState.isLoading,
                                    onValueChange = { key, value ->
                                        when (key) {
                                            AuthForms.KEY_NAME -> viewModel.updateRegisterName(value)
                                            AuthForms.KEY_SURNAME -> viewModel.updateRegisterSurname(value)
                                            AuthForms.KEY_EMAIL -> viewModel.updateRegisterEmail(value)
                                            AuthForms.KEY_PASSWORD -> viewModel.updateRegisterPassword(value)
                                            AuthForms.KEY_CONFIRM_PASSWORD -> viewModel.updateRegisterConfirmPassword(value)
                                        }
                                    },
                                    onSubmit = { viewModel.submitRegister() }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(WrestlingTheme.dimensions.spacing_16))

                        // Botón para alternar entre login y registro
                        WrestlingButton(
                            text = if (uiState.isLoginMode)
                                AppStrings.Auth.noAccount
                            else
                                AppStrings.Auth.alreadyHaveAccount,
                            onClick = viewModel::toggleAuthMode,
                            type = WrestlingButtonType.TEXT,
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
                    WrestlingLoadingOverlay()
                }
            }
        }
    }
}
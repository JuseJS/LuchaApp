package org.iesharia.ui.screens.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import luchaapp.composeapp.generated.resources.Res
import luchaapp.composeapp.generated.resources.app_logo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.iesharia.ui.components.LuchaPrimaryButton
import org.iesharia.ui.components.LuchaSecondaryButton
import org.iesharia.ui.components.LuchaTextField
import org.iesharia.ui.components.LuchaTextButton
import org.iesharia.ui.theme.LuchaTheme

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(LuchaTheme.dimensions.spacing_24),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo o imagen representativa
                Spacer(modifier = Modifier.weight(0.1f))

                // Logo
                Image(
                    painter = painterResource(Res.drawable.app_logo),
                    contentDescription = "Logo de Lucha Canaria",
                    modifier = Modifier
                        .size(LuchaTheme.dimensions.login_logo_size)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_32))

                // Título de la app
                Text(
                    text = "Lucha Canaria",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_8))

                // Subtítulo
                Text(
                    text = "Tradición y Deporte",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
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
                                // Transición al login
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
                                // Transición al registro
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
                    LuchaTextButton(
                        text = if (uiState.isLoginMode) "¿No tienes cuenta? Regístrate" else "¿Ya tienes cuenta? Inicia sesión",
                        onClick = viewModel::toggleAuthMode,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.weight(0.2f))
            }

            // Indicador de carga
            AnimatedVisibility(
                visible = uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    emailError: String,
    passwordError: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = LuchaTheme.dimensions.spacing_24)
        )

        // Correo
        LuchaTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            keyboardType = KeyboardType.Email,
            isError = emailError.isNotEmpty(),
            errorMessage = emailError,
            leadingIcon = Icons.Filled.Email
        )

        // Contraseña
        LuchaTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            isPassword = true,
            imeAction = ImeAction.Done,
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError,
            leadingIcon = Icons.Filled.Lock
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

        // Botón login
        LuchaPrimaryButton(
            text = "Iniciar Sesión",
            onClick = onSubmit,
            enabled = !isLoading
        )
    }
}

@Composable
private fun RegisterForm(
    name: String,
    surname: String,
    email: String,
    password: String,
    confirmPassword: String,
    nameError: String,
    surnameError: String,
    emailError: String,
    passwordError: String,
    confirmPasswordError: String,
    onNameChange: (String) -> Unit,
    onSurnameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = LuchaTheme.dimensions.spacing_24)
        )

        // Nombre
        LuchaTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre",
            isError = nameError.isNotEmpty(),
            errorMessage = nameError,
            leadingIcon = Icons.Filled.Person
        )

        // Apellidos
        LuchaTextField(
            value = surname,
            onValueChange = onSurnameChange,
            label = "Apellidos",
            isError = surnameError.isNotEmpty(),
            errorMessage = surnameError,
            leadingIcon = Icons.Filled.Person
        )

        // Correo
        LuchaTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo electrónico",
            keyboardType = KeyboardType.Email,
            isError = emailError.isNotEmpty(),
            errorMessage = emailError,
            leadingIcon = Icons.Filled.Email
        )

        // Contraseña
        LuchaTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            isPassword = true,
            isError = passwordError.isNotEmpty(),
            errorMessage = passwordError,
            leadingIcon = Icons.Filled.Lock
        )

        // Confirmar contraseña
        LuchaTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = "Confirmar contraseña",
            isPassword = true,
            imeAction = ImeAction.Done,
            isError = confirmPasswordError.isNotEmpty(),
            errorMessage = confirmPasswordError,
            leadingIcon = Icons.Filled.Lock
        )

        Spacer(modifier = Modifier.height(LuchaTheme.dimensions.spacing_16))

        // Botón registro
        LuchaSecondaryButton(
            text = "Registrarse",
            onClick = onSubmit,
            enabled = !isLoading
        )
    }
}
package com.example.ense_as_ma.ui.screens.login

import android.net.Uri
import android.widget.VideoView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import android.widget.Toast
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Pantalla de inicio de sesión que permite a los usuarios autenticarse en la aplicación.
 *
 * @param modifier Modifier para personalizar el diseño del composable
 * @param viewModel ViewModel que maneja la lógica de autenticación
 * @param onLoginSuccess Callback que se ejecuta cuando el inicio de sesión es exitoso
 * @param onRegisterClick Callback que se ejecuta cuando el usuario quiere registrarse
 */
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Estados para manejar los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Contexto para mostrar mensajes Toast
    val context = LocalContext.current

    // Observar el estado de la UI desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()

    /**
     * Efecto que maneja los diferentes estados de la autenticación y
     * muestra retroalimentación apropiada al usuario
     */
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Success -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                errorMessage = (uiState as LoginUiState.Error).message
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
            else -> {
                errorMessage = null
            }
        }
    }

    // Contenedor principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Video de fondo en la parte superior
        AndroidView(
            factory = { context ->
                VideoView(context).apply {
                    setVideoURI(Uri.parse("android.resource://" + context.packageName + "/raw/login_video"))
                    setOnPreparedListener { mediaPlayer ->
                        mediaPlayer.isLooping = true
                        mediaPlayer.setVolume(0f, 0f)
                        start()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Título de la aplicación
        Text(
            text = "EnSeñasApp",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null  // Limpiar error al escribir
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña con opción para mostrar/ocultar
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null  // Limpiar error al escribir
            },
            label = { Text("Contraseña") },
            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = errorMessage != null,
            trailingIcon = {
                // Botón para alternar la visibilidad de la contraseña
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        if (isPasswordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible)
                            "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Área de mensaje de error
        errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón de inicio de sesión con estado de carga
        Button(
            onClick = { viewModel.signIn(email, password) },
            enabled = email.isNotBlank() && password.isNotBlank() &&
                    uiState !is LoginUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Iniciar sesión")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para navegar a la pantalla de registro
        TextButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}

/*

var showLogin by remember { mutableStateOf(true) }
                var isLoggedIn by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val authViewModel: AuthViewModel = viewModel()
                val forumViewModel: ForumViewModel = viewModel()
                val authState by authViewModel.uiState.collectAsState()

                // Observar cambios en el estado de autenticación
                LaunchedEffect(authState) {
                    when (authState) {
                        is AuthUiState.Success -> {
                            isLoggedIn = true
                        }
                        is AuthUiState.Error -> {
                            isLoggedIn = false
                            Toast.makeText(
                                context,
                                (authState as AuthUiState.Error).message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else -> {} // No hacer nada para otros estados
                    }
                }

                Surface(modifier = Modifier.fillMaxSize()) {
                    when {
                        isLoggedIn -> {
                            LandingPage(forumViewModel = forumViewModel)
                        }
                        showLogin -> {
                            LoginScreen(
                                onLoginSuccess = {
                                    isLoggedIn = true
                                    Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                                },
                                onRegisterClick = {
                                    showLogin = false
                                }
                            )
                        }
                        else -> {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    isLoggedIn = true
                                    Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
 */
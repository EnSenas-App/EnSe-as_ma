package com.example.ense_as_ma.ui.screens

import android.util.Log
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ense_as_ma.auth.AuthUiState
import com.example.ense_as_ma.auth.AuthViewModel

// Constante para identificación de logs
private const val TAG = "RegisterScreen"

/**
 * Pantalla de registro que permite a nuevos usuarios crear una cuenta en la aplicación.
 * Implementa validaciones de contraseña y muestra retroalimentación en tiempo real.
 *
 * @param viewModel ViewModel que maneja la lógica de autenticación
 * @param onRegisterSuccess Callback que se ejecuta cuando el registro es exitoso
 */
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit
) {
    // Estados para manejar los campos del formulario
    var user by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Observar el estado de la UI desde el ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Contenedor principal
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título de la pantalla
        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Campo de Usuario
        OutlinedTextField(
            value = user,
            onValueChange = {
                user = it
                errorMessage = null // Limpiar error al escribir
            },
            label = { Text("Usuario") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de correo electrónico
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                errorMessage = null // Limpiar error al escribir
            },
            label = { Text("Correo electrónico") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña con opción para mostrar/ocultar
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                errorMessage = null // Limpiar error al escribir
            },
            label = { Text("Contraseña") },
            visualTransformation = if (isPasswordVisible)
                VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
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

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de confirmación de contraseña
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                errorMessage = null // Limpiar error al escribir
            },
            label = { Text("Confirmar contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registro con validaciones
        Button(
            onClick = {
                // Registrar intento de registro en el log
                Log.d(TAG, "Intentando registro con email: $email")

                // Validación de contraseñas coincidentes
                if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                    Log.e(TAG, "Error: Las contraseñas no coinciden")
                    return@Button
                }

                // Validación de longitud mínima de contraseña
                if (password.length < 6) {
                    errorMessage = "La contraseña debe tener al menos 6 caracteres"
                    Log.e(TAG, "Error: Contraseña muy corta")
                    return@Button
                }

                // Intentar registro si pasa validaciones
                viewModel.signUp(email, password, user)
            },
            // Habilitar el botón solo si todos los campos están llenos y no hay errores
            enabled = email.isNotBlank() && password.isNotBlank() &&
                    confirmPassword.isNotBlank() && errorMessage == null &&
                    uiState !is AuthUiState.Loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Mostrar indicador de progreso durante el registro
            if (uiState is AuthUiState.Loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Registrarse")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Área de mensajes de error del formulario
        errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Manejo de estados de la UI
        when (uiState) {
            is AuthUiState.Success -> {
                Log.d(TAG, "Registro exitoso")
                LaunchedEffect(Unit) { onRegisterSuccess() }
            }
            is AuthUiState.Error -> {
                val message = (uiState as AuthUiState.Error).message
                Log.e(TAG, "Error en registro: $message")
                Text(
                    text = message,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            is AuthUiState.Loading -> {
                Log.d(TAG, "Cargando...")
            }
            else -> Unit
        }
    }
}
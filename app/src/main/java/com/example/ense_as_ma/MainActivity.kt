package com.example.ense_as_ma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.example.ense_as_ma.ui.screens.LandingPage
import com.example.ense_as_ma.ui.screens.LoginScreen
import com.example.ense_as_ma.ui.screens.RegisterScreen
import com.example.ense_as_ma.ui.theme.EnSe_as_maTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.ense_as_ma.auth.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ense_as_ma.auth.AuthUiState

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EnSe_as_maTheme {
                var showLogin by remember { mutableStateOf(true) }
                var isLoggedIn by remember { mutableStateOf(false) }
                val context = LocalContext.current
                val authViewModel: AuthViewModel = viewModel()
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
                            LandingPage()
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
            }
        }
    }
}
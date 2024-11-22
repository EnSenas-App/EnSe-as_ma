package com.example.ense_as_ma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingPage() {
    var selectedItem by remember { mutableStateOf(0) }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = selectedItem == 0,
                    onClick = { selectedItem = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Style, contentDescription = "Cartas") },
                    label = { Text("Cartas") },
                    selected = selectedItem == 1,
                    onClick = { selectedItem = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Forum, contentDescription = "Foro") },
                    label = { Text("Foro") },
                    selected = selectedItem == 2,
                    onClick = { selectedItem = 2 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Settings, contentDescription = "Ajustes") },
                    label = { Text("Ajustes") },
                    selected = selectedItem == 3,
                    onClick = { selectedItem = 3 }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Contenido según la pestaña seleccionada
            when (selectedItem) {
                0 -> HomeScreen()
                1 -> CardsScreen()
                2 -> ForumScreen()
                3 -> SettingsScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Text(
        text = "Pantalla de Inicio provisional",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CardsScreen() {
    Text(
        text = "Pantalla de Cartas provisional",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ForumScreen() {
    Text(
        text = "Pantalla del Foro provisional",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SettingsScreen() {
    Text(
        text = "Pantalla de Ajustes provisional",
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}
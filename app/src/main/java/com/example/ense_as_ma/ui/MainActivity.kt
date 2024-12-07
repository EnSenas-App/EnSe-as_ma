package com.example.ense_as_ma.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ense_as_ma.navigation.AppNavHost
import com.example.ense_as_ma.navigation.navigateSingleTopTo
import com.example.ense_as_ma.navigation.tabBarScreens
import com.example.ense_as_ma.ui.shared.AppNavigationBar
import com.example.ense_as_ma.ui.theme.EnSe_as_maTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContentActivity()
        }
    }
}

@Composable
fun ContentActivity() {
    EnSe_as_maTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentTabBarScreen = tabBarScreens.find {
            it.route == currentBackStack?.destination?.route
        }

        Scaffold(bottomBar = {
            if (currentTabBarScreen != null) {
                AppNavigationBar(
                    allScreens = tabBarScreens,
                    onTabSelected = { newScreen ->
                        navController.navigateSingleTopTo(newScreen.route)
                    },
                    currentScreen = currentTabBarScreen,
                )
            }}
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

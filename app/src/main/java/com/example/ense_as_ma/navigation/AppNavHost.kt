package com.example.ense_as_ma.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ense_as_ma.ui.screens.forum.ForumScreen
import com.example.ense_as_ma.ui.screens.login.LoginScreen

@Composable
fun AppNavHost (
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = Login.route,
        modifier = modifier
    ){
        composable(route = Login.route) {
            LoginScreen(
                onLoginSuccess = { navController.navigateSingleTopTo(Forum.route) },
                onRegisterClick = {}
            )
        }

        composable(route = SignsBot.route) {
            Text("ChatBot")
        }

        composable(route = Cards.route) {
            Text("Cards")
        }

        composable(route = Forum.route) {
            ForumScreen()
        }

        composable(route = Comments.route) {
            Text("Comments")
        }

        composable(route = Event.route) {
            Text("Event")
        }

        composable(route = Camera.route) {
            Text("Camera")
        }

        composable(route = Settings.route) {
            Text("Settings")
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
    popUpTo(
        this@navigateSingleTopTo.graph.findStartDestination().id
    ) {
        saveState = true
    }
    launchSingleTop = true
    restoreState = true
}

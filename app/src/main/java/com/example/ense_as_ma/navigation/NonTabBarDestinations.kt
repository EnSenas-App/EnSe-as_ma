package com.example.ense_as_ma.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface NonTabBarDestination {
    val route: String
}

data object Login : NonTabBarDestination {
    override val route = "login"
}

data object Comments : NonTabBarDestination {
    override val route = "comments"
    const val POST_ID_ARG = "postId"
    const val route_with_args = "comments?${POST_ID_ARG}={${POST_ID_ARG}}"

    val arguments = listOf(
        navArgument(POST_ID_ARG) { type = NavType.StringType }
    )
}

data object Event : NonTabBarDestination {
    override val route = "comments"
    const val EVENT_ID_ARG = "eventId"
    const val route_with_args = "comments?${EVENT_ID_ARG}={${EVENT_ID_ARG}}"

    val arguments = listOf(
        navArgument(EVENT_ID_ARG) { type = NavType.StringType }
    )
}

package com.example.ense_as_ma.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed interface TabBarDestination {
    val route: String
    val selectedIcon: ImageVector
    val unSelectedIcon: ImageVector
}

data object SignsBot : TabBarDestination {
    override val route = "signs_bot"
    override val selectedIcon = Icons.Filled.ChatBubble
    override val unSelectedIcon = Icons.Outlined.ChatBubbleOutline
}

data object Cards : TabBarDestination {
    override val route = "cards"
    override val selectedIcon = Icons.Filled.CollectionsBookmark
    override val unSelectedIcon = Icons.Outlined.CollectionsBookmark
}

data object Forum : TabBarDestination {
    override val route = "forum"
    override val selectedIcon = Icons.Filled.Forum
    override val unSelectedIcon = Icons.Outlined.Forum
}

data object Camera : TabBarDestination {
    override val route = "camera"
    override val selectedIcon = Icons.Filled.CameraAlt
    override val unSelectedIcon = Icons.Outlined.CameraAlt
}

data object Settings : TabBarDestination {
    override val route = "settings"
    override val selectedIcon = Icons.Filled.Settings
    override val unSelectedIcon = Icons.Outlined.Settings
}

val tabBarScreens = listOf(SignsBot, Cards, Forum, Camera, Settings)

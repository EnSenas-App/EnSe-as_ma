package com.example.ense_as_ma.ui.shared

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ense_as_ma.R
import com.example.ense_as_ma.navigation.TabBarDestination

/**
 *
 */
@Composable
fun AppNavigationBar(
    allScreens: List<TabBarDestination>,
    onTabSelected: (TabBarDestination) -> Unit,
    currentScreen: TabBarDestination,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        tonalElevation = 2.dp,
        containerColor = colorScheme.surface,
        contentColor = colorScheme.onSurface,
        windowInsets = NavigationBarDefaults.windowInsets,
        modifier = modifier
    ) {
        allScreens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = { onTabSelected(screen) },
                label = { Text(
                    text = when (screen.route) {
                        "signs_bot" -> stringResource(R.string.label_signs_bot)
                        "cards" -> stringResource(R.string.label_cards)
                        "forum" -> stringResource(R.string.label_forum)
                        "camera" -> stringResource(R.string.label_camera)
                        else -> stringResource(R.string.label_settings)
                    },
                    style = typography.bodySmall,
                )},
                icon = { Icon(
                    imageVector = if (currentScreen == screen) {
                        screen.selectedIcon
                    }else{ screen.unSelectedIcon },
                    contentDescription = stringResource(id = R.string.description_tab_for, screen.route),
                )},
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = colorScheme.primary,
                    unselectedIconColor = colorScheme.onSurface,
                    selectedTextColor = colorScheme.primary,
                    unselectedTextColor = colorScheme.onSurface
                ),
            )
        }
    }
}

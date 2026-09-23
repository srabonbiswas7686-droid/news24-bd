package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NewsRedContainer
import com.example.ui.theme.NewsRedPrimary
import com.example.viewmodel.AppScreen

data class NavItem(
    val titleBn: String,
    val screen: AppScreen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun NewsBottomNavBar(
    currentScreen: AppScreen,
    onTabSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            titleBn = "হোম",
            screen = AppScreen.Home,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            testTag = "nav_tab_home"
        ),
        NavItem(
            titleBn = "সর্বশেষ",
            screen = AppScreen.Latest,
            selectedIcon = Icons.Filled.Newspaper,
            unselectedIcon = Icons.Outlined.Newspaper,
            testTag = "nav_tab_latest"
        ),
        NavItem(
            titleBn = "বিভাগ",
            screen = AppScreen.Categories,
            selectedIcon = Icons.Filled.Category,
            unselectedIcon = Icons.Outlined.Category,
            testTag = "nav_tab_categories"
        ),
        NavItem(
            titleBn = "অনুসন্ধান",
            screen = AppScreen.Search,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
            testTag = "nav_tab_search"
        ),
        NavItem(
            titleBn = "মেনু",
            screen = AppScreen.Menu,
            selectedIcon = Icons.Filled.Menu,
            unselectedIcon = Icons.Outlined.Menu,
            testTag = "nav_tab_menu"
        )
    )

    NavigationBar(
        modifier = modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("main_bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = when (item.screen) {
                is AppScreen.Home -> currentScreen is AppScreen.Home
                is AppScreen.Latest -> currentScreen is AppScreen.Latest
                is AppScreen.Categories -> currentScreen is AppScreen.Categories || currentScreen is AppScreen.CategoryDetail
                is AppScreen.Search -> currentScreen is AppScreen.Search
                is AppScreen.Menu -> currentScreen is AppScreen.Menu || currentScreen is AppScreen.AdminDashboard || currentScreen is AppScreen.UserAccount || currentScreen is AppScreen.Bookmarks
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.titleBn
                    )
                },
                label = {
                    Text(
                        text = item.titleBn,
                        fontSize = 11.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = NewsRedPrimary,
                    selectedTextColor = NewsRedPrimary,
                    indicatorColor = NewsRedContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}

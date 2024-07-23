package com.rksrtx76.cinemax.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val title : String,
    val selectedIcon : ImageVector,
    val unselectedIcon : ImageVector,
)

val items = listOf(
    BottomNavigationItem(
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavigationItem(
        title = "Popular",
        selectedIcon = Icons.Filled.LocalFireDepartment,
        unselectedIcon = Icons.Outlined.LocalFireDepartment
    ),
    BottomNavigationItem(
        title = "Tv Series",
        selectedIcon = Icons.Filled.LiveTv,
        unselectedIcon = Icons.Outlined.LiveTv
    ),
    BottomNavigationItem(
        title = "Bookmarks",
        selectedIcon = Icons.Filled.Bookmarks,
        unselectedIcon = Icons.Outlined.Bookmarks
    ),

    )
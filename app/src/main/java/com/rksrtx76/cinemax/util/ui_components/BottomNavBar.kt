package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.ui.theme.bottom_nav
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.BottomNav
import com.rksrtx76.cinemax.util.BottomNavigationItem
import com.rksrtx76.cinemax.util.Constants

@Composable
fun BottomNavBar(
    items: List<BottomNavigationItem>,
    selectedItem: MutableState<Int>,
    bottomBarNavController: NavController,
) {

    val isDarkTheme = isSystemInDarkTheme()
    val containerColor = if (isDarkTheme) bottom_nav else MaterialTheme.colorScheme.onSecondary

    NavigationBar(
        containerColor = containerColor,
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem.value == index,
                onClick = {
                    selectedItem.value = index
                    when (selectedItem.value) {
                        0 -> bottomBarNavController.navigate(BottomNav.MEDIA_HOME_SCREEN)
                        1 -> bottomBarNavController.navigate("${BottomNav.MEDIA_LIST_SCREEN}?type=${Constants.popularScreen}")
                        2 -> bottomBarNavController.navigate("${BottomNav.MEDIA_LIST_SCREEN}?type=${Constants.tvSeriesScreen}")
                        3 -> bottomBarNavController.navigate(BottomNav.BOOKMARKS_SCREEN)
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        fontFamily = font,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (index == selectedItem.value) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = containerColor,
                    unselectedIconColor = containerColor,
                    indicatorColor = containerColor
                )
            )
        }
    }
}
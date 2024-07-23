package com.rksrtx76.cinemax.main.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rksrtx76.cinemax.details.presentation.bookmark.BookmarkedMediaListScreen
import com.rksrtx76.cinemax.main.presentation.MainUiEvents
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.main.presentation.home.MediaHomeScreen
import com.rksrtx76.cinemax.main.presentation.popular.MediaListScreen
import com.rksrtx76.cinemax.util.BottomNav
import com.rksrtx76.cinemax.util.items
import com.rksrtx76.cinemax.util.ui_components.BottomNavBar

@Composable
fun MediaMainScreen(
    navController: NavController,
    mainUiState: MainUiState,
    onEvent : (MainUiEvents) -> Unit
) {

    val items = items

    val selectedItem = rememberSaveable {
        mutableStateOf(0)
    }

    val bottomBarNavController = rememberNavController()

    androidx.compose.material3.Scaffold(
        content = { paddingValues ->
            BottomNavigationScreens(
                selectedItem = selectedItem,
                modifier = Modifier
                    .padding(
                        bottom = paddingValues.calculateBottomPadding()
                    ),
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        },
        bottomBar = {
            BottomNavBar(
                items = items,
                selectedItem = selectedItem,
                bottomBarNavController = bottomBarNavController
            )
        }
    )

}

@Composable
fun BottomNavigationScreens(
    selectedItem : MutableState<Int>,
    modifier: Modifier = Modifier,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState,
    onEvent: (MainUiEvents) -> Unit
){
    NavHost(
        modifier = modifier,
        navController = bottomBarNavController,
        startDestination = BottomNav.MEDIA_HOME_SCREEN
    ){
        composable(BottomNav.MEDIA_HOME_SCREEN){
            MediaHomeScreen(
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(
            "${BottomNav.MEDIA_LIST_SCREEN}?type={type}",
            arguments = listOf(
                navArgument("type"){
                    type = NavType.StringType
                }
            )
        ){ navBackStackEntry ->
            MediaListScreen(
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                navBackStackEntry = navBackStackEntry,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(BottomNav.BOOKMARKS_SCREEN){ navBackStackEntry ->
            BookmarkedMediaListScreen(
                selectedItem = selectedItem,
                navController = navController,
                bottomBarNavController = bottomBarNavController,
                navBackStackEntry = navBackStackEntry,
                onEvent = onEvent
            )
        }
    }
}
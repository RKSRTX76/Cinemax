package com.rksrtx76.cinemax

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenEvents
import com.rksrtx76.cinemax.details.presentation.MediaDetailsViewModel
import com.rksrtx76.cinemax.details.presentation.cast.CastListScreen
import com.rksrtx76.cinemax.details.presentation.details_screen.MediaDetailScreen
import com.rksrtx76.cinemax.details.presentation.similar_media.SimilarMediaListScreen
import com.rksrtx76.cinemax.details.presentation.watch_video.WatchVideoScreen
import com.rksrtx76.cinemax.main.presentation.MainUiEvents
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.main.presentation.MainViewModel
import com.rksrtx76.cinemax.main.presentation.main.MediaMainScreen
import com.rksrtx76.cinemax.search.presentation.search.SearchScreen
import com.rksrtx76.cinemax.ui.theme.CINEMAXTheme
import com.rksrtx76.cinemax.util.Screen
import com.rksrtx76.cinemax.util.ui_components.SomethingWentWrong
import com.rksrtx76.cinemax.util.ui_components.SplashScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            CINEMAXTheme {
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainUiState = mainViewModel.mainUiState.collectAsState().value


                installSplashScreen().apply {
                    setKeepOnScreenCondition{
                        mainViewModel.showSplashScreen.value
                    }
                }

                Navigation(
                    mainUiState = mainUiState,
                    onEvent = mainViewModel::onEvent
                )
            }
        }
    }
}


@Composable
fun Navigation(
    mainUiState : MainUiState,
    onEvent : (MainUiEvents) -> Unit
){

    val navController = rememberNavController()
    val mediaDetailsViewModel = hiltViewModel<MediaDetailsViewModel>()
    val mediaDetailsScreenState = mediaDetailsViewModel.mediaDetailsScreenState.collectAsState().value
    val lifecycleOwner = LocalLifecycleOwner.current
    var isVideoPlaying by remember { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = Screen.SPLASH_SCREEN
    ) {
        composable(Screen.SPLASH_SCREEN){
            SplashScreen(navController = navController)
        }
        composable(Screen.MAIN_SCREEN){
            MediaMainScreen(
                navController = navController,
                mainUiState = mainUiState,
                onEvent = onEvent
            )
        }

        composable(Screen.SEARCH_SCREEN){
            SearchScreen(
                navController = navController,
                mainUiState = mainUiState
            )
        }
        composable(
            "${Screen.DETAILS_SCREEN}?id={id}&type={type}&category={category}",
            arguments = listOf(
                navArgument("id"){ type = NavType.IntType},
                navArgument("type"){ type = NavType.StringType},
                navArgument("category"){ type = NavType.StringType},
            )
        ){

            val id = it.arguments?.getInt("id") ?: 0
            val type = it.arguments?.getString("type") ?: ""
            val category = it.arguments?.getString("category") ?: ""

            LaunchedEffect(key1 = true) {
                mediaDetailsViewModel.onEvent(
                    MediaDetailsScreenEvents.SetDataAndLoad(
                        moviesGenresList = mainUiState.moviesGenresList,
                        tvGenreList = mainUiState.tvGenresList,
                        id = id,
                        type = type,
                        category = category
                    )
                )
            }

            if(mediaDetailsScreenState.media != null){
                MediaDetailScreen(
                    lifecycleOwner = lifecycleOwner,
                    navController = navController,
                    media = mediaDetailsScreenState.media,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    onEvent = mediaDetailsViewModel::onEvent,
                    onBookmarkToggle = { id, isBookmarked ->
                        mediaDetailsViewModel.onEvent(
                            MediaDetailsScreenEvents.ToggleBookmark(id, isBookmarked)
                        )
                    }
                )
            }else{
                SomethingWentWrong()
            }
        }

        composable(
            "${Screen.CAST_LIST_SCREEN}?castId={castId}") {
            CastListScreen(
                mediaDetailsScreenState = mediaDetailsScreenState,
            )
        }

        composable(
            "${Screen.SIMILAR_MEDIA_SCREEN}?title={title}",
            arguments = listOf(
                navArgument("title"){ type = NavType.StringType }
            )
        ){
            val name = it.arguments?.getString("title") ?: ""

            SimilarMediaListScreen(
                navController = navController,
                mediaDetailsScreenState = mediaDetailsScreenState,
                name = name
            )
        }

        composable(
            "${Screen.WATCH_VIDEO_SCREEN}?videoId={videoId}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ){
            val videoId = it.arguments?.getString("videoId") ?: ""

            WatchVideoScreen(
                lifecycleOwner = LocalLifecycleOwner.current,
                videoId = videoId,
                onVideoPlay = { isPlaying ->
                    isVideoPlaying = isPlaying
                }

            )
        }
    }

}
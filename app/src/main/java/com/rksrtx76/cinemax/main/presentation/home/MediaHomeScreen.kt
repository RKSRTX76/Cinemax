package com.rksrtx76.cinemax.main.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rksrtx76.cinemax.main.presentation.MainUiEvents
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.ui.theme.BigRadius
import com.rksrtx76.cinemax.ui.theme.MediumRadius
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Constants.NOW_PLAYING
import com.rksrtx76.cinemax.util.Constants.airingTodayTvSeriesScreen
import com.rksrtx76.cinemax.util.Constants.popularScreen
import com.rksrtx76.cinemax.util.Constants.recommendedListScreen
import com.rksrtx76.cinemax.util.Constants.topRatedAllListScreen
import com.rksrtx76.cinemax.util.Constants.trendingAllListScreen
import com.rksrtx76.cinemax.util.Constants.tvSeriesScreen
import com.rksrtx76.cinemax.util.Constants.upcomingMoviesScreen
import com.rksrtx76.cinemax.util.Screen
import com.rksrtx76.cinemax.util.ui_components.AutoSwipeSection
import com.rksrtx76.cinemax.util.ui_components.NonFocusedTopBar
import com.rksrtx76.cinemax.util.ui_components.ShouldShowMediaHomeScreenSectionOrShimmer
import com.rksrtx76.cinemax.util.ui_components.shimmerEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaHomeScreen(
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState : MainUiState,
    onEvent : (MainUiEvents) -> Unit
){

    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current
    BackHandler(
        enabled = true
    ) {
        (context as Activity).finish()
    }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }

    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1000)
        onEvent(MainUiEvents.Refresh(type = Constants.homeScreen))

        refreshing = false
    }

    val refreshState = rememberPullRefreshState(refreshing= refreshing, onRefresh = { refresh() })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState),
        contentAlignment = Alignment.BottomCenter
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.surface)
                .padding(top = 62.dp)
        ) {

            if(mainUiState.specialList.isEmpty()){
                Box(
                    modifier = Modifier
                        .height(450.dp)
                        .fillMaxWidth(0.9f)
//                        .padding(top = 20.dp, bottom = 12.dp)
//                        .clip(RoundedCornerShape(MediumRadius))
                        .shimmerEffect(false)
//                        .align(CenterHorizontally)
                )
            }
            else{
                AutoSwipeSection(
                    navController = navController,
                    mainUiState = mainUiState
                )
            }

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = trendingAllListScreen,
                showShimmer = mainUiState.trendingAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = popularScreen,
                showShimmer = mainUiState.popularMoviesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = tvSeriesScreen,
                showShimmer = mainUiState.tvSeriesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = topRatedAllListScreen,
                showShimmer = mainUiState.topRatedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = recommendedListScreen,
                showShimmer = mainUiState.recommendedAllList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = NOW_PLAYING,
                showShimmer = mainUiState.nowPlayingMoviesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = airingTodayTvSeriesScreen,
                showShimmer = mainUiState.airingTvSeriesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )


            Spacer(modifier = Modifier.padding(vertical = 8.dp))

            ShouldShowMediaHomeScreenSectionOrShimmer(
                type = upcomingMoviesScreen,
                showShimmer = mainUiState.upcomingMoviesList.isEmpty(),
                navController = navController,
                navHostController = bottomBarNavController,
                mainUiState = mainUiState
            )

            Spacer(modifier = Modifier.padding(vertical = 8.dp))
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = refreshState,
            modifier = Modifier
                .align(TopCenter)
                .padding(top = (BigRadius - 8).dp)
        )

    }
    NonFocusedTopBar(
        toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
        navController = navController
    )

}
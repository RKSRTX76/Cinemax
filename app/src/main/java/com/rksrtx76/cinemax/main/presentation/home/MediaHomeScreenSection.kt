package com.rksrtx76.cinemax.main.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.BottomNav
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.ui_components.Item

@Composable
fun MediaHomeScreenSection(
    type: String,
    navController: NavController,
    bottomBarNavController: NavHostController,
    mainUiState: MainUiState
){
    val title = when(type){
        Constants.trendingAllListScreen ->{
            stringResource(R.string.trending)
        }
        Constants.tvSeriesScreen ->{
            stringResource(R.string.tv_series)
        }
        Constants.recommendedListScreen ->{
            stringResource(R.string.recommended)
        }
        Constants.popularScreen ->{
            stringResource(R.string.popular)
        }
        Constants.NOW_PLAYING ->{
            stringResource(R.string.now_playing)
        }
        Constants.upcomingMoviesScreen ->{
            stringResource(R.string.upcoming_movies)
        }
        Constants.airingTodayTvSeriesScreen ->{
            stringResource(R.string.airing_today)
        }
        else -> {
            stringResource(R.string.top_rated)
        }
    }

    val mediaList = when(type){
        Constants.trendingAllListScreen ->{
            mainUiState.trendingAllList.take(50)
        }
        Constants.tvSeriesScreen ->{
            mainUiState.tvSeriesList.take(50)
        }
        Constants.recommendedListScreen ->{
            mainUiState.recommendedAllList.take(20)
        }
        Constants.popularScreen ->{
            mainUiState.popularMoviesList.take(50)
        }
        Constants.NOW_PLAYING ->{
            mainUiState.nowPlayingMoviesList.take(20)
        }
        Constants.upcomingMoviesScreen ->{
            mainUiState.upcomingMoviesList.take(50)
        }
        Constants.airingTodayTvSeriesScreen ->{
            mainUiState.airingTvSeriesList.take(10)
        }
        else -> {
            mainUiState.topRatedMoviesList.take(30)
        }
    }

    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            androidx.compose.material3.Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = font,
                fontSize = 20.sp
            )
            androidx.compose.material3.Text(
                modifier = Modifier
                    .alpha(0.7f)
                    .clickable {
                        bottomBarNavController.navigate(
                            "${BottomNav.MEDIA_LIST_SCREEN}?type=${type}"
                        )
                    },
                text = stringResource(R.string.see_all),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontFamily = font,
                fontSize = 14.sp
            )
        }

        LazyRow {
            items(mediaList.size){
                var paddingEnd = 0.dp
                if(it == mediaList.size -1 ){
                    paddingEnd = 16.dp
                }

                Item(
                    media = mediaList[it],
                    navController = navController,
                    modifier = Modifier
                        .height(200.dp)
                        .width(150.dp)
                        .padding(start = 16.dp, end = paddingEnd)
                )
            }
        }
    }
}
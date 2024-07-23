package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.main.presentation.home.MediaHomeScreenSection
import com.rksrtx76.cinemax.util.Constants

@Composable
fun ShouldShowMediaHomeScreenSectionOrShimmer(
    type : String,
    showShimmer : Boolean,
    navController: NavController,
    navHostController: NavHostController,
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
        else -> {
            stringResource(R.string.top_rated)
        }
    }

    if(showShimmer){
        ShowHomeShimmer(
            title = title,
            modifier = Modifier
                .height(220.dp)
                .width(150.dp)
                .padding(top = 20.dp, start = 16.dp, bottom = 12.dp),
            paddingEnd = 16.dp
        )
    }else{
        MediaHomeScreenSection(
            type = type,
            navController = navController,
            bottomBarNavController = navHostController,
            mainUiState = mainUiState
        )
    }
}
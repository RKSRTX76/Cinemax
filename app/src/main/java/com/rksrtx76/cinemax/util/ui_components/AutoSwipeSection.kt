package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.main.presentation.MainUiState

@Composable
fun AutoSwipeSection(
    navController: NavController,
    mainUiState: MainUiState,
){
    Column {
        AutoSwipeImagePager(
            mediaList = mainUiState.specialList.take(7),
            navController = navController,
            modifier = Modifier
                .height(450.dp)
                .fillMaxWidth(0.9f)
        )
    }
}
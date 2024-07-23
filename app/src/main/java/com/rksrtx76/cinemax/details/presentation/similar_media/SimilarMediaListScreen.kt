package com.rksrtx76.cinemax.details.presentation.similar_media

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.ui.theme.SmallRadius
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.ui_components.ListShimmerEffect
import com.rksrtx76.cinemax.util.ui_components.header

@Composable
fun SimilarMediaListScreen(
    navController: NavController,
    mediaDetailsScreenState: MediaDetailsScreenState,
    name : String
){
    val title = stringResource(R.string.similar_to, name)

    val mediaList = mediaDetailsScreenState.similarMediaList

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ){

        if(mediaList.isEmpty()){
            ListShimmerEffect(
                title = title,
                radius = SmallRadius
            )
        }else{
            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                contentPadding = PaddingValues(top = SmallRadius.dp),
                columns = GridCells.Fixed(2)
            ) {

                header {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 22.dp),
                            textAlign = TextAlign.Center,
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = font,
                            fontSize = 20.sp
                        )
                    }
                }

                items(mediaList.size){ index->
                    SimilarMediaItem(
                        media = mediaList[index],
                        navController = navController,
                        mediaDetailsScreenState = mediaDetailsScreenState
                    )
                }
            }
        }
    }
}
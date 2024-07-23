package com.rksrtx76.cinemax.details.presentation.details_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.Screen
import com.rksrtx76.cinemax.util.ui_components.Item

@Composable
fun SimilarMediaSection(
    navController: NavController,
    media: Media,
    mediaDetailsScreenState: MediaDetailsScreenState
){
    val title = stringResource(R.string.similar)
    val mediaList = mediaDetailsScreenState.similarMediaList

    if(mediaList.isNotEmpty()){
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = font,
                    fontSize = 18.sp
                )
                Text(
                    modifier = Modifier
                        .alpha(0.85f)
                        .clickable {
                            navController.navigate(
                                "${Screen.SIMILAR_MEDIA_SCREEN}?title=${media.title}"
                            )
                        },
                    text = stringResource(R.string.see_all),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = font,
                    fontSize = 14.sp
                )
            }
            LazyRow {
                items(mediaList.size){
                    var paddingEnd = 0.dp
                    if(it == mediaList.size-1){
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
}
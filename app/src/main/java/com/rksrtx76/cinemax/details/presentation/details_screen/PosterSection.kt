package com.rksrtx76.cinemax.details.presentation.details_screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.ui.theme.SmallRadius

@Composable
fun PosterSection(
    media: Media,
    isVideoPlaying: Boolean,
    onImageLoaded: (color: Color) -> Unit
) {
    val posterUrl = "${MediaApiService.IMAGE_BASE_URL}${media.posterPath}"
    val posterPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .size(Size.ORIGINAL)
            .build()
    )
    val posterState = posterPainter.state

    val posterWidth by animateDpAsState(targetValue = if (isVideoPlaying) 145.dp else 180.dp, animationSpec = tween(durationMillis = 300))
    val posterHeight by animateDpAsState(targetValue = if (isVideoPlaying) 190.dp else 250.dp, animationSpec = tween(durationMillis = 300))
    val spacerHeight by animateDpAsState(targetValue = if (isVideoPlaying) 260.dp else 200.dp, animationSpec = tween(durationMillis = 300))
    val cardElevation by animateFloatAsState(targetValue = if (isVideoPlaying) 8f else 5f, animationSpec = tween(durationMillis = 300))

    Column {
        Spacer(modifier = Modifier.height(spacerHeight))

        Card(
            modifier = Modifier
                .width(posterWidth)
                .height(posterHeight)
                .padding(start = 16.dp),
            shape = RoundedCornerShape(SmallRadius.dp),
            elevation = CardDefaults.cardElevation(cardElevation.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                MovieImage(
                    imageState = posterState,
                    description = media.title,
                    noImageId = painterResource(id = R.drawable.cinemax)
                ) { color ->
                    onImageLoaded(color)
                }
            }
        }
    }
}
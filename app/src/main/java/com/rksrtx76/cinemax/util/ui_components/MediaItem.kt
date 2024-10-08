package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService.Companion.IMAGE_BASE_URL
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.ui.theme.Radius
import com.rksrtx76.cinemax.ui.theme.RadiusContainer
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.Screen

@Composable
fun MediaItem(
    media: Media,
    navController: NavController,
    mainUiState: MainUiState,
    modifier: Modifier = Modifier,
) {
    val imageUrl = "${IMAGE_BASE_URL}${media.posterPath}"
    val title = media.title

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    val imageState = imagePainter.state

    val defaultDominantColor = MaterialTheme.colorScheme.primaryContainer
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(
        modifier = modifier
            .padding(bottom = 12.dp, start = 4.dp, end = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(RadiusContainer.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondaryContainer,
                            dominantColor
                        )
                    )
                )
                .clickable {
                    navController.navigate(
                        "${Screen.DETAILS_SCREEN}?id=${media.id}&type=${media.mediaType}&category=${media.category}"
                    )
                }
        ) {
            Box(
                modifier = Modifier
                    .height(220.dp)
                    .fillMaxSize()
                    .padding(6.dp)
            ) {
                if (imageState is AsyncImagePainter.State.Success) {
                    val imageBitmap = imageState.result.drawable.toBitmap()

                    dominantColor = getAverageColor(imageBitmap = imageBitmap.asImageBitmap())

                    Image(
                        bitmap = imageBitmap.asImageBitmap(),
                        contentScale = ContentScale.Crop,
                        contentDescription = title,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(Radius.dp))
                            .background(MaterialTheme.colorScheme.background)
                    )
                }

                if (imageState is AsyncImagePainter.State.Error) {
                    dominantColor = MaterialTheme.colorScheme.primary

                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(Radius.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .padding(32.dp)
                            .alpha(0.8f),
                        painter = painterResource(id = R.drawable.cinemax),
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                if (imageState is AsyncImagePainter.State.Loading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center),
                        strokeWidth = 2.dp
                    )
                }
            }

            var badgeCount by remember { mutableStateOf(0) }

            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                text = title,
                fontFamily = font,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = title
                            .substring(lineEndIndex)
                            .count { it == '.' }
                    }
                }
            )

            val genres = GenresProvider(
                genre_ids = media.genreIds,
                allGenres = if (media.mediaType == Constants.MOVIE) mainUiState.moviesGenresList else mainUiState.tvGenresList
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 2.dp),
                text = genres,
                fontFamily = font,
                fontSize = 12.5.sp,
                maxLines = 1,
                color = Color.LightGray,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        val lineEndIndex = textLayoutResult.getLineEnd(
                            lineIndex = 0,
                            visibleEnd = true
                        )
                        badgeCount = genres
                            .substring(lineEndIndex)
                            .count { it == '.' }
                    }
                },
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp, start = 11.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RatingBar(
                        modifier = Modifier,
                        starsModifier = Modifier.size(18.dp),
                        rating = media.voteAverage / 2
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = media.voteAverage.toString().take(3),
                        fontFamily = font,
                        fontSize = 14.sp,
                        maxLines = 1,
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}


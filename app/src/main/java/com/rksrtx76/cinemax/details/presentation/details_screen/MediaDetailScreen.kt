package com.rksrtx76.cinemax.details.presentation.details_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenEvents
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.details.presentation.cast.CastSection
import com.rksrtx76.cinemax.main.data.remote.api.MediaApiService
import com.rksrtx76.cinemax.main.domain.model.Media

@Composable
fun MediaDetailScreen(
    lifecycleOwner: LifecycleOwner,
    navController: NavController,
    media: Media,
    mediaDetailsScreenState: MediaDetailsScreenState,
    onEvent: (MediaDetailsScreenEvents) -> Unit,
    onBookmarkToggle: (Int, Boolean) -> Unit
) {

    var isVideoPlaying by remember { mutableStateOf(false) }

    val imageUrl = "${MediaApiService.IMAGE_BASE_URL}${media.backdropPath}"

    var isBookmarked by remember { mutableStateOf(media.isBookmarked) }

    val imagePainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .size(Size.ORIGINAL)
            .build()
    )

    val surface = MaterialTheme.colorScheme.surface
    var averageColor by remember { mutableStateOf(surface) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                VideoSection(
                    lifecycleOwner = lifecycleOwner,
                    mediaDetailsScreenState = mediaDetailsScreenState,
                    media = media,
                    imageState = imagePainter.state,
                    onEvent = onEvent,
                    onImageLoaded = { color ->
                        averageColor = color
                    },
                    onVideoPlay = { isPlaying ->
                        isVideoPlaying = isPlaying
                    },
                    navController = navController,
                    isBookmarked = isBookmarked,
                    onBookmarkToggle = { newBookmarkState ->
                        isBookmarked = newBookmarkState
                        onBookmarkToggle(media.id,newBookmarkState)
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    PosterSection(
                        media = media,
                        isVideoPlaying = isVideoPlaying,
                        onImageLoaded = { averageColor = it }
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    InfoSection(
                        media = media,
                        mediaDetailsScreenState = mediaDetailsScreenState
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OverviewSection(media = media)

            CastSection(
                isLoading = mediaDetailsScreenState.isLoading,
                castList = mediaDetailsScreenState.castList,
                paddingEnd = 16.dp,
                navController = navController,
                title = stringResource(R.string.cast)
            )
            Spacer(modifier = Modifier.height(16.dp))

            SimilarMediaSection(
                navController = navController,
                media = media,
                mediaDetailsScreenState = mediaDetailsScreenState
            )

            Spacer(modifier = Modifier.height(16.dp))

        }

    }
}
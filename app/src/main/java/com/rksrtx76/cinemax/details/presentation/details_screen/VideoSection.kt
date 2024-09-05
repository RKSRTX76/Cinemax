package com.rksrtx76.cinemax.details.presentation.details_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenEvents
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.details.presentation.watch_video.WatchVideoScreen
import com.rksrtx76.cinemax.main.domain.model.Media

@Composable
fun VideoSection(
    lifecycleOwner: LifecycleOwner,
    mediaDetailsScreenState: MediaDetailsScreenState,
    media: Media,
    imageState: AsyncImagePainter.State,
    onEvent: (MediaDetailsScreenEvents) -> Unit,
    onImageLoaded: (color: Color) -> Unit,
    onVideoPlay: (Boolean) -> Unit,
    navController: NavController,
    isBookmarked: Boolean,
    onBookmarkToggle : (Boolean) -> Unit
) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {
                if (mediaDetailsScreenState.videoList.isNotEmpty()) {
                    onEvent(MediaDetailsScreenEvents.NavigateToWatchVideo)
                    isPlaying = true
                    onVideoPlay(isPlaying)
                } else {
                    Toast
                        .makeText(
                            context,
                            context.getString(R.string.no_video_available_at_the_moment),
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (isPlaying) {
            WatchVideoScreen(
                lifecycleOwner = lifecycleOwner,
                videoId = mediaDetailsScreenState.videoId,
                onVideoPlay = { playing ->
                    onVideoPlay(playing)
                    isPlaying = playing
                }
            )
        } else {
            MovieImage(
                imageState = imageState,
                description = media.title,
                noImageId = null
            ) { color ->
                onImageLoaded(color)
            }
            // Back Icon
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .size(40.dp)
                    .background(Color.LightGray)
                    .clickable {
                        navController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(
                onClick = {
                    val newBookmarkState = !media.isBookmarked
                    onBookmarkToggle(newBookmarkState)
                    Toast.makeText(
                        context,
                        if (newBookmarkState) context.getString(R.string.added_to_bookmark)
                        else context.getString(R.string.removed_from_bookmark),
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)

            ) {
                Icon(
                    imageVector = if (media.isBookmarked ) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = colorResource(R.color.ruby_red),
                    modifier = Modifier.size(30.dp)
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .size(50.dp)
                    .alpha(0.7f)
                    .background(Color.LightGray)
            )
            Icon(
                imageVector = Icons.Rounded.PlayArrow,
                contentDescription = stringResource(R.string.watch_trailer),
                tint = Color.Black,
                modifier = Modifier.size(35.dp)
            )
        }
    }
}
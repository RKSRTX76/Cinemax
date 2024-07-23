package com.rksrtx76.cinemax.details.presentation.details_screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.ui_components.GenresProvider
import com.rksrtx76.cinemax.util.ui_components.RatingBar

@Composable
fun InfoSection(
    media: Media,
    mediaDetailsScreenState: MediaDetailsScreenState
){
    val genres = GenresProvider(
        genre_ids = media.genreIds,
        allGenres = if (media.mediaType == Constants.MOVIE) mediaDetailsScreenState.moviesGenreList else mediaDetailsScreenState.tvGenresList
    )

    Column {

        Spacer(modifier = Modifier.height(260.dp))

        Text(
            text = media.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold,
            fontFamily = font,
            fontSize = 19.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RatingBar(
                modifier = Modifier,
                starsModifier = Modifier.size(18.dp),
                rating = media.voteAverage.div(2)
            )

            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = media.voteAverage.toString().take(3),
                fontFamily = font,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = if(media.releaseDate != Constants.unavailable) media.releaseDate.take(4) else "",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 0.dp),
                text = if(media.adult) "18+" else "12+",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 0.dp),
                text = if(media.mediaType == "movie") "Movie" else "Series",
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = font,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(8.dp))

        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier= Modifier.padding(end = 8.dp),
            text = genres,
            fontFamily = font,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier= Modifier.padding(end = 8.dp),
            text = mediaDetailsScreenState.readableTime,
            fontFamily = font,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )
    }
}
package com.rksrtx76.cinemax.details.presentation.cast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.details.presentation.MediaDetailsScreenState
import com.rksrtx76.cinemax.ui.theme.SmallRadius
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.ui_components.ListShimmerEffect

@Composable
fun CastListScreen(
    mediaDetailsScreenState: MediaDetailsScreenState,
) {
    val title = stringResource(R.string.all_cast)
    val castList = mediaDetailsScreenState.castList
    val mediaName = mediaDetailsScreenState.media?.title ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (castList.isEmpty()) {
            ListShimmerEffect(
                title = title,
                radius = SmallRadius
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(top = SmallRadius.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item(span = { GridItemSpan(2) }) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(vertical = 16.dp, horizontal = 22.dp),
                            textAlign = TextAlign.Center,
                            text = mediaName,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = font,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 22.dp),
                            textAlign = TextAlign.Center,
                            text = title,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = font,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                items(castList.size) { index ->
                    CastMemberItem(
                        castMember = castList[index],
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
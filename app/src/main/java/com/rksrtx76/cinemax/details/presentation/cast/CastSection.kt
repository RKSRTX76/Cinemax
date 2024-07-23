package com.rksrtx76.cinemax.details.presentation.cast

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.domain.model.CastMember
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.Screen


@Composable
fun CastSection(
    isLoading: Boolean,
    castList: List<CastMember>,
    title: String,
    paddingEnd: Dp = 16.dp,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    if (isLoading) {
        ShowCastShimmer(title = title, paddingEnd = paddingEnd, castSize = castList.size, modifier = modifier)
    } else {
        ShowCast(castList = castList.take(12), title = title, paddingEnd = paddingEnd, modifier = modifier, navController = navController)
    }
}

@Composable
fun ShowCastShimmer(
    title: String,
    paddingEnd: Dp,
    castSize: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            fontWeight = FontWeight.Bold,
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = font,
            fontSize = 20.sp
        )

        LazyRow {
            items(castSize) {
                Box(
                    modifier = modifier
                        .padding(end = if (it == castSize - 1) paddingEnd else 16.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                )
            }
        }
    }
}



@Composable
fun ShowCast(
    castList: List<CastMember>,
    title: String,
    paddingEnd: Dp,
    modifier: Modifier = Modifier,
    navController: NavController // Add navController parameter for navigation
) {
    Column(
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            fontWeight = FontWeight.Bold,
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontFamily = font,
            fontSize = 20.sp
        )

        LazyRow {
            items(castList) { castMember ->
                CastMemberItem(
                    castMember = castMember,
                    modifier = modifier.padding(end = 16.dp)
                )
            }

            item {
                Text(
                    text = stringResource(R.string.see_all),
                    fontWeight = FontWeight.Bold,
                    fontFamily = font,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(end = paddingEnd, top = 24.dp)
                        .clickable {
                            navController.navigate("${Screen.CAST_LIST_SCREEN}?castId=${castList.firstOrNull()?.castId}")
                        }
                )
            }
        }
    }
}
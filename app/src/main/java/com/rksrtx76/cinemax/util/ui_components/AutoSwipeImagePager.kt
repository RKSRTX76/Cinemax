package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.main.domain.model.Media
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSwipeImagePager(
    mediaList: List<Media>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        mediaList.size
    }

    val scope = rememberCoroutineScope()
    val swipeIntervalMillis = 3000

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = pagerState,
            key = {
                mediaList[it % mediaList.size].id
            },
            pageSize = PageSize.Fill,
            pageSpacing = 16.dp
        ) { index ->

            val adjustedIndex = index % mediaList.size
            val pageOffset = (pagerState.currentPage - adjustedIndex) + pagerState.currentPageOffsetFraction
            val scale = androidx.compose.ui.util.lerp(0.85f, 1f, 1f - pageOffset.coerceIn(0f, 1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = androidx.compose.ui.util.lerp(0.5f, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                    },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray.copy(alpha = 0.2f))
                ) {
                    Item(
                        media = mediaList[adjustedIndex],
                        navController = navController,
                        modifier = Modifier.fillMaxSize()
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    )
                                )
                            )
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 12.dp,
                                top = 22.dp,
                            )
                            .align(Alignment.BottomStart)
                    )
                }
            }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(swipeIntervalMillis.toLong())
                    scope.launch {
                        val nextPage = (pagerState.currentPage + 1) % mediaList.size
                        pagerState.animateScrollToPage(
                            nextPage,
                            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DotsIndicator(
                totalDots = mediaList.size,
                selectedIndex = pagerState.currentPage
            )

        }
    }
}

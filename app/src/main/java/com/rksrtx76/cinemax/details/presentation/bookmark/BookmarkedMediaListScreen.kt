package com.rksrtx76.cinemax.details.presentation.bookmark

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.presentation.MainUiEvents
import com.rksrtx76.cinemax.main.presentation.MainViewModel
import com.rksrtx76.cinemax.ui.theme.BigRadius
import com.rksrtx76.cinemax.ui.theme.font
import com.rksrtx76.cinemax.util.BottomNav
import com.rksrtx76.cinemax.util.ui_components.MediaItem
import com.rksrtx76.cinemax.util.ui_components.NonFocusedTopBar
import com.rksrtx76.cinemax.util.ui_components.header
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun BookmarkedMediaListScreen(
    selectedItem : MutableState<Int>,
    navController: NavController,
    bottomBarNavController: NavHostController,
    navBackStackEntry: NavBackStackEntry,
    onEvent: (MainUiEvents) -> Unit,
) {

    val viewModel = hiltViewModel<MainViewModel>()
    val mainUiState by viewModel.mainUiState.collectAsState()

    val toolbarHeightPx = with(LocalDensity.current) { BigRadius.dp.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.floatValue + delta
                toolbarOffsetHeightPx.floatValue = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    BackHandler(
        enabled = true
    ) {
        selectedItem.value = 0
        bottomBarNavController.navigate(BottomNav.MEDIA_HOME_SCREEN)
    }

    val type = remember {
        navBackStackEntry.arguments?.getString("type")
    }
    val title = stringResource(id = R.string.bookmarked_media)

    LaunchedEffect(viewModel) {
        viewModel.loadBookmarkedMedia()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection)
    ){
        if (mainUiState.bookmarkedMedia.isEmpty()) {
            Column {
                Text(
                    modifier = Modifier
                        .padding(top = 62.dp, start = 12.dp, end = 8.dp),
                    text = title,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = font,
                    fontSize = 24.sp
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = "Bookmark",
                            tint = Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(100.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No Bookmarks",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = font,
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }else{
            val listState = rememberLazyGridState()

            LazyVerticalGrid(
                state = listState,
                contentPadding = PaddingValues(top = 62.dp, start = 8.dp, end = 8.dp),
                columns = GridCells.Fixed(2)
            ) {
                header {
                    Text(
                        modifier = Modifier
                            .padding(vertical = 14.dp, horizontal = 12.dp),
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = font,
                        fontSize = 24.sp
                    )
                }

                items(mainUiState.bookmarkedMedia.size){ index ->
                    MediaItem(
                        media = mainUiState.bookmarkedMedia[index],
                        navController = navController,
                        mainUiState = mainUiState,
                    )
                    if( index >= mainUiState.bookmarkedMedia.size - 1 && !mainUiState.isLoading){
                        if(type != null){
                            onEvent(MainUiEvents.OnPaginate(type = type))
                        }
                    }
                }
            }
        }
    }
    NonFocusedTopBar(
        toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
        navController = navController
    )
}
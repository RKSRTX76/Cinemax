package com.rksrtx76.cinemax.search.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rksrtx76.cinemax.main.presentation.MainUiState
import com.rksrtx76.cinemax.search.presentation.SearchUiEvents
import com.rksrtx76.cinemax.search.presentation.SearchViewModel
import com.rksrtx76.cinemax.ui.theme.BigRadius
import com.rksrtx76.cinemax.util.Constants
import com.rksrtx76.cinemax.util.ui_components.FocusedTopBar
import com.rksrtx76.cinemax.util.ui_components.MediaItem
import kotlin.math.roundToInt

@Composable
fun SearchScreen(
    navController: NavController,
    mainUiState: MainUiState
) {
    val searchViewModel = hiltViewModel<SearchViewModel>()
    val searchScreenState = searchViewModel.searchScreenState.collectAsState().value

    val toolbarHeightPx = with(LocalDensity.current) {
        BigRadius.dp.roundToPx().toFloat()
    }

    val toolbarOffsetHeightPx = remember {
        mutableFloatStateOf(0f)
    }
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

    val isSearchQueryEmpty = searchScreenState.searchQuery.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .nestedScroll(nestedScrollConnection)
    ) {
        if (isSearchQueryEmpty) {
            // Display recommended screen data when search query is empty
            LazyVerticalGrid(
                contentPadding = PaddingValues(top = BigRadius.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(mainUiState.recommendedAllList.size) { index ->
                    MediaItem(
                        media = mainUiState.recommendedAllList[index],
                        navController = navController,
                        mainUiState = mainUiState
                    )
                    if (index >= mainUiState.recommendedAllList.size - 1 && !mainUiState.isLoading) {
                        searchViewModel.onEvent(SearchUiEvents.OnPaginate(Constants.recommendedListScreen))
                    }
                }
            }
        } else {
            // Display search results when search query is not empty
            LazyVerticalGrid(
                contentPadding = PaddingValues(top = BigRadius.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(searchScreenState.searchList.size) { index ->
                    SearchMediaItem(
                        media = searchScreenState.searchList[index],
                        navController = navController,
                        mainUiState = mainUiState,
                        onEvent = searchViewModel::onEvent
                    )
                    if (index >= searchScreenState.searchList.size - 1 && !mainUiState.isLoading) {
                        searchViewModel.onEvent(SearchUiEvents.OnPaginate(Constants.searchScreen))
                    }
                }
            }
        }

        FocusedTopBar(
            toolbarOffsetHeightPx = toolbarOffsetHeightPx.floatValue.roundToInt(),
            searchScreenState = searchScreenState
        ) {
            searchViewModel.onEvent(SearchUiEvents.OnSearchQueryChanged(it))
        }
    }
}

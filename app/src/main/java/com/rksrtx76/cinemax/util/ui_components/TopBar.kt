package com.rksrtx76.cinemax.util.ui_components

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.search.presentation.SearchScreenState
import com.rksrtx76.cinemax.search.presentation.search.SearchBar
import com.rksrtx76.cinemax.ui.theme.BigRadius
import com.rksrtx76.cinemax.util.Screen

@Composable
fun NonFocusedTopBar(
    toolbarOffsetHeightPx: Int,
    navController: NavController,
) {
    val context = LocalContext.current
    val statusBarColor = (context as Activity).getStatusBarColor()
    Box(
        modifier = Modifier
            .background(statusBarColor.copy(alpha = 0.7f))
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(R.drawable.cinemax),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(100.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(36.dp)
                    .clickable {
                        navController.navigate(Screen.SEARCH_SCREEN)
                    }
            )
        }
    }
}

@Composable
fun FocusedTopBar(
    toolbarOffsetHeightPx: Int,
    searchScreenState: SearchScreenState,
    onSearch: (String) -> Unit = {}
) {

    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .height(BigRadius.dp)
            .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx) }
    ) {
        SearchBar(
            leadingIcon = {
                Icon(
                    Icons.Rounded.Search,
                    null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                )
            },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .height(50.dp),
            placeholderText = stringResource(R.string.movies_shows_and_more),
            searchScreenState = searchScreenState
        ) {
            onSearch(it)
        }
    }
}
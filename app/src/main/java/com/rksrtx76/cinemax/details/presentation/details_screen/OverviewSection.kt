package com.rksrtx76.cinemax.details.presentation.details_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.main.domain.model.Media
import com.rksrtx76.cinemax.ui.theme.font

@Composable
fun OverviewSection(
    media: Media
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column {
        Text(
            modifier = Modifier.padding(horizontal = 22.dp),
            text = stringResource(R.string.overview),
            fontFamily = font,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 22.dp)
                .clickable { isExpanded = !isExpanded },
            text = media.overview,
            fontFamily = font,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 16.sp,
            maxLines = if (isExpanded) Int.MAX_VALUE else 2,
            overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis
        )

        if (!isExpanded) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 22.dp)
                    .clickable { isExpanded = true },
                text = stringResource(R.string.see_more),
                fontFamily = font,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
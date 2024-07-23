package com.rksrtx76.cinemax.util.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rksrtx76.cinemax.ui.theme.Radius
import com.rksrtx76.cinemax.ui.theme.font

@Composable
fun ShowHomeShimmer(
    title : String,
    paddingEnd : Dp,
    modifier: Modifier = Modifier
){

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
            items(10){
                Box(
                    modifier = modifier
                        .padding(end = if(it == 9) paddingEnd else 0.dp)
                        .clip(RoundedCornerShape(Radius.dp))
                        .shimmerEffect(false)
                )
            }
        }
    }
}
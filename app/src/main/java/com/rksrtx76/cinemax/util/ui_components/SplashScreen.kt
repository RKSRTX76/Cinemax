package com.rksrtx76.cinemax.util.ui_components

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rksrtx76.cinemax.R
import com.rksrtx76.cinemax.util.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController : NavHostController
) {
    var animateLogo by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val statusBarColor = (context as Activity).getStatusBarColor()


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(statusBarColor)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {

            LaunchedEffect(Unit) {
                delay(2000)
                animateLogo = true
                delay(2000)
                navController.navigate(Screen.MAIN_SCREEN) {
                    popUpTo(Screen.SPLASH_SCREEN) { inclusive = true }
                }
            }
            LottieLoader(
                modifier = Modifier.size(570.dp),
                lottieFile = R.raw.bubble
            )

            this@Column.AnimatedVisibility(
                visible = animateLogo.not(),
                exit = fadeOut(
                    animationSpec = tween(durationMillis = 2000)
                ) + scaleOut(animationSpec = tween(durationMillis = 1000)),
            ) {
                Image(
                    modifier = Modifier
                        .size(240.dp)
                        .alpha(0.78F),
                    painter = painterResource(id = R.drawable.cinemax),
                    contentDescription = null
                )
            }
            if (animateLogo) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(70.dp)
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 30.dp),
                    color = Color.Red,
                )
            }
        }

    }
}
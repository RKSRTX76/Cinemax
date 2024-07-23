package com.rksrtx76.cinemax.util.ui_components

import android.app.Activity
import androidx.compose.ui.graphics.Color

fun Activity.getStatusBarColor(): Color {
    val window = this.window
    val statusBarColor = window.statusBarColor
    return Color(statusBarColor)
}
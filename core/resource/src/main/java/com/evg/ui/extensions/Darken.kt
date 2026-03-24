package com.evg.ui.extensions

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float): Color {
    return Color(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha
    )
}
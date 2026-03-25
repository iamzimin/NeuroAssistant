package com.evg.ui.extensions

import androidx.compose.ui.graphics.Color

fun Color.lighten(factor: Float): Color {
    return Color(
        red = red + (1f - red) * factor,
        green = green + (1f - green) * factor,
        blue = blue + (1f - blue) * factor,
        alpha = alpha
    )
}
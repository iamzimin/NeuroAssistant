package com.evg.ui.theme

import androidx.compose.ui.graphics.Color

data class AppPalette(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val shimmer: Color,

    val text: Color,

    // SnackBar
    val snackBarBackground: Color,

    // TextField
    val textFieldPlaceholder: Color,
    val textFieldTitle: Color,

    val tileBackground: Color,
)

enum class AppStyle {
    Blue,
}

val baseDarkPalette = AppPalette(
    primary = Color(0xFF91A8D0),
    secondary = Color(0xFF375E97),
    background = Color(0xFF121C2B),
    shimmer = Color.LightGray,

    text = Color(0xFFFFFFFF),

    // SnackBar
    snackBarBackground = Color(0xFF373737),

    // TextField
    textFieldPlaceholder = Color(0xFF4A6FA5),
    textFieldTitle = Color(0xFFAAAAAA),
    tileBackground = Color(0xFF1A2940),
)

val baseLightPalette = AppPalette(
    primary = Color(0xFF91A8D0),
    secondary = Color(0xFFA6C1EC),
    background = Color(0xFFFFFFFF),
    shimmer = Color.Gray,

    text = Color(0xFF000000),

    // SnackBar
    snackBarBackground = Color(0xFFCDCDCD),

    // TextField
    textFieldPlaceholder = Color(0xFF4A6FA5),
    textFieldTitle = Color(0xFF666666),
    tileBackground = Color(0xFFDFE8F5),
)

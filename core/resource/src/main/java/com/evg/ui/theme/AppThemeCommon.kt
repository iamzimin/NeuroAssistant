package com.evg.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.appcompat.app.AppCompatDelegate


object AppTheme {
    var nightMode by mutableIntStateOf(AppCompatDelegate.MODE_NIGHT_YES)

    val colors: AppPalette
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current

    val dimens: AppDimens
        @Composable
        get() = LocalAppDimens.current

    val isDarkTheme: Boolean
        @Composable
        get() = LocalAppIsDarkTheme.current
}


val LocalAppColors = staticCompositionLocalOf<AppPalette> {
    error("Colors composition error")
}

val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("Typography composition error")
}

val LocalAppIsDarkTheme = staticCompositionLocalOf {
    true
}

val LocalAppDimens = staticCompositionLocalOf {
    AppDimens()
}
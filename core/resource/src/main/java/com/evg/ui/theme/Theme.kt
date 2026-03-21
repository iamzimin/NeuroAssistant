package com.evg.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun NeuroAssistantTheme(
    style: AppStyle = AppStyle.Blue,
    textSize: AppSize = AppSize.Medium,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = when (darkTheme) {
        true -> {
            when (style) {
                AppStyle.Blue -> baseDarkPalette
            }
        }
        false -> {
            when (style) {
                AppStyle.Blue -> baseLightPalette
            }
        }
    }

    val typography = when(textSize) {
        AppSize.Medium -> mediumTextSize
    }

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        content = content,
    )
}
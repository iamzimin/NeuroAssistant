package com.evg.ui.theme

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect

@Composable
fun NeuroAssistantTheme(
    style: AppStyle = AppStyle.Blue,
    textSize: AppSize = AppSize.Medium,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    LaunchedEffect(AppTheme.nightMode) {
        AppCompatDelegate.setDefaultNightMode(AppTheme.nightMode)
    }

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
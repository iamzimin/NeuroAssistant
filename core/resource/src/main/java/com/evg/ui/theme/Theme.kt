package com.evg.ui.theme

import android.app.Activity
import android.content.ContextWrapper
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val activity = generateSequence(view.context) { context ->
                (context as? ContextWrapper)?.baseContext
            }.filterIsInstance<Activity>()
                .firstOrNull()

            activity?.window?.let { window ->
                WindowCompat.setDecorFitsSystemWindows(window, false)

                WindowCompat.getInsetsController(window, view).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalAppIsDarkTheme provides darkTheme,
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
        content = content,
    )
}
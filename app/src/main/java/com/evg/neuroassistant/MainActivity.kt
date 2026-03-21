package com.evg.neuroassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.evg.ui.theme.AppSize
import com.evg.ui.theme.AppStyle
import com.evg.ui.theme.NeuroAssistantTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val currentStyle = remember { mutableStateOf(AppStyle.Blue) }
            val currentFontSize = remember { mutableStateOf(AppSize.Medium) }

            NeuroAssistantTheme(
                style = currentStyle.value,
                textSize = currentFontSize.value,
            ) {
                MainScreen()
            }
        }
    }
}
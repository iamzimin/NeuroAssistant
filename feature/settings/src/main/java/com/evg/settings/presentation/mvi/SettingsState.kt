package com.evg.settings.presentation.mvi

data class SettingsState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
package com.evg.settings.presentation.mvi

sealed class SettingsSideEffect {
    data class FirstClass(val paramOne: String) : SettingsSideEffect()
    data object FirstObject : SettingsSideEffect()
}
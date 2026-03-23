package com.evg.settings.presentation.mvi

sealed class SettingsAction {
    data class FirstClass(val paramOne: String) : SettingsAction()
    data object SecondObject : SettingsAction()
}
package com.evg.settings.presentation.mvi

sealed class SettingsAction {
    data object Initialize : SettingsAction()
    data object OnEditClicked : SettingsAction()
    data object OnCancelEditClicked : SettingsAction()
    data class OnDisplayNameChanged(val value: String) : SettingsAction()
    data class OnPhotoSelected(
        val bytes: ByteArray,
        val fileExtension: String?,
    ) : SettingsAction()
    data object OnSaveProfileClicked : SettingsAction()
    data object OnRetryBalanceClicked : SettingsAction()
    data object OnRetryProfileUpdateClicked : SettingsAction()
    data object OnLogoutClicked : SettingsAction()
}
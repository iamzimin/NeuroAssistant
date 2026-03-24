package com.evg.settings.presentation.mvi

import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.GigaChatError

sealed class SettingsSideEffect {
    data class ShowBalanceError(val error: GigaChatError) : SettingsSideEffect()
    data class ShowProfileUpdateError(val error: FirebaseError) : SettingsSideEffect()
    data object NavigateToAuth : SettingsSideEffect()
}
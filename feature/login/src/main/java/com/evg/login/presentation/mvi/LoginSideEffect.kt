package com.evg.login.presentation.mvi

import com.evg.api.domain.utils.FirebaseError

sealed class LoginSideEffect {
    data object NavigateToHome : LoginSideEffect()
    data object LaunchGoogleSignIn : LoginSideEffect()
    data class ShowError(val error: FirebaseError) : LoginSideEffect()
}
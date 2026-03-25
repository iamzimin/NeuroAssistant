package com.evg.login.presentation.mvi

import android.util.Patterns

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginMode: Boolean = true,
) {
    val isEmailValid: Boolean
        get() = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val isPasswordValid: Boolean
        get() = password.length >= 6

    val canSubmit: Boolean
        get() = isEmailValid && isPasswordValid && !isLoading
}

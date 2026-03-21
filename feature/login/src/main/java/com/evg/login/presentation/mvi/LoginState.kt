package com.evg.login.presentation.mvi

import android.util.Patterns

data class LoginState(
    val email: String = "test@mail.com",
    val password: String = "qweqweqwe123",
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

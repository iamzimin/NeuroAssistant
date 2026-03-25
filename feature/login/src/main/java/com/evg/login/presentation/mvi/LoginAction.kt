package com.evg.login.presentation.mvi

sealed class LoginAction {
    data class OnEmailChanged(val email: String) : LoginAction()
    data class OnPasswordChanged(val password: String) : LoginAction()
    data object OnLoginClicked : LoginAction()
    data object OnRegisterClicked : LoginAction()
    data object OnGoogleSignInClicked : LoginAction()
    data class OnGoogleIdTokenReceived(val idToken: String) : LoginAction()
    data object ToggleLoginMode : LoginAction()
}

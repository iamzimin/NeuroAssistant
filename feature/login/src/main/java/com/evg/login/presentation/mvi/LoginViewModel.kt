package com.evg.login.presentation.mvi

import androidx.lifecycle.ViewModel
import com.evg.api.domain.utils.ServerResult
import com.evg.login.domain.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ContainerHost<LoginState, LoginSideEffect>, ViewModel() {

    override val container = container<LoginState, LoginSideEffect>(LoginState())

    fun dispatch(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChanged -> {
                intent {
                    reduce { state.copy(email = action.email) }
                }
            }
            is LoginAction.OnPasswordChanged -> {
                intent {
                    reduce { state.copy(password = action.password) }
                }
            }
            is LoginAction.OnLoginClicked -> {
                login()
            }
            is LoginAction.OnRegisterClicked -> {
                register()
            }
            is LoginAction.ToggleLoginMode -> {
                intent {
                    reduce { state.copy(isLoginMode = !state.isLoginMode) }
                }
            }
        }
    }

    private fun login() = intent {
        reduce { state.copy(isLoading = true) }
        when (val response = repository.login(state.email, state.password)) {
            is ServerResult.Success -> {
                postSideEffect(LoginSideEffect.NavigateToHome)
            }
            is ServerResult.Error -> {
                postSideEffect(LoginSideEffect.ShowError(response.error))
            }
        }
        reduce { state.copy(isLoading = false) }
    }

    private fun register() = intent {
        reduce { state.copy(isLoading = true) }
        when (val response = repository.register(state.email, state.password)) {
            is ServerResult.Success -> {
                postSideEffect(LoginSideEffect.NavigateToHome)
            }
            is ServerResult.Error -> {
                postSideEffect(LoginSideEffect.ShowError(response.error))
            }
        }
        reduce { state.copy(isLoading = false) }
    }
}

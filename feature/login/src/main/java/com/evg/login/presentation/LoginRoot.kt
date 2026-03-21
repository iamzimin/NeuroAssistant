package com.evg.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.evg.login.presentation.mvi.LoginSideEffect
import com.evg.login.presentation.mvi.LoginViewModel
import com.evg.ui.mapper.toErrorMessage
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.snackbar.SnackBarEvent
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginRoot(
    modifier: Modifier,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.NavigateToHome -> onNavigateToHome()
            is LoginSideEffect.ShowError -> {
                SnackBarController.sendEvent(SnackBarEvent(sideEffect.error.toErrorMessage(context)))
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        dispatch = viewModel::dispatch,
    )
}
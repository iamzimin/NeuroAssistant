package com.evg.login.presentation

import android.app.Activity
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.evg.login.presentation.mvi.LoginAction
import com.evg.login.presentation.mvi.LoginSideEffect
import com.evg.login.presentation.mvi.LoginViewModel
import com.evg.resource.R
import com.evg.ui.mapper.toErrorMessage
import com.evg.ui.snackbar.SnackBarAction
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.snackbar.SnackBarEvent
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun LoginRoot(
    modifier: Modifier,
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.collectAsState().value

    val credentialManager = remember(context.applicationContext) {
        CredentialManager.create(context.applicationContext)
    }
    val googleSignInRequest = remember(context.applicationContext) {
        createGoogleSignInRequest(context.applicationContext)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is LoginSideEffect.NavigateToHome -> onNavigateToHome()
            is LoginSideEffect.LaunchGoogleSignIn -> {
                if (googleSignInRequest == null) {
                    SnackBarController.sendEvent(
                        SnackBarEvent(message = context.getString(R.string.google_sign_in_not_configured))
                    )
                } else {
                    val activity = generateSequence(context) {
                        (it as? ContextWrapper)?.baseContext
                    }.filterIsInstance<Activity>()
                        .firstOrNull()

                    if (activity == null) {
                        SnackBarController.sendEvent(
                            SnackBarEvent(message = context.getString(R.string.google_sign_in_failed))
                        )
                    } else {
                        scope.launch {
                            try {
                                val response = credentialManager.getCredential(
                                    context = activity,
                                    request = googleSignInRequest,
                                )
                                val idToken = response.credential.extractGoogleIdTokenOrNull()

                                if (idToken.isNullOrBlank()) {
                                    SnackBarController.sendEvent(
                                        SnackBarEvent(message = context.getString(R.string.google_sign_in_failed))
                                    )
                                } else {
                                    viewModel.dispatch(LoginAction.OnGoogleIdTokenReceived(idToken))
                                }
                            } catch (_: GetCredentialCancellationException) {
                                //
                            } catch (_: GetCredentialException) {
                                SnackBarController.sendEvent(
                                    SnackBarEvent(message = context.getString(R.string.google_sign_in_failed))
                                )
                            }
                        }
                    }
                }
            }
            is LoginSideEffect.ShowError -> {
                SnackBarController.sendEvent(
                    SnackBarEvent(
                        message = sideEffect.error.toErrorMessage(context),
                        action = SnackBarAction(
                            name = context.getString(R.string.retry),
                            action = {
                                if (state.isLoginMode) {
                                    viewModel.dispatch(LoginAction.OnLoginClicked)
                                } else {
                                    viewModel.dispatch(LoginAction.OnRegisterClicked)
                                }
                            },
                        ),
                    )
                )
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        state = state,
        dispatch = viewModel::dispatch,
    )
}
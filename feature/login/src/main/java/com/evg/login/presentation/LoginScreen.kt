package com.evg.login.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.login.presentation.mvi.LoginAction
import com.evg.login.presentation.mvi.LoginState
import com.evg.resource.R
import com.evg.ui.custom.AuthorizationTextField
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: LoginState,
    dispatch: (LoginAction) -> Unit,
) {
    val authorization = stringResource(R.string.authorization)
    val registration = stringResource(R.string.registration)
    val loginAccountProgressText = stringResource(R.string.login_account_progress)
    val createAccountProgressText = stringResource(R.string.create_account_progress)
    val emailLabel = stringResource(R.string.email)
    val enterEmailPlaceholder = stringResource(R.string.enter_email)
    val passwordLabel = stringResource(R.string.password)
    val enterPasswordPlaceholder = stringResource(R.string.enter_password)
    val iDontHaveAccountText = stringResource(R.string.i_dont_have_account)
    val iAlreadyHaveAccountText = stringResource(R.string.i_already_have_account)
    val logInText = stringResource(R.string.log_in)
    val signUpText = stringResource(R.string.sign_up)
    val googleSignInText = stringResource(R.string.google_sign_in)

    var hasPlayedEntryAnimation by rememberSaveable { mutableStateOf(false) }
    val formTransitionState = remember {
        MutableTransitionState(false)
    }
    LaunchedEffect(Unit) {
        if (!hasPlayedEntryAnimation) delay(1600)
        formTransitionState.targetState = true
    }
    LaunchedEffect(formTransitionState.isIdle, formTransitionState.currentState) {
        if (!hasPlayedEntryAnimation && formTransitionState.isIdle && formTransitionState.currentState) {
            hasPlayedEntryAnimation = true
        }
    }

    AnimatedVisibility(
        modifier = modifier
            .fillMaxSize(),
        visibleState = formTransitionState,
        enter = if (hasPlayedEntryAnimation) {
            EnterTransition.None
        } else {
            fadeIn(animationSpec = tween(durationMillis = 1500)) +
                slideInVertically(
                    animationSpec = tween(durationMillis = 1500),
                    initialOffsetY = { it / 3 },
                )
        },
        exit = ExitTransition.None,
    ) {
        Column(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimens.horizontalPadding,
                    vertical = AppTheme.dimens.verticalPadding,
                )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (state.isLoginMode) authorization else registration,
                style = AppTheme.typography.heading,
                color = AppTheme.colors.primary,
            )

            Text(
                text = if (state.isLoginMode) loginAccountProgressText else createAccountProgressText,
                style = AppTheme.typography.body,
                color = AppTheme.colors.text.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingLarge))

            AuthorizationTextField(
                filedName = emailLabel,
                placeholder = enterEmailPlaceholder,
                value = state.email,
                isError = !state.isEmailValid,
                onValueChange = { newText -> dispatch(LoginAction.OnEmailChanged(newText)) }
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

            AuthorizationTextField(
                filedName = passwordLabel,
                placeholder = enterPasswordPlaceholder,
                value = state.password,
                isError = !state.isPasswordValid,
                onValueChange = { newText -> dispatch(LoginAction.OnPasswordChanged(newText)) },
                isPassword = true,
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingLarge))

            Button(
                onClick = {
                    if (state.isLoginMode) {
                        dispatch(LoginAction.OnLoginClicked)
                    } else {
                        dispatch(LoginAction.OnRegisterClicked)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.canSubmit,
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.colors.primary,
                    contentColor = AppTheme.colors.background,
                    disabledContainerColor = AppTheme.colors.primary.copy(alpha = 0.5f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = AppTheme.colors.background,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (state.isLoginMode) logInText else signUpText,
                        style = AppTheme.typography.body
                    )
                }
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))

            TextButton(
                onClick = { dispatch(LoginAction.ToggleLoginMode) }
            ) {
                Text(
                    text = if (state.isLoginMode) iDontHaveAccountText else iAlreadyHaveAccountText,
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.body
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

            SocialLoginButton(
                icon = painterResource(id = R.drawable.google),
                contentDescription = googleSignInText,
                enabled = !state.isLoading,
                onClick = { dispatch(LoginAction.OnGoogleSignInClicked) },
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoginScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            LoginScreen(
                state = LoginState(),
                dispatch = {},
            )
        }
    }
}
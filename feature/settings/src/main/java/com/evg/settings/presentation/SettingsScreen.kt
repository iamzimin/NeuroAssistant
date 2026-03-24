package com.evg.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.resource.R
import com.evg.settings.presentation.cards.ProfileHeaderCard
import com.evg.settings.presentation.cards.ProfileInfoCard
import com.evg.settings.presentation.cards.ThemeCard
import com.evg.settings.presentation.mvi.SettingsAction
import com.evg.settings.presentation.mvi.SettingsState
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsState,
    dispatch: (SettingsAction) -> Unit,
    onChangePhotoClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimens.horizontalPadding,
                vertical = AppTheme.dimens.verticalPadding,
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
    ) {
        ProfileHeaderCard(
            state = state,
            onChangePhotoClick = onChangePhotoClick,
        )

        ProfileInfoCard(
            state = state,
            dispatch = dispatch,
        )

        ThemeCard(
            selectedThemeMode = AppTheme.nightMode,
            onThemeSelected = { mode ->
                AppTheme.nightMode = mode
            },
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = AppTheme.dimens.paddingLarge),
            onClick = { dispatch(SettingsAction.OnLogoutClicked) },
            enabled = !state.isSigningOut,
            shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
            ),
        ) {
            if (state.isSigningOut) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = MaterialTheme.colorScheme.onError,
                    strokeWidth = 2.dp,
                )
            } else {
                Text(text = stringResource(R.string.profile_logout))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SettingsScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            SettingsScreen(
                state = SettingsState(
                    gigaChatTokens = "123456",
                ),
                dispatch = {},
                onChangePhotoClick = {},
            )
        }
    }
}
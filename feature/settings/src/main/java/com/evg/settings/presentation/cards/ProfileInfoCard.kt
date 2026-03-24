package com.evg.settings.presentation.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.resource.R
import com.evg.settings.presentation.mvi.SettingsAction
import com.evg.settings.presentation.mvi.SettingsState
import com.evg.ui.custom.DefaultTextField
import com.evg.ui.extensions.darken
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ProfileInfoCard(
    state: SettingsState,
    dispatch: (SettingsAction) -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
        color = AppTheme.colors.tileBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.profile_info_title),
                    style = AppTheme.typography.body.copy(fontWeight = FontWeight.Medium),
                    color = AppTheme.colors.text,
                )

                if (state.isEditing) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = AppTheme.colors.tileBackground.darken(0.8f)
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
                        onClick = { dispatch(SettingsAction.OnCancelEditClicked) }
                    ) {
                        Text(
                            text = stringResource(R.string.profile_cancel),
                            color = AppTheme.colors.text,
                        )
                    }
                } else {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = AppTheme.colors.tileBackground.darken(0.8f)
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
                        onClick = { dispatch(SettingsAction.OnEditClicked) }) {
                        Text(
                            text = stringResource(R.string.profile_edit),
                            color = AppTheme.colors.text,
                        )
                    }
                }
            }

            if (state.isEditing) {
                DefaultTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.editedDisplayName,
                    onValueChange = { dispatch(SettingsAction.OnDisplayNameChanged(it)) },
                    label = stringResource(R.string.profile_name),
                )
            } else {
                InfoRow(
                    label = stringResource(R.string.profile_name),
                    value = state.profile?.displayName?.ifBlank { "-" } ?: "-",
                )
            }

            HorizontalDivider(color = AppTheme.colors.background)

            InfoRow(
                label = stringResource(R.string.email),
                value = state.profile?.email?.ifBlank { "-" } ?: "-",
            )

            HorizontalDivider(color = AppTheme.colors.background)

            InfoRow(
                label = stringResource(R.string.profile_phone),
                value = state.profile?.phoneNumber ?: "-",
            )

            HorizontalDivider(color = AppTheme.colors.background)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.profile_gigachat_tokens),
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.text.copy(alpha = 0.8f),
                )

                if (state.isBalanceLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = AppTheme.colors.primary,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = state.gigaChatTokens,
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.text,
                    )
                }
            }

            if (state.isEditing) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { dispatch(SettingsAction.OnSaveProfileClicked) },
                    enabled = state.canSave,
                    shape = RoundedCornerShape(AppTheme.dimens.borderRadius),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppTheme.colors.primary,
                        contentColor = AppTheme.colors.background,
                    ),
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = AppTheme.colors.background,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text(text = stringResource(R.string.profile_save))
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ProfileInfoCardPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ProfileInfoCard(
                state = SettingsState(
                    gigaChatTokens = "123456",
                    isEditing = true,
                ),
                dispatch = {},
            )
        }
    }
}
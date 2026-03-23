package com.evg.settings.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.evg.settings.presentation.mvi.SettingsState
import com.evg.settings.presentation.mvi.SettingsAction
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsState,
    dispatch: (SettingsAction) -> Unit,
) {

}

@Preview
@Composable
private fun SettingsScreenPreview() {
    NeuroAssistantTheme {
        SettingsScreen(
            state = SettingsState(),
            dispatch = {},
        )
    }
}
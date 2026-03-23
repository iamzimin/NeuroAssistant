package com.evg.settings.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Modifier
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.evg.settings.presentation.mvi.SettingsSideEffect
import com.evg.settings.presentation.mvi.SettingsViewModel

@Composable
fun SettingsRoot(
    modifier: Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    /*viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SettingsSideEffect.FirstClass -> {
                when (sideEffect.paramOne) {
                    "" -> {}
                }
            }
            SettingsSideEffect.FirstObject -> {}
        }
    }*/

    SettingsScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        dispatch = viewModel::dispatch,
    )
}
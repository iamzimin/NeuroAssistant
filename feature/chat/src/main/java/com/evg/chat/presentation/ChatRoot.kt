package com.evg.chat.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.ui.Modifier
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.evg.chat.presentation.mvi.ChatSideEffect
import com.evg.chat.presentation.mvi.ChatViewModel

@Composable
fun ChatRoot(
    modifier: Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    /*viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ChatSideEffect.FirstClass -> {
                when (sideEffect.paramOne) {
                    "" -> {}
                }
            }
            ChatSideEffect.FirstObject -> {}
        }
    }*/

    ChatScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        dispatch = viewModel::dispatch,
    )
}
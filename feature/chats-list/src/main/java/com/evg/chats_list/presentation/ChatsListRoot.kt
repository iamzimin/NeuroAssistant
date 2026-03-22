package com.evg.chats_list.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState
import com.evg.chats_list.presentation.mvi.ChatsListViewModel

@Composable
fun ChatsListRoot(
    modifier: Modifier,
    viewModel: ChatsListViewModel = hiltViewModel(),
) {
    /*viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ChatsListSideEffect.FirstClass -> {
                when (sideEffect.paramOne) {
                    "" -> {}
                }
            }
            ChatsListSideEffect.FirstObject -> {}
        }
    }*/

    ChatsListScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        dispatch = viewModel::dispatch,
    )
}
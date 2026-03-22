package com.evg.chats_list.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.evg.chats_list.presentation.mvi.ChatsListSideEffect
import com.evg.chats_list.presentation.mvi.ChatsListViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChatsListRoot(
    modifier: Modifier,
    onNavigateToChat: (Long) -> Unit,
    viewModel: ChatsListViewModel = hiltViewModel(),
) {
    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ChatsListSideEffect.NavigateToChat -> onNavigateToChat(sideEffect.chatId)
        }
    }

    ChatsListScreen(
        modifier = modifier,
        state = viewModel.collectAsState().value,
        chats = viewModel.chats.collectAsLazyPagingItems(),
        dispatch = viewModel::dispatch,
    )
}
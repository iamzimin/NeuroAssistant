package com.evg.chats_list.presentation.mvi

sealed class ChatsListAction {
    data class OnSearchQueryChanged(val query: String) : ChatsListAction()
    data object OnSearchClicked : ChatsListAction()
    data class OnCreateChatClicked(val title: String) : ChatsListAction()
    data class OnChatClicked(val chatId: Long) : ChatsListAction()
}
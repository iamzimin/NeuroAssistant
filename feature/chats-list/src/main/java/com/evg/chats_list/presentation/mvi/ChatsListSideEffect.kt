package com.evg.chats_list.presentation.mvi

sealed class ChatsListSideEffect {
    data class NavigateToChat(val chatId: Long) : ChatsListSideEffect()
}
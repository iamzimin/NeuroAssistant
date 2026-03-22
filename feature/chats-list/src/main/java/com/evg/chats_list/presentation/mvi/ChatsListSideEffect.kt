package com.evg.chats_list.presentation.mvi

sealed class ChatsListSideEffect {
    data class FirstClass(val paramOne: String) : ChatsListSideEffect()
    data object FirstObject : ChatsListSideEffect()
}
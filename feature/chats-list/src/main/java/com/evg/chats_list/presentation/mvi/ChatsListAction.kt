package com.evg.chats_list.presentation.mvi

sealed class ChatsListAction {
    data class FirstClass(val paramOne: String) : ChatsListAction()
    data object SecondObject : ChatsListAction()
}
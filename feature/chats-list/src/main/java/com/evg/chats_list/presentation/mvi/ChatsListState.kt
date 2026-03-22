package com.evg.chats_list.presentation.mvi

data class ChatsListState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
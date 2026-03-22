package com.evg.chats_list.presentation.mvi

data class ChatsListState(
    val searchQuery: String = "",
    val appliedQuery: String? = null,
    val isCreatingChat: Boolean = false,
)
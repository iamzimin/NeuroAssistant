package com.evg.chat.presentation.mvi

import com.evg.chat.domain.model.ChatMessage

data class ChatState(
    val chatId: Long? = null,
    val title: String = "",
    val input: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isRequestInProgress: Boolean = false,
)

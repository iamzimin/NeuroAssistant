package com.evg.chat.presentation.mvi

data class ChatState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
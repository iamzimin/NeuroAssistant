package com.evg.chat.presentation.mvi

sealed class ChatSideEffect {
    data class FirstClass(val paramOne: String) : ChatSideEffect()
    data object FirstObject : ChatSideEffect()
}
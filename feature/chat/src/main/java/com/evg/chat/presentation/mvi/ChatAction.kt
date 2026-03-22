package com.evg.chat.presentation.mvi

sealed class ChatAction {
    data class FirstClass(val paramOne: String) : ChatAction()
    data object SecondObject : ChatAction()
}
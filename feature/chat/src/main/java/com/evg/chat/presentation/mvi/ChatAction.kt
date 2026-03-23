package com.evg.chat.presentation.mvi

sealed class ChatAction {
    data class Initialize(val chatId: Long) : ChatAction()
    data class OnMessageChanged(val value: String) : ChatAction()
    data object OnSendClicked : ChatAction()
    data object OnClearInputClicked : ChatAction()
    data class OnRetryClicked(val assistantMessageId: Long) : ChatAction()
    data class OnAssistantMessageLongClicked(val assistantMessageId: Long) : ChatAction()
}

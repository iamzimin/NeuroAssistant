package com.evg.chat.presentation.mvi

import com.evg.api.domain.utils.GigaChatError

sealed class ChatSideEffect {
    data class ShareMessage(val text: String) : ChatSideEffect()
    data class ShowError(val error: GigaChatError) : ChatSideEffect()
}

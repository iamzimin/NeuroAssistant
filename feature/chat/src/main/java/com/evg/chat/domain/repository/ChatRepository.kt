package com.evg.chat.domain.repository

import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import com.evg.chat.domain.model.ChatConversation
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeChat(chatId: Long): Flow<ChatConversation?>
    suspend fun sendMessage(chatId: Long, text: String): ServerResult<Unit, GigaChatError>
    suspend fun retryAssistantMessage(
        chatId: Long,
        assistantMessageId: Long,
    ): ServerResult<Unit, GigaChatError>
}
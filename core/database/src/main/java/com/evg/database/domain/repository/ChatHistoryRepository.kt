package com.evg.database.domain.repository

import com.evg.database.domain.model.ChatMessageDBO
import kotlinx.coroutines.flow.Flow

interface ChatHistoryRepository {
    fun observeMessages(chatId: Long): Flow<List<ChatMessageDBO>>
    suspend fun getMessages(chatId: Long): List<ChatMessageDBO>
    suspend fun getMessage(messageId: Long): ChatMessageDBO?
    suspend fun insertMessage(message: ChatMessageDBO): Long
    suspend fun updateMessage(message: ChatMessageDBO)
}
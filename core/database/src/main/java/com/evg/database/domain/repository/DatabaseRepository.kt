package com.evg.database.domain.repository

import androidx.paging.PagingData
import com.evg.database.domain.model.ChatDBO
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getChats(query: String? = null): Flow<PagingData<ChatDBO>>
    fun observeChat(chatId: Long): Flow<ChatDBO?>
    suspend fun createChat(title: String): Long
    suspend fun updateChatTitle(chatId: Long, title: String)
    suspend fun clearAll()
}
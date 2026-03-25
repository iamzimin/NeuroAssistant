package com.evg.database.data.repository

import com.evg.database.data.storage.ChatMessageDao
import com.evg.database.domain.model.ChatMessageDBO
import com.evg.database.domain.repository.ChatHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ChatHistoryRepositoryImpl @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
) : ChatHistoryRepository {
    override fun observeMessages(chatId: Long): Flow<List<ChatMessageDBO>> {
        return chatMessageDao.observeMessages(chatId)
    }

    override suspend fun getMessages(chatId: Long): List<ChatMessageDBO> {
        return chatMessageDao.getMessages(chatId)
    }

    override suspend fun getMessage(messageId: Long): ChatMessageDBO? {
        return chatMessageDao.getMessage(messageId)
    }

    override suspend fun insertMessage(message: ChatMessageDBO): Long {
        return chatMessageDao.insertMessage(message)
    }

    override suspend fun updateMessage(message: ChatMessageDBO) {
        chatMessageDao.updateMessage(message)
    }
}

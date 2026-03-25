package com.evg.database.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evg.database.data.storage.ChatDao
import com.evg.database.data.storage.ChatMessageDao
import com.evg.database.domain.model.ChatDBO
import com.evg.database.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val chatDao: ChatDao,
    private val chatMessageDao: ChatMessageDao,
) : DatabaseRepository {
    private companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }

    override fun getChats(query: String?): Flow<PagingData<ChatDBO>> {
        val normalizedQuery = query?.trim().orEmpty()

        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                if (normalizedQuery.isBlank()) {
                    chatDao.getChatsPagingSource()
                } else {
                    chatDao.searchChatsPagingSource(normalizedQuery)
                }
            },
        ).flow
    }

    override fun observeChat(chatId: Long): Flow<ChatDBO?> {
        return chatDao.observeChat(chatId)
    }

    override suspend fun createChat(title: String): Long {
        val normalizedTitle = title.trim()
        val chatId = chatDao.insertChat(
            ChatDBO(
                title = normalizedTitle,
                createdAt = System.currentTimeMillis(),
            )
        )
        chatDao.updateChatTitle(chatId = chatId, title = normalizedTitle)
        return chatId
    }

    override suspend fun updateChatTitle(chatId: Long, title: String) {
        val normalizedTitle = title.trim()
        chatDao.updateChatTitle(chatId = chatId, title = normalizedTitle)
    }

    override suspend fun clearAll() {
        chatMessageDao.clearAll()
        chatDao.clearAll()
    }
}

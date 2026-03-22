package com.evg.database.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evg.database.data.storage.ChatDao
import com.evg.database.domain.model.ChatDBO
import com.evg.database.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

class DatabaseRepositoryImpl(
    private val chatDao: ChatDao,
) : DatabaseRepository {
    private companion object {
        const val DEFAULT_CHAT_TITLE = "New chat"
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

    override suspend fun createChat(title: String): Long {
        val normalizedTitle = title.trim().ifBlank { DEFAULT_CHAT_TITLE }

        return chatDao.insertChat(
            ChatDBO(
                title = normalizedTitle,
                createdAt = System.currentTimeMillis(),
            )
        )
    }
}

package com.evg.database.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evg.database.data.storage.ChatDao
import com.evg.database.data.storage.ChatMessageDao
import com.evg.database.domain.model.ChatDBO
import com.evg.database.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow

/**
 * Репозиторий для работы с базой данных чатов и сообщений
 *
 * @property chatDao DAO для работы с чатами
 * @property chatMessageDao DAO для работы с сообщениями чатов
 */
class DatabaseRepositoryImpl(
    private val chatDao: ChatDao,
    private val chatMessageDao: ChatMessageDao,
) : DatabaseRepository {
    private companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 5
    }

    /**
     * Получение списка чатов с поддержкой пагинации и фильтрации по запросу
     *
     * @param query Строка для фильтрации чатов по названию
     * @return [Flow] с [PagingData] объектов [ChatDBO]
     */
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

    /**
     * Наблюдение за конкретным чатом по ID
     *
     * @param chatId Идентификатор чата
     * @return [Flow] с объектом [ChatDBO] или null, если чат не найден
     */
    override fun observeChat(chatId: Long): Flow<ChatDBO?> {
        return chatDao.observeChat(chatId)
    }

    /**
     * Создание нового чата с заданным названием
     *
     * @param title Название чата
     * @return ID созданного чата
     */
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

    /**
     * Обновление названия существующего чата
     *
     * @param chatId Идентификатор чата
     * @param title Новое название чата
     */
    override suspend fun updateChatTitle(chatId: Long, title: String) {
        val normalizedTitle = title.trim()
        chatDao.updateChatTitle(chatId = chatId, title = normalizedTitle)
    }


    /**
     * Очистка всех чатов и сообщений из базы данных
     */
    override suspend fun clearAll() {
        chatMessageDao.clearAll()
        chatDao.clearAll()
    }
}

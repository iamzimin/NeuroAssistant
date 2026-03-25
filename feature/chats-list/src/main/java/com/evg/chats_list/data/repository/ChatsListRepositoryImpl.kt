package com.evg.chats_list.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.evg.chats_list.domain.mapper.toChatListItem
import com.evg.chats_list.domain.model.ChatListItem
import com.evg.chats_list.domain.repository.ChatsListRepository
import com.evg.database.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ChatsListRepositoryImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository,
) : ChatsListRepository {
    override fun getChats(query: String?): Flow<PagingData<ChatListItem>> {
        return databaseRepository.getChats(query = query).map { pagingData ->
            pagingData.map { chat -> chat.toChatListItem() }
        }
    }

    override suspend fun createChat(title: String): Long {
        return databaseRepository.createChat(title = title)
    }
}

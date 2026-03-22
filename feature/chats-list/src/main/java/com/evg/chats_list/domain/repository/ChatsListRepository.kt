package com.evg.chats_list.domain.repository

import androidx.paging.PagingData
import com.evg.chats_list.domain.model.ChatListItem
import kotlinx.coroutines.flow.Flow

interface ChatsListRepository {
    fun getChats(query: String? = null): Flow<PagingData<ChatListItem>>
    suspend fun createChat(title: String): Long
}
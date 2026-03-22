package com.evg.database.domain.repository

import androidx.paging.PagingData
import com.evg.database.domain.model.ChatDBO
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getChats(query: String? = null): Flow<PagingData<ChatDBO>>
    suspend fun createChat(title: String): Long
}
package com.evg.database.data.storage

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.evg.database.domain.model.ChatDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatDBO): Long

    @Query(
        """
        UPDATE chats
        SET title = :title
        WHERE id = :chatId
        """
    )
    suspend fun updateChatTitle(chatId: Long, title: String)

    @Query(
        """
        SELECT * FROM chats
        ORDER BY createdAt DESC, id DESC
        """
    )
    fun getChatsPagingSource(): PagingSource<Int, ChatDBO>

    @Query(
        """
        SELECT * FROM chats
        WHERE LOWER(title) LIKE '%' || LOWER(:query) || '%'
        ORDER BY createdAt DESC, id DESC
        """
    )
    fun searchChatsPagingSource(query: String): PagingSource<Int, ChatDBO>

    @Query(
        """
        SELECT * FROM chats
        WHERE id = :chatId
        LIMIT 1
        """
    )
    fun observeChat(chatId: Long): Flow<ChatDBO?>

    @Query("DELETE FROM chats")
    suspend fun clearAll()
}

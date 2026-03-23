package com.evg.database.data.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.evg.database.domain.model.ChatMessageDBO
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query(
        """
        SELECT * FROM chat_messages
        WHERE chatId = :chatId
        ORDER BY createdAt ASC, id ASC
        """
    )
    fun observeMessages(chatId: Long): Flow<List<ChatMessageDBO>>

    @Query(
        """
        SELECT * FROM chat_messages
        WHERE chatId = :chatId
        ORDER BY createdAt ASC, id ASC
        """
    )
    suspend fun getMessages(chatId: Long): List<ChatMessageDBO>

    @Query(
        """
        SELECT * FROM chat_messages
        WHERE id = :messageId
        LIMIT 1
        """
    )
    suspend fun getMessage(messageId: Long): ChatMessageDBO?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageDBO): Long

    @Update
    suspend fun updateMessage(message: ChatMessageDBO)
}

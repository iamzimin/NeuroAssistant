package com.evg.database.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageDBO(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val chatId: Long,
    val role: String,
    val content: String,
    val status: String,
    val createdAt: Long,
    val requestUserMessageId: Long? = null,
) {
    companion object {
        const val ROLE_USER = "user"
        const val ROLE_ASSISTANT = "assistant"

        const val STATUS_SENT = "sent"
        const val STATUS_GENERATING = "generating"
        const val STATUS_ERROR = "error"
    }
}
package com.evg.chat.domain.model

data class ChatConversation(
    val chatId: Long,
    val title: String,
    val messages: List<ChatMessage>,
)

data class ChatMessage(
    val id: Long,
    val role: ChatMessageRole,
    val content: String,
    val status: ChatMessageStatus,
    val createdAt: Long,
    val requestUserMessageId: Long?,
)

enum class ChatMessageRole {
    USER,
    ASSISTANT,
}

enum class ChatMessageStatus {
    SENT,
    GENERATING,
    ERROR,
}

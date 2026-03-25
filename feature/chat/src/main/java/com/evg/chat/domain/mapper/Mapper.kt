package com.evg.chat.domain.mapper

import com.evg.api.domain.model.GigaChatRequestMessage
import com.evg.chat.domain.model.ChatConversation
import com.evg.chat.domain.model.ChatMessage
import com.evg.chat.domain.model.ChatMessageRole
import com.evg.chat.domain.model.ChatMessageStatus
import com.evg.database.domain.model.ChatDBO
import com.evg.database.domain.model.ChatMessageDBO

fun ChatDBO.toChatConversation(messages: List<ChatMessageDBO>): ChatConversation {
    return ChatConversation(
        chatId = id,
        title = title,
        messages = messages.map { message ->
            message.toChatMessage()
        },
    )
}

fun ChatMessageDBO.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        role = when (role) {
            ChatMessageDBO.ROLE_USER -> ChatMessageRole.USER
            ChatMessageDBO.ROLE_ASSISTANT -> ChatMessageRole.ASSISTANT
            else -> ChatMessageRole.ASSISTANT
        },
        content = content,
        status = when (status) {
            ChatMessageDBO.STATUS_SENT -> ChatMessageStatus.SENT
            ChatMessageDBO.STATUS_GENERATING -> ChatMessageStatus.GENERATING
            ChatMessageDBO.STATUS_ERROR -> ChatMessageStatus.ERROR
            else -> ChatMessageStatus.ERROR
        },
        createdAt = createdAt,
        requestUserMessageId = requestUserMessageId,
    )
}

fun ChatMessageDBO.toRequestMessage(): GigaChatRequestMessage {
    return GigaChatRequestMessage(
        role = role,
        content = content,
    )
}
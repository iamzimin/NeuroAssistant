package com.evg.chats_list.domain.mapper

import com.evg.chats_list.domain.model.ChatListItem
import com.evg.database.domain.model.ChatDBO

fun ChatDBO.toChatListItem(): ChatListItem {
    return ChatListItem(
        id = id,
        title = title,
        createdAt = createdAt,
    )
}
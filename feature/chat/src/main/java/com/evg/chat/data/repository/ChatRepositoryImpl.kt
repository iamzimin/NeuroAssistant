package com.evg.chat.data.repository

import com.evg.api.domain.model.GigaChatRequestMessage
import com.evg.api.domain.repository.GigaChatApiRepository
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import com.evg.chat.domain.mapper.toChatConversation
import com.evg.chat.domain.mapper.toRequestMessage
import com.evg.chat.domain.model.ChatConversation
import com.evg.chat.domain.repository.ChatRepository
import com.evg.database.domain.model.ChatMessageDBO
import com.evg.database.domain.repository.ChatHistoryRepository
import com.evg.database.domain.repository.DatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

/**
 * Репозиторий для работы с чатами, историей сообщений и API GigaChat
 *
 * @property databaseRepository Репозиторий для доступа к локальной базе данных
 * @property chatHistoryRepository Репозиторий для работы с историей сообщений
 * @property gigaChatApiRepository Репозиторий для взаимодействия с GigaChat API
 */
class ChatRepositoryImpl @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val chatHistoryRepository: ChatHistoryRepository,
    private val gigaChatApiRepository: GigaChatApiRepository,
) : ChatRepository {
    private companion object {
        const val CHAT_TITLE_MAX_LENGTH = 100
    }

    /**
     * Наблюдение за конкретной беседой чата с объединением данных и сообщений
     *
     * @param chatId ID чата
     * @return [Flow] с объектом [ChatConversation] или null
     */
    override fun observeChat(chatId: Long): Flow<ChatConversation?> {
        return combine(
            databaseRepository.observeChat(chatId),
            chatHistoryRepository.observeMessages(chatId),
        ) { chat, messages ->
            chat?.toChatConversation(messages)
        }
    }

    /**
     * Отправка сообщения пользователя и генерация ответа ассистента
     *
     * @param chatId ID чата
     * @param text Текст сообщения пользователя
     * @return [ServerResult] с [Unit] при успехе или [GigaChatError] при ошибке
     */
    override suspend fun sendMessage(
        chatId: Long,
        text: String,
    ): ServerResult<Unit, GigaChatError> {
        val normalizedText = text.trim()
        if (normalizedText.isBlank()) {
            return ServerResult.Success(Unit)
        }
        val isFirstUserMessage = chatHistoryRepository.getMessages(chatId)
            .none { it.role == ChatMessageDBO.ROLE_USER }

        val now = System.currentTimeMillis()
        val userMessageId = chatHistoryRepository.insertMessage(
            ChatMessageDBO(
                chatId = chatId,
                role = ChatMessageDBO.ROLE_USER,
                content = normalizedText,
                status = ChatMessageDBO.STATUS_SENT,
                createdAt = now,
            )
        )
        if (isFirstUserMessage) {
            databaseRepository.updateChatTitle(
                chatId = chatId,
                title = normalizedText
                    .replace(Regex("\\s+"), " ")
                    .trim()
                    .take(CHAT_TITLE_MAX_LENGTH),
            )
        }

        val assistantMessageId = chatHistoryRepository.insertMessage(
            ChatMessageDBO(
                chatId = chatId,
                role = ChatMessageDBO.ROLE_ASSISTANT,
                content = "",
                status = ChatMessageDBO.STATUS_GENERATING,
                createdAt = now + 1,
                requestUserMessageId = userMessageId,
            )
        )

        return requestAssistantReply(
            chatId = chatId,
            anchorUserMessageId = userMessageId,
            assistantMessageId = assistantMessageId,
        )
    }

    /**
     * Повторная отправка сообщения ассистента для генерации ответа
     *
     * @param chatId ID чата
     * @param assistantMessageId ID сообщения ассистента
     * @return [ServerResult] с [Unit] при успехе или [GigaChatError] при ошибке
     */
    override suspend fun retryAssistantMessage(
        chatId: Long,
        assistantMessageId: Long,
    ): ServerResult<Unit, GigaChatError> {
        val assistantMessage = chatHistoryRepository.getMessage(assistantMessageId)
            ?: return ServerResult.Error(GigaChatError.UNKNOWN)
        val anchorUserMessageId = assistantMessage.requestUserMessageId
            ?: return ServerResult.Error(GigaChatError.UNKNOWN)

        chatHistoryRepository.updateMessage(
            assistantMessage.copy(
                content = "",
                status = ChatMessageDBO.STATUS_GENERATING,
            )
        )

        return requestAssistantReply(
            chatId = chatId,
            anchorUserMessageId = anchorUserMessageId,
            assistantMessageId = assistantMessageId,
        )
    }

    /**
     * Отправка запроса ассистенту и обновление состояния сообщения ассистента
     *
     * @param chatId ID чата
     * @param anchorUserMessageId ID якорного сообщения пользователя
     * @param assistantMessageId ID сообщения ассистента для обновления
     * @return [ServerResult] с [Unit] при успехе или [GigaChatError] при ошибке
     */
    private suspend fun requestAssistantReply(
        chatId: Long,
        anchorUserMessageId: Long,
        assistantMessageId: Long,
    ): ServerResult<Unit, GigaChatError> {
        val requestMessages = buildRequestMessages(chatId, anchorUserMessageId)
        if (requestMessages.isEmpty()) {
            markAssistantMessageAsError(assistantMessageId)
            return ServerResult.Error(GigaChatError.UNKNOWN)
        }

        return when (val response = gigaChatApiRepository.getAnswer(requestMessages)) {
            is ServerResult.Success -> {
                val assistantMessage = chatHistoryRepository.getMessage(assistantMessageId)
                    ?: return ServerResult.Error(GigaChatError.UNKNOWN)

                chatHistoryRepository.updateMessage(
                    assistantMessage.copy(
                        content = response.data.content,
                        status = ChatMessageDBO.STATUS_SENT,
                    )
                )
                ServerResult.Success(Unit)
            }

            is ServerResult.Error -> {
                markAssistantMessageAsError(assistantMessageId)
                ServerResult.Error(response.error)
            }
        }
    }

    /**
     * Формирование списка сообщений для запроса к GigaChat
     *
     * @param chatId ID чата
     * @param anchorUserMessageId ID якорного сообщения пользователя
     * @return Список сообщений [GigaChatRequestMessage] для запроса
     */
    private suspend fun buildRequestMessages(
        chatId: Long,
        anchorUserMessageId: Long,
    ): List<GigaChatRequestMessage> {
        val orderedMessages = chatHistoryRepository.getMessages(chatId)
        val anchorIndex = orderedMessages.indexOfFirst { it.id == anchorUserMessageId }
        if (anchorIndex == -1) {
            return emptyList()
        }

        return orderedMessages
            .take(anchorIndex + 1)
            .filter { it.status == ChatMessageDBO.STATUS_SENT }
            .map { it.toRequestMessage() }
    }

    /**
     * Смена сообщения ассистента как ошибки
     *
     * @param assistantMessageId ID сообщения ассистента
     */
    private suspend fun markAssistantMessageAsError(assistantMessageId: Long) {
        val assistantMessage = chatHistoryRepository.getMessage(assistantMessageId) ?: return
        chatHistoryRepository.updateMessage(
            assistantMessage.copy(
                status = ChatMessageDBO.STATUS_ERROR,
                content = "",
            )
        )
    }
}
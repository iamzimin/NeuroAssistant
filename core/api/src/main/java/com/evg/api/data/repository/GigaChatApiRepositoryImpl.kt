package com.evg.api.data.repository

import com.evg.api.data.remote.GigaChatCredentials
import com.evg.api.domain.service.GigaChatService
import com.evg.api.data.remote.model.GigaChatMessageRequest
import com.evg.api.data.remote.model.GigaChatRequest
import com.evg.api.domain.mapper.toGigaChatError
import com.evg.api.domain.model.GigaChatCompletionResult
import com.evg.api.domain.model.GigaChatRequestMessage
import com.evg.api.domain.repository.GigaChatApiRepository
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Реализация репозитория для работы с GigaChat API
 *
 * @property tokenProvider Провайдер для получения токена доступа к API
 * @property gigaChatService Сервис для выполнения сетевых запросов к GigaChat
 */
@Singleton
class GigaChatApiRepositoryImpl @Inject constructor(
    private val tokenProvider: GigaChatTokenProvider,
    private val gigaChatService: GigaChatService,
) : GigaChatApiRepository {
    /**
     * Получение ответа от GigaChat по списку сообщений
     *
     * @param messages Список сообщений для передачи в модель
     * @return [ServerResult] с результатом [GigaChatCompletionResult] или [GigaChatError]
     */
    override suspend fun getAnswer(
        messages: List<GigaChatRequestMessage>,
    ): ServerResult<GigaChatCompletionResult, GigaChatError> {
        if (messages.isEmpty()) {
            return ServerResult.Error(GigaChatError.UNKNOWN)
        }

        return withAccessToken { accessToken ->
            val response = gigaChatService.getAnswer(
                authorization = bearer(accessToken),
                request = GigaChatRequest(
                    model = GigaChatCredentials.MODEL,
                    messages = messages.map { message ->
                        GigaChatMessageRequest(
                            role = message.role,
                            content = message.content,
                        )
                    },
                ),
            )

            val rawContent = response.choices.firstOrNull()?.message?.content.orEmpty()
            if (rawContent.isBlank()) {
                return@withAccessToken ServerResult.Error(GigaChatError.UNKNOWN)
            }

            ServerResult.Success(
                GigaChatCompletionResult(
                    content = rawContent,
                )
            )
        }
    }

    /**
     * Получение баланса токенов для указанной модели
     *
     * @param model Название модели для проверки баланса
     * @return [ServerResult] с числом оставшихся токенов или [GigaChatError]
     */
    override suspend fun getTokenBalance(
        model: String,
    ): ServerResult<Int, GigaChatError> {
        return withAccessToken { accessToken ->
            val response = gigaChatService.getBalance(
                authorization = bearer(accessToken),
            )

            val balance = response.balance
                .firstOrNull { item -> item.usage.equals(model, ignoreCase = true) }
                ?.value
                ?: return@withAccessToken ServerResult.Error(GigaChatError.NOT_FOUND)

            ServerResult.Success(balance)
        }
    }

    /**
     * Выполнение блока с действующим токеном доступа
     *
     * @param T Тип результата блока
     * @param block Суспенд-блок, принимающий токен и возвращающий [ServerResult]
     * @return [ServerResult] результата выполнения блока
     */
    private suspend fun <T> withAccessToken(
        block: suspend (String) -> ServerResult<T, GigaChatError>,
    ): ServerResult<T, GigaChatError> {
        return when (val tokenResult = tokenProvider.getAccessToken()) {
            is ServerResult.Success -> safeApiCall { block(tokenResult.data) }
            is ServerResult.Error -> ServerResult.Error(tokenResult.error)
        }
    }

    /**
     * Безопасный вызов API с обработкой ошибок
     *
     * @param T Тип результата блока
     * @param block Суспенд-блок, возвращающий [ServerResult]
     * @return [ServerResult] результата выполнения блока или конвертированная ошибка
     */
    private suspend fun <T> safeApiCall(
        block: suspend () -> ServerResult<T, GigaChatError>,
    ): ServerResult<T, GigaChatError> {
        return runCatching { block() }
            .getOrElse { throwable ->
                ServerResult.Error(throwable.toGigaChatError())
            }
    }

    private fun bearer(accessToken: String): String = "Bearer $accessToken"
}

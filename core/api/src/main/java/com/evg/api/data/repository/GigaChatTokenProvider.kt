package com.evg.api.data.repository

import com.evg.api.domain.service.GigaChatAuthService
import com.evg.api.data.remote.GigaChatCredentials
import com.evg.api.domain.mapper.toGigaChatError
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Провайдер токена доступа для GigaChat с кешированием и синхронизацией
 *
 * @property authService Сервис для получения токена доступа
 */
@Singleton
class GigaChatTokenProvider @Inject constructor(
    private val authService: GigaChatAuthService,
) {
    private companion object {
        const val TOKEN_REFRESH_MS = 15 * 60 * 1000L
        const val BASIC = "Basic ${GigaChatCredentials.AUTHORIZATION_KEY}"
    }

    private val mutex = Mutex()
    private var cachedToken: CachedToken? = null

    /**
     * Получение актуального токена доступа с учетом кеша
     *
     * @return [ServerResult] с токеном в случае успеха или [GigaChatError] при ошибке
     */
    suspend fun getAccessToken(): ServerResult<String, GigaChatError> {
        val now = System.currentTimeMillis()
        cachedToken?.takeIf { it.isValid(now) }?.let { token ->
            return ServerResult.Success(token.value)
        }

        return mutex.withLock {
            val freshNow = System.currentTimeMillis()
            cachedToken?.takeIf { it.isValid(freshNow) }?.let { token ->
                return@withLock ServerResult.Success(token.value)
            }

            safeApiCall {
                authService.getAccessToken(
                    requestId = UUID.randomUUID().toString(),
                    authorization = BASIC,
                    scope = GigaChatCredentials.OAUTH_SCOPE,
                )
            }.let { result ->
                when (result) {
                    is ServerResult.Success -> {
                        cachedToken = CachedToken(
                            value = result.data.accessToken,
                            expiresAt = result.data.expiresAt,
                        )
                        ServerResult.Success(result.data.accessToken)
                    }

                    is ServerResult.Error -> ServerResult.Error(result.error)
                }
            }
        }
    }

    /**
     * Безопасный вызов API с обработкой исключений
     *
     * @param T Тип возвращаемого значения блока
     * @param block Суспенд-блок для выполнения API запроса
     * @return [ServerResult] с результатом блока или конвертированной ошибкой
     */
    private suspend fun <T> safeApiCall(
        block: suspend () -> T,
    ): ServerResult<T, GigaChatError> {
        return runCatching { block() }
            .fold(
                onSuccess = { ServerResult.Success(it) },
                onFailure = { throwable -> ServerResult.Error(throwable.toGigaChatError()) },
            )
    }

    private fun CachedToken.isValid(now: Long): Boolean {
        return expiresAt > now + TOKEN_REFRESH_MS
    }

    private data class CachedToken(
        val value: String,
        val expiresAt: Long,
    )
}

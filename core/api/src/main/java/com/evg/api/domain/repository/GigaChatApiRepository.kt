package com.evg.api.domain.repository

import com.evg.api.domain.model.GigaChatCompletionResult
import com.evg.api.domain.model.GigaChatRequestMessage
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult

interface GigaChatApiRepository {
    suspend fun getAnswer(
        messages: List<GigaChatRequestMessage>,
    ): ServerResult<GigaChatCompletionResult, GigaChatError>
    suspend fun getTokenBalance(
        model: String,
    ): ServerResult<Int, GigaChatError>
}
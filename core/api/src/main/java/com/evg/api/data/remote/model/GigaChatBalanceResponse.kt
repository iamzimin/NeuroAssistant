package com.evg.api.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class GigaChatBalanceResponse(
    val balance: List<GigaChatBalanceItemResponse> = emptyList(),
)

@Serializable
data class GigaChatBalanceItemResponse(
    val usage: String = "",
    val value: Int = 0,
)
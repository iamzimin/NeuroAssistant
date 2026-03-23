package com.evg.api.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GigaChatTokenResponse(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_at")
    val expiresAt: Long,
)
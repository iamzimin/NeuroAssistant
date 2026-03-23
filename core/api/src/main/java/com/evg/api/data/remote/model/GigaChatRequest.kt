package com.evg.api.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GigaChatRequest(
    val model: String,
    val messages: List<GigaChatMessageRequest>,
    val stream: Boolean = false,
    @SerialName("update_interval")
    val updateInterval: Int = 0,
    @SerialName("function_call")
    val functionCall: String = "auto",
)

@Serializable
data class GigaChatMessageRequest(
    val role: String,
    val content: String,
)
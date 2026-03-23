package com.evg.api.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GigaChatResponse(
    val choices: List<GigaChatChoice> = emptyList(),
)

@Serializable
data class GigaChatChoice(
    val message: GigaChatMessageResponse? = null,
)

@Serializable
data class GigaChatMessageResponse(
    val role: String? = null,
    val content: String = "",
    val name: String? = null,
    @SerialName("functions_state_id")
    val functionsStateId: String? = null,
)
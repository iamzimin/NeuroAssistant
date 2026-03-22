package com.evg.chats_list.domain.model

data class TestModel(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
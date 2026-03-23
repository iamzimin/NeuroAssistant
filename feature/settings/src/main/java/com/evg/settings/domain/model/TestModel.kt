package com.evg.settings.domain.model

data class TestModel(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)
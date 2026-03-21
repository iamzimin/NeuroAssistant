package com.evg.neuroassistant.navigation

import kotlinx.serialization.Serializable


sealed interface Route {
    @Serializable data object Login: Route
    @Serializable data object Registration: Route
    @Serializable data object ChatsList: Route
    @Serializable data class Chat(val id: Long): Route
}

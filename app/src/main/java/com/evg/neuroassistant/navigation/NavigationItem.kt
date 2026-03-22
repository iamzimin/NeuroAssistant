package com.evg.neuroassistant.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.evg.resource.R

data class NavigationItem(
    @StringRes val title: Int,
    val route: Route,
    @DrawableRes val icon: Int,
) {
    companion object {
        val items = listOf(
            NavigationItem(
                title = R.string.chats_list,
                route = Route.ChatsList,
                icon = R.drawable.eye_on,
            ),
        )
    }
}
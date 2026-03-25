package com.evg.neuroassistant.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.evg.resource.R
import com.evg.ui.theme.AppTheme

val topNavPadding = 45.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navigation: NavController,
    title: String,
    onPreviousScreen: () -> Unit,
) {
    val navBackStackEntry by navigation.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val topBarVisible = currentDestination?.hasRoute(Route.Chat::class) == true

    AnimatedVisibility(
        visible = topBarVisible,
        modifier = Modifier.fillMaxWidth(),
        enter = slideInVertically(initialOffsetY = { -it }),
        exit = slideOutVertically(targetOffsetY = { -it }),
    ) {
        Column {
            TopAppBar(
                expandedHeight = topNavPadding - 1.dp,
                title = {
                    Text(
                        text = title,
                        style = AppTheme.typography.body,
                        color = AppTheme.colors.text,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onPreviousScreen) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(R.drawable.navigation_back),
                            contentDescription = null,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = AppTheme.colors.text,
                )
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = AppTheme.colors.primary.copy(alpha = 0.5f),
            )
        }
    }
}
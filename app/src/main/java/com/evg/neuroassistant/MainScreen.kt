package com.evg.neuroassistant

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.evg.chat.presentation.ChatRoot
import com.evg.chats_list.presentation.ChatsListRoot
import com.evg.login.presentation.LoginRoot
import com.evg.neuroassistant.navigation.NavigationItem
import com.evg.neuroassistant.navigation.NeuroAssistantScaffold
import com.evg.neuroassistant.navigation.Route
import com.evg.neuroassistant.navigation.TopBar
import com.evg.neuroassistant.navigation.topNavPadding
import com.evg.neuroassistant.snackbar.ObserveAsEvent
import com.evg.neuroassistant.snackbar.SwipeableSnackBarHost
import com.evg.settings.presentation.SettingsRoot
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.theme.AppTheme
import com.evg.resource.R
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val chatName = stringResource(R.string.empty_chat_title)

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val currentDes = navController.currentBackStackEntryAsState().value?.destination
    val isLoginScreen = currentDes?.hasRoute(Route.Login::class) == true
    val snackBarHostState = remember { SnackbarHostState() }
    val startDestination = viewModel.startDestination
    val isSplashVisible = viewModel.isSplashVisible

    var currentChatTitle by remember {
        mutableStateOf("")
    }
    val selectedItemIndex = NavigationItem.items.indexOfFirst { item ->
        currentDes?.hasRoute(item.route::class) == true
    }.takeIf { it >= 0 }

    ObserveAsEvent(
        flow = SnackBarController.events,
        snackBarHostState,
    ) { event ->
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            val result = snackBarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short,
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    LaunchedEffect(isSplashVisible) {
        if (isSplashVisible) {
            delay(1600)
            viewModel.dismissSplash()
        }
    }

    LaunchedEffect(viewModel.createdChatId) {
        if (viewModel.createdChatId < 0L) return@LaunchedEffect

        currentChatTitle = ""
        navController.navigate(Route.Chat(viewModel.createdChatId)) {
            popUpTo(Route.Chat::class) { inclusive = true }
        }
        drawerState.close()
        viewModel.clearCreatedChat()
    }

    ModalNavigationDrawer(
        modifier = Modifier
            .background(AppTheme.colors.background),
        drawerState = drawerState,
        gesturesEnabled = !isLoginScreen,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .padding(end = 70.dp),
                drawerContainerColor = AppTheme.colors.tileBackground,
            ) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))

                Text(
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding),
                    text = stringResource(id = R.string.app_name),
                    style = AppTheme.typography.heading,
                    color = AppTheme.colors.text,
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))

                NavigationDrawerItem(
                    label = {
                        Text(
                            text = stringResource(R.string.new_chat),
                            style = AppTheme.typography.body,
                            color = AppTheme.colors.text,
                        )
                    },
                    selected = false,
                    onClick = {
                        viewModel.createChat(chatName)
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = AppTheme.colors.secondary,
                    ),
                    icon = {
                        if (viewModel.isCreatingChat) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = AppTheme.colors.primary,
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = R.drawable.plus),
                                contentDescription = stringResource(R.string.new_chat),
                                tint = AppTheme.colors.text,
                            )
                        }
                    },
                    shape = RectangleShape,
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

                NavigationItem.items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(item.title),
                                style = AppTheme.typography.body,
                                color = AppTheme.colors.text,
                            )
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedContainerColor = Color.Transparent,
                            selectedContainerColor = AppTheme.colors.secondary,
                        ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = item.icon),
                                contentDescription = stringResource(item.title),
                                tint = AppTheme.colors.text,
                            )
                        },
                        shape = RectangleShape,
                    )
                }
            }
        },
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize().imePadding(),
            topBar = { TopBar(
                navigation = navController,
                title = currentChatTitle,
                onPreviousScreen = {
                    navController.popBackStack()
                })
            },
            containerColor = AppTheme.colors.background,
            snackbarHost = { SwipeableSnackBarHost(hostState = snackBarHostState) }
        ) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable<Route.Login> {
                    NeuroAssistantScaffold { paddingValues ->
                        LoginRoot(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            onNavigateToHome = {
                                navController.navigate(Route.ChatsList) {
                                    popUpTo(Route.Login) { inclusive = true }
                                }
                            },
                        )
                    }
                }
                composable<Route.ChatsList> {
                    NeuroAssistantScaffold { paddingValues ->
                        ChatsListRoot(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            onNavigateToChat = { chatId ->
                                currentChatTitle = ""
                                navController.navigate(Route.Chat(chatId))
                            },
                        )
                    }
                }
                composable<Route.Settings> {
                    NeuroAssistantScaffold { paddingValues ->
                        SettingsRoot(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            onLogout = {
                                currentChatTitle = ""
                                navController.navigate(Route.Login) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            },
                        )
                    }
                }
                composable<Route.Chat> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.Chat>()

                    NeuroAssistantScaffold(
                        modifier = Modifier.padding(top = topNavPadding),
                    ) { paddingValues ->
                        ChatRoot(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            chatId = route.id,
                            onChatTitleChanged = { title ->
                                currentChatTitle = title
                            },
                        )
                    }
                }
            }
        }
    }

    AnimatedVisibility(
        visible = isSplashVisible,
        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
    ) {
        AppLaunchSplash()
    }
}

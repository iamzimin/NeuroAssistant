package com.evg.neuroassistant

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.evg.login.presentation.LoginRoot
import com.evg.neuroassistant.navigation.NeuroAssistantScaffold
import com.evg.neuroassistant.navigation.Route
import com.evg.neuroassistant.navigation.TopBar
import com.evg.neuroassistant.snackbar.ObserveAsEvent
import com.evg.neuroassistant.snackbar.SwipeableSnackBarHost
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.theme.AppTheme
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val snackBarHostState = remember { SnackbarHostState() }
    val startDestination = viewModel.startDestination

    val scope = rememberCoroutineScope()
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(
            navigation = navController,
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
                Text(text = "Chats List Screen", modifier = Modifier.fillMaxSize())
            }
        }
    }
}

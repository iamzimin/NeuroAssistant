package com.evg.chat.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.evg.chat.presentation.mvi.ChatSideEffect
import com.evg.chat.presentation.mvi.ChatAction
import com.evg.chat.presentation.mvi.ChatViewModel
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.evg.ui.mapper.toErrorMessage
import com.evg.ui.snackbar.SnackBarController
import com.evg.ui.snackbar.SnackBarEvent
import com.evg.resource.R
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ChatRoot(
    modifier: Modifier,
    chatId: Long,
    onChatTitleChanged: (String) -> Unit,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = viewModel.collectAsState().value

    LaunchedEffect(chatId) {
        viewModel.dispatch(ChatAction.Initialize(chatId))
    }

    LaunchedEffect(state.title) {
        onChatTitleChanged(state.title)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is ChatSideEffect.ShareMessage -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, sideEffect.text)
                }
                context.startActivity(
                    Intent.createChooser(
                        shareIntent,
                        context.getString(R.string.chat_share_chooser_title),
                    )
                )
            }

            is ChatSideEffect.ShowError -> {
                scope.launch {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = sideEffect.error.toErrorMessage(context),
                        )
                    )
                }
            }
        }
    }

    ChatScreen(
        modifier = modifier,
        state = state,
        dispatch = viewModel::dispatch,
    )
}
package com.evg.chat.presentation

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.evg.chat.domain.model.ChatMessage
import com.evg.chat.domain.model.ChatMessageRole
import com.evg.chat.domain.model.ChatMessageStatus
import com.evg.chat.presentation.message.ChatMessageItem
import com.evg.chat.presentation.mvi.ChatAction
import com.evg.chat.presentation.mvi.ChatState
import com.evg.resource.R
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatState,
    dispatch: (ChatAction) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.lastOrNull()?.id) {
        if (state.messages.isNotEmpty()) {
            listState.scrollToItem(state.messages.lastIndex)
        }
    }

    Column(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimens.horizontalPadding,
                vertical = AppTheme.dimens.verticalPadding,
            ),
    ) {
        if (state.messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.chat_empty_state),
                    style = AppTheme.typography.body,
                    color = AppTheme.colors.text,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
                contentPadding = PaddingValues(
                    top = AppTheme.dimens.paddingSmall,
                    bottom = AppTheme.dimens.paddingMedium,
                ),
            ) {
                items(
                    items = state.messages,
                    key = { message -> message.id },
                ) { message ->
                    ChatMessageItem(
                        message = message,
                        onRetry = {
                            dispatch(ChatAction.OnRetryClicked(message.id))
                        },
                        onLongClick = {
                            dispatch(ChatAction.OnAssistantMessageLongClicked(message.id))
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingSmall))

        ChatInputBar(
            value = state.input,
            isSending = state.isRequestInProgress,
            onValueChange = { dispatch(ChatAction.OnMessageChanged(it)) },
            onClearClick = { dispatch(ChatAction.OnClearInputClicked) },
            onSendClick = { dispatch(ChatAction.OnSendClicked) },
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatScreen(
                state = ChatState(
                    title = "New chat",
                    input = "Show me a cozy reading corner",
                    messages = listOf(
                        ChatMessage(
                            id = 1,
                            role = ChatMessageRole.USER,
                            content = "Draw a calm cyberpunk street in the rain",
                            status = ChatMessageStatus.SENT,
                            createdAt = 1L,
                            requestUserMessageId = null,
                        ),
                        ChatMessage(
                            id = 2,
                            role = ChatMessageRole.ASSISTANT,
                            content = "Here is a first concept with neon reflections and warm windows.",
                            status = ChatMessageStatus.SENT,
                            createdAt = 2L,
                            requestUserMessageId = 1L,
                        ),
                        ChatMessage(
                            id = 3,
                            role = ChatMessageRole.ASSISTANT,
                            content = "",
                            status = ChatMessageStatus.GENERATING,
                            createdAt = 3L,
                            requestUserMessageId = 1L,
                        ),
                    ),
                ),
                dispatch = {},
            )
        }
    }
}
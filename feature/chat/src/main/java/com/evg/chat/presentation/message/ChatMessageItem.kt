package com.evg.chat.presentation.message

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.chat.domain.model.ChatMessage
import com.evg.chat.domain.model.ChatMessageRole
import com.evg.chat.domain.model.ChatMessageStatus
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    onRetry: () -> Unit,
    onLongClick: () -> Unit,
) {
    val isUser = message.role == ChatMessageRole.USER
    val bubbleColor = if (isUser) AppTheme.colors.primary else AppTheme.colors.tileBackground
    val contentColor = if (isUser) AppTheme.colors.background else AppTheme.colors.text
    val bubbleShape = RoundedCornerShape(
        topStart = 22.dp,
        topEnd = 22.dp,
        bottomStart = if (isUser) 22.dp else 8.dp,
        bottomEnd = if (isUser) 8.dp else 22.dp,
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        val interactionModifier = if (
            !isUser &&
            message.status == ChatMessageStatus.SENT &&
            message.content.isNotBlank()
        ) {
            Modifier.combinedClickable(
                onClick = {},
                onLongClick = onLongClick,
            )
        } else {
            Modifier
        }

        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(bubbleShape)
                .background(bubbleColor)
                .then(interactionModifier)
                .padding(AppTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
        ) {
            when (message.status) {
                ChatMessageStatus.GENERATING -> GeneratingMessagePlaceholder()
                ChatMessageStatus.ERROR -> ErrorMessageContent(
                    onRetry = onRetry,
                )
                ChatMessageStatus.SENT -> SentMessageContent(
                    message = message,
                    contentColor = contentColor,
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatMessageItemPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatMessageItem(
                message = ChatMessage(
                    id = 1L,
                    role = ChatMessageRole.USER,
                    content = "Test message",
                    status = ChatMessageStatus.SENT,
                    createdAt = System.currentTimeMillis(),
                    requestUserMessageId = null,
                ),
                onRetry = {},
                onLongClick = {},
            )
        }
    }
}
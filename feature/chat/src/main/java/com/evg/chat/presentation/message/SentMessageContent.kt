package com.evg.chat.presentation.message

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.evg.chat.domain.model.ChatMessage
import com.evg.chat.domain.model.ChatMessageRole
import com.evg.chat.domain.model.ChatMessageStatus
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun SentMessageContent(
    message: ChatMessage,
    contentColor: Color,
) {
    if (message.content.isNotBlank()) {
        Text(
            text = message.content,
            style = AppTheme.typography.body,
            color = contentColor,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun SentMessageContentPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            SentMessageContent(
                message = ChatMessage(
                    id = 1L,
                    role = ChatMessageRole.USER,
                    content = "Test message",
                    status = ChatMessageStatus.SENT,
                    createdAt = System.currentTimeMillis(),
                    requestUserMessageId = null,
                ),
                contentColor = AppTheme.colors.text,
            )
        }
    }
}
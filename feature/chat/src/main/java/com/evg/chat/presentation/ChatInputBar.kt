package com.evg.chat.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.evg.resource.R
import com.evg.ui.custom.DefaultTextField
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ChatInputBar(
    value: String,
    isSending: Boolean,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onSendClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        DefaultTextField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChange = onValueChange,
            minLines = 1,
            maxLines = 5,
            singleLine = false,
            placeholder = stringResource(R.string.chat_message_hint),
            trailingIcon = {
                if (value.isNotBlank()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            modifier = Modifier.size(30.dp),
                            painter = painterResource(id = R.drawable.clear),
                            contentDescription = stringResource(R.string.chat_clear_input),
                            tint = AppTheme.colors.text,
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.width(AppTheme.dimens.paddingSmall))

        IconButton(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (value.isBlank() || isSending) {
                        AppTheme.colors.tileBackground
                    } else {
                        AppTheme.colors.primary
                    }
                ),
            onClick = onSendClick,
            enabled = value.isNotBlank() && !isSending,
        ) {
            if (isSending) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = AppTheme.colors.text,
                )
            } else {
                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.send),
                    contentDescription = stringResource(R.string.chat_send),
                    tint = if (value.isBlank()) {
                        AppTheme.colors.text.copy(alpha = 0.5f)
                    } else {
                        AppTheme.colors.background
                    },
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatInputBarPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatInputBar(
                value = "Test message",
                isSending = false,
                onValueChange = {},
                onClearClick = {},
                onSendClick = {},
            )
        }
    }
}
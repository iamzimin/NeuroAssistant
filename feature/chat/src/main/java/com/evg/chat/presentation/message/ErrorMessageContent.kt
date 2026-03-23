package com.evg.chat.presentation.message

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.evg.resource.R
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ErrorMessageContent(
    onRetry: () -> Unit,
) {
    Text(
        text = stringResource(R.string.chat_response_error),
        style = AppTheme.typography.body,
        color = AppTheme.colors.text,
    )

    TextButton(onClick = onRetry) {
        Text(
            text = stringResource(R.string.retry),
            color = AppTheme.colors.primary,
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ErrorMessageContentPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ErrorMessageContent(
                onRetry = {},
            )
        }
    }
}
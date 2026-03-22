package com.evg.chat.presentation

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import com.evg.chat.presentation.mvi.ChatState
import com.evg.chat.presentation.mvi.ChatAction
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    state: ChatState,
    dispatch: (ChatAction) -> Unit,
) {

}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatScreen(
                state = ChatState(),
                dispatch = {},
            )
        }
    }
}
package com.evg.chats_list.presentation

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.evg.chats_list.presentation.mvi.ChatsListState
import com.evg.chats_list.presentation.mvi.ChatsListAction
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme

@Composable
fun ChatsListScreen(
    modifier: Modifier = Modifier,
    state: ChatsListState,
    dispatch: (ChatsListAction) -> Unit,
) {

}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun LoginScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            ChatsListScreen(
                state = ChatsListState(),
                dispatch = {},
            )
        }
    }
}
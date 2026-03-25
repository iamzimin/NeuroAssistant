package com.evg.chats_list.presentation

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.evg.chats_list.domain.model.ChatListItem
import com.evg.chats_list.presentation.mvi.ChatsListAction
import com.evg.chats_list.presentation.mvi.ChatsListState
import com.evg.resource.R
import com.evg.ui.custom.DefaultTextField
import com.evg.ui.theme.AppTheme
import com.evg.ui.theme.NeuroAssistantTheme
import com.evg.ui.theme.darkAddButtonColor
import com.evg.ui.theme.lightAddButtonColor
import kotlinx.coroutines.flow.flowOf

@Composable
fun ChatsListScreen(
    modifier: Modifier = Modifier,
    state: ChatsListState,
    chats: LazyPagingItems<ChatListItem>,
    dispatch: (ChatsListAction) -> Unit,
) {
    val chatName = stringResource(R.string.empty_chat_title)

    Column(
        modifier = modifier
            .padding(
                horizontal = AppTheme.dimens.horizontalPadding,
                vertical = AppTheme.dimens.verticalPadding,
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DefaultTextField(
                modifier = Modifier.weight(1f),
                value = state.searchQuery,
                onValueChange = { dispatch(ChatsListAction.OnSearchQueryChanged(it)) },
                singleLine = true,
                placeholder = stringResource(R.string.search_chats_hint),
            )

            Spacer(modifier = Modifier.width(AppTheme.dimens.paddingSmall))

            IconButton(
                modifier = Modifier.size(50.dp),
                onClick = { dispatch(ChatsListAction.OnSearchClicked) },
                shape = MaterialTheme.shapes.medium,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = AppTheme.colors.primary,
                    contentColor = AppTheme.colors.background,
                )
            ) {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = null,
                    tint = AppTheme.colors.background,
                )
            }
        }

        Spacer(modifier = Modifier.height(AppTheme.dimens.paddingMedium))

        when (chats.loadState.refresh) {
            is LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = AppTheme.colors.primary)
                }
            }

            is LoadState.Error -> {
                ChatsListError(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = chats::retry,
                )
            }

            is LoadState.NotLoading -> {
                if (chats.itemCount == 0) {
                    ChatsListEmpty(
                        modifier = Modifier.fillMaxSize(),
                        hasActiveSearch = !state.appliedQuery.isNullOrBlank(),
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.lazyColumnSpacedBy),
                        contentPadding = PaddingValues(bottom = 100.dp),
                    ) {
                        items(
                            count = chats.itemCount,
                            key = { index -> chats[index]?.id ?: index },
                        ) { index ->
                            val chat = chats[index] ?: return@items

                            ChatListItem(
                                chat = chat,
                                onClick = {
                                    dispatch(ChatsListAction.OnChatClicked(chat.id))
                                },
                            )
                        }

                        if (chats.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(AppTheme.dimens.paddingMedium),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator(color = AppTheme.colors.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        RoundedButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(AppTheme.dimens.buttonPadding),
            backgroundColor = if (isSystemInDarkTheme()) darkAddButtonColor else lightAddButtonColor,
            icon = painterResource(id = R.drawable.plus),
            iconColor = AppTheme.colors.background,
            isLoading = state.isCreatingChat,
            onClick = {
                dispatch(ChatsListAction.OnCreateChatClicked(chatName))
            },
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun ChatsListScreenPreview(darkTheme: Boolean = true) {
    NeuroAssistantTheme(darkTheme = darkTheme) {
        Surface(color = AppTheme.colors.background) {
            val previewChats = flowOf(
                PagingData.from(
                    listOf(
                        ChatListItem(id = 1, title = "Travel ideas", createdAt = 1L),
                        ChatListItem(id = 2, title = "Work notes", createdAt = 2L),
                    )
                )
            ).collectAsLazyPagingItems()

            ChatsListScreen(
                state = ChatsListState(),
                chats = previewChats,
                dispatch = {},
            )
        }
    }
}
package com.evg.chats_list.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.evg.chats_list.domain.repository.ChatsListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ChatsListViewModel @Inject constructor(
    private val repository: ChatsListRepository,
) : ContainerHost<ChatsListState, ChatsListSideEffect>, ViewModel() {
    override val container = container<ChatsListState, ChatsListSideEffect>(ChatsListState())

    private val appliedQuery = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val chats = appliedQuery
        .flatMapLatest { query -> repository.getChats(query) }
        .cachedIn(viewModelScope)

    fun dispatch(action: ChatsListAction) {
        when (action) {
            is ChatsListAction.OnSearchQueryChanged -> updateSearchQuery(action.query)
            is ChatsListAction.OnSearchClicked -> applySearch()
            is ChatsListAction.OnCreateChatClicked -> createChat(action.title)
            is ChatsListAction.OnChatClicked -> openChat(action.chatId)
        }
    }

    private fun updateSearchQuery(query: String) = intent {
        reduce { state.copy(searchQuery = query) }
    }

    private fun applySearch() = intent {
        val normalizedQuery = state.searchQuery.trim()
        //if (normalizedQuery.isBlank()) return@intent // TODO

        reduce { state.copy(appliedQuery = normalizedQuery) }
        appliedQuery.value = normalizedQuery
    }

    private fun createChat(title: String) = intent {
        if (state.isCreatingChat) return@intent

        reduce { state.copy(isCreatingChat = true) }

        runCatching { repository.createChat(title = title) }
            .onSuccess { chatId ->
                postSideEffect(ChatsListSideEffect.NavigateToChat(chatId))
            }

        reduce { state.copy(isCreatingChat = false) }
    }

    private fun openChat(chatId: Long) = intent {
        postSideEffect(ChatsListSideEffect.NavigateToChat(chatId))
    }
}
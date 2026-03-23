package com.evg.chat.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evg.api.domain.utils.ServerResult
import com.evg.chat.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
) : ContainerHost<ChatState, ChatSideEffect>, ViewModel() {
    override val container = container<ChatState, ChatSideEffect>(ChatState())
    private var observeChatJob: Job? = null

    fun dispatch(action: ChatAction) {
        when (action) {
            is ChatAction.Initialize -> initialize(action.chatId)
            is ChatAction.OnMessageChanged -> updateMessage(action.value)
            ChatAction.OnSendClicked -> sendMessage()
            ChatAction.OnClearInputClicked -> clearInput()
            is ChatAction.OnRetryClicked -> retryMessage(action.assistantMessageId)
            is ChatAction.OnAssistantMessageLongClicked -> shareMessage(action.assistantMessageId)
        }
    }

    private fun initialize(chatId: Long) = intent {
        if (state.chatId == chatId && observeChatJob != null) return@intent

        reduce { state.copy(chatId = chatId) }

        observeChatJob?.cancel()
        observeChatJob = viewModelScope.launch {
            chatRepository.observeChat(chatId).collect { conversation ->
                intent {
                    reduce {
                        state.copy(
                            chatId = chatId,
                            title = conversation?.title.orEmpty(),
                            messages = conversation?.messages.orEmpty(),
                        )
                    }
                }
            }
        }
    }

    private fun updateMessage(value: String) = intent {
        reduce { state.copy(input = value) }
    }

    private fun clearInput() = intent {
        reduce { state.copy(input = "") }
    }

    private fun sendMessage() = intent {
        val chatId = state.chatId ?: return@intent
        val message = state.input.trim()
        if (message.isBlank() || state.isRequestInProgress) return@intent

        reduce {
            state.copy(
                input = "",
                isRequestInProgress = true,
            )
        }

        when (val result = chatRepository.sendMessage(chatId, message)) {
            is ServerResult.Success -> Unit
            is ServerResult.Error -> postSideEffect(ChatSideEffect.ShowError(result.error))
        }

        reduce { state.copy(isRequestInProgress = false) }
    }

    private fun retryMessage(assistantMessageId: Long) = intent {
        val chatId = state.chatId ?: return@intent
        if (state.isRequestInProgress) return@intent

        reduce { state.copy(isRequestInProgress = true) }

        when (val result = chatRepository.retryAssistantMessage(chatId, assistantMessageId)) {
            is ServerResult.Success -> Unit
            is ServerResult.Error -> postSideEffect(ChatSideEffect.ShowError(result.error))
        }

        reduce { state.copy(isRequestInProgress = false) }
    }

    private fun shareMessage(assistantMessageId: Long) = intent {
        val message = state.messages.firstOrNull { it.id == assistantMessageId } ?: return@intent
        val content = message.content.trim()
        if (content.isBlank()) return@intent

        postSideEffect(ChatSideEffect.ShareMessage(content))
    }
}
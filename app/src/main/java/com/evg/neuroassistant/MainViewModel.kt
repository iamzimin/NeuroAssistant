package com.evg.neuroassistant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.database.domain.repository.DatabaseRepository
import com.evg.neuroassistant.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    firebaseApiRepository: FirebaseApiRepository,
    private val databaseRepository: DatabaseRepository,
) : ViewModel() {

    val startDestination: Route = if (firebaseApiRepository.getCurrentUser() != null) {
        Route.ChatsList
    } else {
        Route.Login
    }

    var isSplashVisible by mutableStateOf(true)
        private set

    var isCreatingChat by mutableStateOf(false)
        private set

    var createdChatId by mutableLongStateOf(-1L)
        private set

    fun dismissSplash() {
        isSplashVisible = false
    }

    fun createChat(title: String) {
        if (isCreatingChat) return

        viewModelScope.launch {
            isCreatingChat = true

            runCatching { databaseRepository.createChat(title = title) }
                .onSuccess { chatId ->
                    createdChatId = chatId
                }

            isCreatingChat = false
        }
    }

    fun clearCreatedChat() {
        createdChatId = -1L
    }
}

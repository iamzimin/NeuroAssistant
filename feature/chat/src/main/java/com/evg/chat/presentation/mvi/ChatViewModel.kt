package com.evg.chat.presentation.mvi

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class ChatViewModel(

) : ContainerHost<ChatState, ChatSideEffect>, ViewModel() {
    override val container = container<ChatState, ChatSideEffect>(ChatState())

    fun dispatch(action: ChatAction) {
        when (action) {
            is ChatAction.FirstClass -> test()
            is ChatAction.SecondObject -> test()
        }
    }

    private fun test() = intent {
        //reduce { state.copy(variable = true) }
    }
}
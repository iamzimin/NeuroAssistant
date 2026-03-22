package com.evg.chats_list.presentation.mvi

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

class ChatsListViewModel(

) : ContainerHost<ChatsListState, ChatsListSideEffect>, ViewModel() {
    override val container = container<ChatsListState, ChatsListSideEffect>(ChatsListState())

    fun dispatch(action: ChatsListAction) {
        when (action) {
            is ChatsListAction.FirstClass -> test()
            is ChatsListAction.SecondObject -> test()
        }
    }

    private fun test() = intent {
        //reduce { state.copy(variable = true) }
    }
}
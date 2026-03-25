package com.evg.neuroassistant

import androidx.lifecycle.ViewModel
import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.neuroassistant.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    firebaseApiRepository: FirebaseApiRepository,
) : ViewModel() {

    val startDestination: Route = if (firebaseApiRepository.getCurrentUser() != null) {
        Route.ChatsList
    } else {
        Route.Login
    }

    var isSplashVisible by mutableStateOf(true)
        private set

    fun dismissSplash() {
        isSplashVisible = false
    }
}

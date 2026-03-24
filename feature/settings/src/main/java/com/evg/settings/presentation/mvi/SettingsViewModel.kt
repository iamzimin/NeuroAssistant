package com.evg.settings.presentation.mvi

import androidx.lifecycle.ViewModel
import com.evg.api.domain.utils.ServerResult
import com.evg.settings.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ContainerHost<SettingsState, SettingsSideEffect>, ViewModel() {
    override val container = container<SettingsState, SettingsSideEffect>(SettingsState())

    fun dispatch(action: SettingsAction) {
        when (action) {
            SettingsAction.Initialize -> initialize()
            SettingsAction.OnEditClicked -> enableEditMode()
            SettingsAction.OnCancelEditClicked -> cancelEdit()
            is SettingsAction.OnDisplayNameChanged -> updateDisplayName(action.value)
            is SettingsAction.OnPhotoSelected -> updateSelectedPhoto(action.bytes, action.fileExtension)
            SettingsAction.OnSaveProfileClicked -> saveProfile()
            SettingsAction.OnRetryBalanceClicked -> loadBalance()
            SettingsAction.OnRetryProfileUpdateClicked -> saveProfile()
            SettingsAction.OnLogoutClicked -> logout()
        }
    }

    private fun initialize() = intent {
        val profile = settingsRepository.getProfileInfo()
        if (profile == null) {
            postSideEffect(SettingsSideEffect.NavigateToAuth)
            return@intent
        }

        reduce {
            state.copy(
                profile = profile,
                editedDisplayName = profile.displayName,
            )
        }

        loadBalance()
    }

    private fun enableEditMode() = intent {
        reduce { state.copy(isEditing = true) }
    }

    private fun cancelEdit() = intent {
        reduce {
            state.copy(
                isEditing = false,
                editedDisplayName = state.profile?.displayName.orEmpty(),
                selectedPhotoBytes = null,
                selectedPhotoFileExtension = null,
            )
        }
    }

    private fun updateDisplayName(value: String) = intent {
        reduce { state.copy(editedDisplayName = value) }
    }

    private fun updateSelectedPhoto(bytes: ByteArray, fileExtension: String?) = intent {
        reduce {
            state.copy(
                isEditing = true,
                selectedPhotoBytes = bytes,
                selectedPhotoFileExtension = fileExtension,
            )
        }
    }

    private fun loadBalance() = intent {
        if (state.isBalanceLoading) return@intent

        reduce { state.copy(isBalanceLoading = true) }

        when (val result = settingsRepository.getGigaChatTokenBalance()) {
            is ServerResult.Success -> {
                reduce {
                    state.copy(
                        gigaChatTokens = result.data.toString(),
                        isBalanceLoading = false,
                    )
                }
            }

            is ServerResult.Error -> {
                reduce {
                    state.copy(
                        gigaChatTokens = "-",
                        isBalanceLoading = false,
                    )
                }
                postSideEffect(SettingsSideEffect.ShowBalanceError(result.error))
            }
        }
    }

    private fun saveProfile() = intent {
        if (state.isSaving) return@intent
        if (!state.canSave) {
            reduce { state.copy(isEditing = false) }
            return@intent
        }

        reduce { state.copy(isSaving = true) }

        when (
            val result = settingsRepository.updateProfile(
                displayName = state.editedDisplayName,
                photoBytes = state.selectedPhotoBytes,
                photoFileExtension = state.selectedPhotoFileExtension,
            )
        ) {
            is ServerResult.Success -> {
                reduce {
                    state.copy(
                        profile = result.data,
                        editedDisplayName = result.data.displayName,
                        selectedPhotoBytes = null,
                        selectedPhotoFileExtension = null,
                        isEditing = false,
                        isSaving = false,
                    )
                }
            }

            is ServerResult.Error -> {
                reduce { state.copy(isSaving = false) }
                postSideEffect(SettingsSideEffect.ShowProfileUpdateError(result.error))
            }
        }
    }

    private fun logout() = intent {
        if (state.isSigningOut) return@intent

        reduce { state.copy(isSigningOut = true) }
        runCatching { settingsRepository.logout() }
        postSideEffect(SettingsSideEffect.NavigateToAuth)
    }
}
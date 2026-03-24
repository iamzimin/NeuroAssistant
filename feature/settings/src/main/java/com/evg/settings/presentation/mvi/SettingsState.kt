package com.evg.settings.presentation.mvi

import com.evg.settings.domain.model.ProfileInfo

data class SettingsState(
    val profile: ProfileInfo? = null,
    val editedDisplayName: String = "",
    val selectedPhotoBytes: ByteArray? = null,
    val selectedPhotoFileExtension: String? = null,
    val gigaChatTokens: String = "-",
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val isSigningOut: Boolean = false,
    val isBalanceLoading: Boolean = false,
) {
    val hasPhotoChanges: Boolean
        get() = selectedPhotoBytes != null

    val hasNameChanges: Boolean
        get() = editedDisplayName.trim() != profile?.displayName.orEmpty().trim()

    val canSave: Boolean
        get() = !isSaving && (hasPhotoChanges || hasNameChanges)
}
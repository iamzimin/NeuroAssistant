package com.evg.settings.domain.repository

import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.GigaChatError
import com.evg.api.domain.utils.ServerResult
import com.evg.settings.domain.model.ProfileInfo

interface SettingsRepository {
    suspend fun getProfileInfo(): ProfileInfo?

    suspend fun getGigaChatTokenBalance(): ServerResult<Int, GigaChatError>

    suspend fun updateProfile(
        displayName: String?,
        photoBytes: ByteArray?,
        photoFileExtension: String?,
    ): ServerResult<ProfileInfo, FirebaseError>

    suspend fun logout()
}
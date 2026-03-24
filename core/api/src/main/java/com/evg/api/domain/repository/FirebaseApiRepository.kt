package com.evg.api.domain.repository

import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult
import com.google.firebase.auth.FirebaseUser

interface FirebaseApiRepository {
    suspend fun login(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError>
    suspend fun register(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError>
    fun getCurrentUser(): FirebaseUser?
    suspend fun getProfilePhotoBase64(): ServerResult<String?, FirebaseError>
    suspend fun updateProfile(
        displayName: String?,
        photoBase64: String?,
        photoFileExtension: String?,
    ): ServerResult<FirebaseUser?, FirebaseError>
    fun signOut()
}
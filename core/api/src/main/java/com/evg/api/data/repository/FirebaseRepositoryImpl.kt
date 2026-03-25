package com.evg.api.data.repository

import com.evg.api.domain.mapper.toFirebaseError
import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore,
): FirebaseApiRepository {
    override suspend fun login(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                ServerResult.Success(user)
            } else {
                FirebaseCrashlytics.getInstance().recordException(Exception("User is null in login function FirebaseRepositoryImpl"))
                ServerResult.Error(FirebaseError.UNKNOWN)
            }
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override suspend fun register(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user != null) {
                ServerResult.Success(user)
            } else {
                FirebaseCrashlytics.getInstance().recordException(Exception("User is null in register function FirebaseRepositoryImpl"))
                ServerResult.Error(FirebaseError.UNKNOWN)
            }
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override suspend fun loginWithGoogle(idToken: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebaseAuth.signInWithCredential(credential).await()
            ServerResult.Success(result.user)
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun getProfilePhotoBase64(): ServerResult<String?, FirebaseError> {
        val user = firebaseAuth.currentUser
            ?: return ServerResult.Error(FirebaseError.INVALID_CREDENTIAL)

        return try {
            val snapshot = firebaseFirestore.collection("users")
                .document(user.uid)
                .get()
                .await()

            ServerResult.Success(snapshot.getString("photoBase64"))
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override suspend fun updateProfile(
        displayName: String?,
        photoBase64: String?,
        photoFileExtension: String?,
    ): ServerResult<FirebaseUser?, FirebaseError> {
        val user = firebaseAuth.currentUser
            ?: return ServerResult.Error(FirebaseError.INVALID_CREDENTIAL)

        return try {
            val trimmedDisplayName = displayName?.trim()?.ifBlank { null }

            val profileBuilder = UserProfileChangeRequest.Builder()
            trimmedDisplayName?.let { profileBuilder.setDisplayName(it) }

            user.updateProfile(profileBuilder.build()).await()

            val profileData = mutableMapOf<String, Any>(
                "updatedAt" to FieldValue.serverTimestamp(),
            )

            trimmedDisplayName?.let { profileData["displayName"] = it }
            photoBase64?.let { profileData["photoBase64"] = it }
            photoFileExtension
                ?.takeIf { it.isNotBlank() }
                ?.let { profileData["photoFileExtension"] = it }

            firebaseFirestore.collection("users")
                .document(user.uid)
                .set(profileData, SetOptions.merge())
                .await()

            user.reload().await()
            ServerResult.Success(firebaseAuth.currentUser)
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
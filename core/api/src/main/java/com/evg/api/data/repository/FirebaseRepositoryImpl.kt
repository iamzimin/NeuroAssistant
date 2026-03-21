package com.evg.api.data.repository

import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): FirebaseApiRepository {
    private fun mapExceptionToFirebaseError(e: Exception): FirebaseError {
        return when (e) {
            is FirebaseAuthWeakPasswordException -> FirebaseError.WEAK_PASSWORD
            is FirebaseAuthUserCollisionException -> FirebaseError.EMAIL_ALREADY_IN_USE
            is FirebaseTooManyRequestsException -> FirebaseError.TOO_MANY_REQUESTS
            is FirebaseNetworkException -> FirebaseError.NETWORK_ERROR
            is FirebaseAuthException -> {
                when (e.errorCode) {
                    "ERROR_INVALID_CREDENTIAL" -> FirebaseError.INVALID_CREDENTIAL
                    "ERROR_USER_NOT_FOUND" -> FirebaseError.USER_NOT_FOUND
                    "ERROR_USER_DISABLED" -> FirebaseError.USER_DISABLED
                    else -> FirebaseError.UNKNOWN
                }
            }
            else -> FirebaseError.UNKNOWN
        }
    }


    override suspend fun login(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ServerResult.Success(result.user) // TODO null
        } catch (e: Exception) {
            ServerResult.Error(mapExceptionToFirebaseError(e))
        }
    }

    override suspend fun register(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ServerResult.Success(result.user) // TODO null
        } catch (e: Exception) {
            ServerResult.Error(mapExceptionToFirebaseError(e))
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
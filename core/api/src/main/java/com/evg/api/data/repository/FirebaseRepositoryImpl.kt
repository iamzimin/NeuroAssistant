package com.evg.api.data.repository

import com.evg.api.domain.mapper.toFirebaseError
import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): FirebaseApiRepository {
    override suspend fun login(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            ServerResult.Success(result.user) // TODO null
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override suspend fun register(email: String, password: String): ServerResult<FirebaseUser?, FirebaseError> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            ServerResult.Success(result.user) // TODO null
        } catch (e: Exception) {
            ServerResult.Error(e.toFirebaseError())
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
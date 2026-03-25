package com.evg.login.data.repository

import com.evg.api.domain.repository.FirebaseApiRepository
import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult
import com.evg.api.domain.utils.mapData
import com.evg.login.domain.repository.LoginRepository

class LoginRepositoryImpl(
    private val firebaseApi: FirebaseApiRepository,
) : LoginRepository {
    override suspend fun login(
        email: String,
        password: String
    ): ServerResult<Unit, FirebaseError> {
        return firebaseApi.login(email, password).mapData{ Unit }
    }

    override suspend fun register(
        email: String,
        password: String
    ): ServerResult<Unit, FirebaseError> {
        return firebaseApi.register(email, password).mapData { Unit }
    }

    override suspend fun loginWithGoogle(idToken: String): ServerResult<Unit, FirebaseError> {
        return firebaseApi.loginWithGoogle(idToken).mapData { Unit }
    }
}
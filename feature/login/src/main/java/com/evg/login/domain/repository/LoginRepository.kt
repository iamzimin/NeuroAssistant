package com.evg.login.domain.repository

import com.evg.api.domain.utils.FirebaseError
import com.evg.api.domain.utils.ServerResult

interface LoginRepository {
    suspend fun login(email: String, password: String): ServerResult<Unit?, FirebaseError>
    suspend fun register(email: String, password: String): ServerResult<Unit?, FirebaseError>
}
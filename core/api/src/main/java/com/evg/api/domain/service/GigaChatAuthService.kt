package com.evg.api.domain.service

import com.evg.api.data.remote.model.GigaChatTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Header

interface GigaChatAuthService {
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("oauth")
    suspend fun getAccessToken(
        @Header("RqUID") requestId: String,
        @Header("Authorization") authorization: String,
        @Field("scope") scope: String,
    ): GigaChatTokenResponse
}

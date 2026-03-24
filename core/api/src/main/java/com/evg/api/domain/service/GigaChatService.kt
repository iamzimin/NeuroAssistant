package com.evg.api.domain.service

import com.evg.api.data.remote.model.GigaChatRequest
import com.evg.api.data.remote.model.GigaChatResponse
import com.evg.api.data.remote.model.GigaChatBalanceResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface GigaChatService {
    @POST("chat/completions")
    suspend fun getAnswer(
        @Header("Authorization") authorization: String,
        @Body request: GigaChatRequest,
    ): GigaChatResponse

    @GET("files/{fileId}/content")
    suspend fun downloadFile(
        @Header("Authorization") authorization: String,
        @Path("fileId") fileId: String,
    ): ResponseBody

    @GET("balance")
    suspend fun getBalance(
        @Header("Authorization") authorization: String,
    ): GigaChatBalanceResponse
}